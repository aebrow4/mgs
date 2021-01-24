package etl.sources.discogs.parser

import etl.sources.discogs.models.Artist
import graph.dataAccess.{ArtistDataAccess, Neo4jSessionFactory}
import scala.xml.{Elem, Node, NodeSeq}
import graph.Utils.buildResultMap
import scala.collection.mutable.ListBuffer

class ArtistParser(xmlPath: String) extends DiscogsParser[Artist](xmlPath) {

  /*********************** Xml Api ***********************/
  /**
    * Deserialize an xml artist node to an instance of
    * [[etl.sources.discogs.models.Artist]]
    *
    * @param artist xml node. Assumed to be <artist>
    * @return [[etl.sources.discogs.models.Artist]]
    */
  def deserialize(artist: Node): Artist = {
    Artist(
      discogsId = getId(artist),
      name = escapeDoubleQuotes(getName(artist)),
      dataQuality = getDataQuality(artist),
      realName = getRealName(artist),
      aliases = getAliases(artist),
      members = getMembers(artist)
    )
  }

  def getRecords(elem: Elem): NodeSeq = elem.\("artist")

  protected def getRealName(elem: Node): Option[String] = {
    val realName = elem.\("realname").text
    if (realName.nonEmpty) {
      Some(realName)
    } else {
      None
    }
  }

  /**
    * Get the discogs Ids of any aliases for this artist
    * @param artistNode  Artist node
    * @return The Ids of the associated Artist records, if any
    */
  private def getAliases(artistNode: Node): Seq[String] = {
    artistNode.\("aliases").\("name").map { node =>
      node.attribute("id").map(_.text).getOrElse("")
    }
  }

  /**
    * Get the discogs Ids of any members for this artist
    * @param artistNode  Artist node
    * @return The Ids of the associated Artist records, if any
    */
  private def getMembers(artistNode: Node): Seq[String] = {
    artistNode.\("members").\("name").map { node =>
      node.attribute("id").map(_.text).getOrElse("")
    }
  }

  /*********************** Neo4j Api ***********************/
  val dataAccessScala = new ArtistDataAccess({ () =>
    Neo4jSessionFactory.getInstance().getNeo4jSession
  })

  def batchCreate(): Unit = {
    var batch = new ListBuffer[Artist]()
    for (node <- getRecords(document)) yield {
      val artist = deserialize(node)
      if (acceptedDataQualities.contains(artist.dataQuality)) {
        batch += artist
      }

      if (batch.length >= BatchSize) {
        dataAccessScala.create(batch.map(_.toOgm))
        batch = new ListBuffer[Artist]()
      }
    }
  }

  /**
    * Given a discogs Artist xml file, process it in batches such that the artist-artist
    * relationships (IsAlias/HasAlias, IsMember/HasMember) between all artists in the
    * file are created. Assumes that all artists to be updated have already been created
    * in neo4j.
    * An artist may have lists of members and aliases. Build a mapping of ids:
    * (artistID) -> (aliasIds, memberIds)
    * and once the size of the mapping is equal to the batch size, fetch all the records
    * from the db, relate them to one another, commit, and start over for the next batch.
    * Updates are idempotent.
    */
  def batchUpdate(): Unit = {
    // Our position in the file
    var lineNumber = 0

    // A collection of ids whose records will be updated in the batch.
    var artistIdsToSubArtistIds = collection.mutable
      .Map[Long, (Set[Long], Set[Long])]()
    for (node <- getRecords(document)) yield {
      val artist = deserialize(node)
      artistIdsToSubArtistIds =
        artistIdsToSubArtistIds + (artist.discogsId.toLong -> (artist.aliases
          .map(_.toLong)
          .toSet, artist.members.map(_.toLong).toSet))
      lineNumber += 1

      if (lineNumber % BatchSize == 0 || lineNumber == fileLength) {
        val aliasIds = artistIdsToSubArtistIds.valuesIterator.toList
          .flatMap(_._1)
          .toSet
        val memberIds = artistIdsToSubArtistIds.valuesIterator.toList
          .flatMap(_._2)
          .toSet

        // Fetch records
        val artistRecords =
          dataAccessScala.getByDiscogsId(
            artistIdsToSubArtistIds.keySet
          )
        val aliasRecords =
          buildResultMap(
            dataAccessScala.getByDiscogsId(aliasIds)
          )
        val memberRecords = {
          buildResultMap(
            dataAccessScala.getByDiscogsId(memberIds)
          )
        }

        // establish the relationships in memory
        artistRecords.foreach { artist =>
          val id = artist.getDiscogsId
          val aliasIds = artistIdsToSubArtistIds.get(id).get._1
          aliasIds.foreach { aliasId =>
            aliasRecords.get(aliasId).foreach { alias =>
              dataAccessScala.createBidirectionalAliasEdges(alias, artist)
            }
          }
          val memberIds = artistIdsToSubArtistIds.get(id).get._2
          memberIds.foreach { memberId =>
            memberRecords.get(memberId).foreach { member =>
              dataAccessScala.createBidirectionalMemberEdges(member, artist)
            }
          }
        }
        // update db and clear id map
        dataAccessScala.update(iteratorToIterable(artistRecords))
        artistIdsToSubArtistIds = artistIdsToSubArtistIds.empty
      }
    }
  }
}
