package etl.sources.discogs.parser

import etl.sources.discogs.models.{Artist => DiscogsArtist}
import graph.dataAccess.ArtistDataAccess
import graph.models.nodes.Artist

import scala.xml.{Elem, Node, NodeSeq}

import scala.collection.mutable.ListBuffer

class ArtistParser(xmlPath: String, val dataAccess: ArtistDataAccess)
    extends DiscogsParser[DiscogsArtist](xmlPath) {

  /*********************** Xml Api ***********************/
  /**
    * Deserialize an xml artist node to an instance of
    * [[etl.sources.discogs.models.Artist]]
    *
    * @param artist xml node. Assumed to be <artist>
    * @return [[etl.sources.discogs.models.Artist]]
    */
  def deserialize(artist: Node): DiscogsArtist =
    DiscogsArtist("123", "bob", "ok", None, Seq(), Seq())
  //def deserialize(artist: Node): DiscogsArtist = {
  //  DiscogsArtist(
  //    discogsId = getId(artist),
  //    name = escapeDoubleQuotes(getName(artist)),
  //    dataQuality = getDataQuality(artist),
  //    realName = getRealName(artist),
  //    aliases = getAliases(artist),
  //    members = getMembers(artist)
  //  )
  //}

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
  def batchCreate(): Unit = {
    // Our position in the file
    //var recordsRead = 0
    //val totalRecords = getRecords(document).size

    //var batch = new ListBuffer[Artist]()
    //for (node <- getRecords(document)) yield {
    //  val artist = deserialize(node)
    //  if (acceptedDataQualities.contains(artist.dataQuality)) {
    //    batch += artist
    //  }
    //  recordsRead += 1
    //  println(s"totalRecords $totalRecords, recordsRead $recordsRead")

    //  if (batch.size == BatchSize || recordsRead == totalRecords) {
    //    dataAccess.create(batch.map(_.toOgm))
    //    println(s"created $batch")
    //    batch = new ListBuffer[Artist]()
    //  }
    //}
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
    //var lineNumber = 0

    //// A collection of ids whose records will be updated in the batch.
    //var artistIdsToSubArtistIds = collection.mutable
    //  .Map[Long, (Set[Long], Set[Long])]()
    //for (node <- getRecords(document)) yield {
    //  val artist = deserialize(node)
    //  artistIdsToSubArtistIds =
    //    artistIdsToSubArtistIds + (artist.discogsId.toLong -> (artist.aliases
    //      .map(_.toLong)
    //      .toSet, artist.members.map(_.toLong).toSet))
    //  lineNumber += 1

    //  if (lineNumber % BatchSize == 0 || lineNumber == fileLength) {
    //    val aliasIds = artistIdsToSubArtistIds.valuesIterator.toList
    //      .flatMap(_._1)
    //      .toSet
    //    val memberIds = artistIdsToSubArtistIds.valuesIterator.toList
    //      .flatMap(_._2)
    //      .toSet

    //    // Fetch records
    //    val artistRecords =
    //      dataAccess.getByDiscogsId(
    //        artistIdsToSubArtistIds.keySet
    //      )

    //    val a: Iterator[ArtistDep] =
    //      dataAccess.getByDiscogsId(aliasIds)
    //    val aliasRecords =
    //      buildResultMap(
    //        dataAccess.getByDiscogsId(aliasIds)
    //      )
    //    val memberRecords = {
    //      buildResultMap(
    //        dataAccess.getByDiscogsId(memberIds)
    //      )
    //    }

    //    // establish the relationships in memory
    //    artistRecords.foreach { artist =>
    //      val id = artist.getDiscogsId
    //      val aliasIds = artistIdsToSubArtistIds.get(id).get._1
    //      aliasIds.foreach { aliasId =>
    //        aliasRecords.get(aliasId).foreach { alias =>
    //          dataAccess.createBidirectionalAliasEdges(alias, artist)
    //        }
    //      }
    //      val memberIds = artistIdsToSubArtistIds.get(id).get._2
    //      memberIds.foreach { memberId =>
    //        memberRecords.get(memberId).foreach { member =>
    //          dataAccess.createBidirectionalMemberEdges(member, artist)
    //        }
    //      }
    //    }
    //    // update db and clear id map
    //    dataAccess.update(iteratorToIterable(artistRecords))
    //    artistIdsToSubArtistIds = artistIdsToSubArtistIds.empty
    //  }
    //}
  }
}
