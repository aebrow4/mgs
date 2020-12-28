package etl.sources.discogs.parser

import etl.sources.discogs.models.Artist
import graph.dataAccess.{ArtistDataAccess, Neo4jSessionFactory}
import graph.models
import scala.xml.{Elem, Node, NodeSeq}
import utils.Converters.scalaToJavaSet
import graph.Utils.buildResultMap
import java.util
import collection.JavaConverters._
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
  val dataAccess = new ArtistDataAccess({ () =>
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
        dataAccess.create(batch.map(_.toOgm).asJava)
        batch = new ListBuffer[Artist]()
      }
    }
  }

  /**
    * Idempotently add the two relationships that an artist record may have to other
    * artist records.
    * A map of artist IDs to that artist's alias and member IDs is built iteratively.
    * When the batch size is reached, all artist records for IDs in the map are fetched.
    * The relationships are added between the models, the batch is committed, reset,
    * and iteration continues.
    */
  def batchUpdate(): Unit = {
    var currentBatch = 0
    var artistIdsToSubArtistIds = collection.mutable
      .Map[Long, (Set[Long], Set[Long])]()
    for (node <- getRecords(document)) yield {
      val artist = deserialize(node)
      artistIdsToSubArtistIds =
        artistIdsToSubArtistIds + (artist.discogsId.toLong -> (artist.aliases
          .map(_.toLong)
          .toSet, artist.members.map(_.toLong).toSet))
      currentBatch += 1

      // FIXME we also need to process the final group of records in each file, where currentBatch
      // will be less than BatchSize
      if (currentBatch == BatchSize) {
        // get records needed for update
        // artist records is a set we'll iterate over; the other two are maps of
        // ids to records
        val artistRecords: util.Iterator[models.Artist] =
          dataAccess.getByDiscogsId(
            scalaToJavaSet(artistIdsToSubArtistIds.keySet)
          )
        val aliasIds = artistIdsToSubArtistIds.valuesIterator.toList
          .map(_._1)
          .flatten
          .toSet
        val memberIds = artistIdsToSubArtistIds.valuesIterator.toList
          .map(_._2)
          .flatten
          .toSet
        val aliasRecords =
          buildResultMap(
            dataAccess.getByDiscogsId(scalaToJavaSet(aliasIds))
          )
        val memberRecords = {
          buildResultMap(
            dataAccess.getByDiscogsId(scalaToJavaSet(memberIds))
          )
        }

        // establish the relationships
        artistRecords.forEachRemaining { artist =>
          val id = artist.getDiscogsId
          val aliasIds = artistIdsToSubArtistIds.get(id).get._1
          aliasIds.foreach { aliasId =>
            println(s"building edges for alias $aliasId")
            aliasRecords.get(aliasId).foreach { alias =>
              dataAccess.createBidirectionalAliasEdges(alias, artist)
            }
          }
          val memberIds = artistIdsToSubArtistIds.get(id).get._2
          memberIds.foreach { memberId =>
            println(s"building edges for member $memberId")
            memberRecords.get(memberId).foreach { member =>
              dataAccess.createBidirectionalMemberEdges(member, artist)
            }
          }
        }
        // need to get artistRecords from set to iterator
        var iterable =
          scala.collection.mutable.ListBuffer[graph.models.Artist]()
        while (artistRecords.hasNext) {
          val artist = artistRecords.next()
          iterable += artist
        }

        dataAccess.update(iterable.asJava)
        currentBatch = 0
        println("=================New Batch===================")
      }
    }
  }
}
