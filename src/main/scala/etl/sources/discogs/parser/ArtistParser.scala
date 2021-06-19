package etl.sources.discogs.parser

import scala.xml.{Elem, Node, NodeSeq}
import etl.sources.discogs.models.{Artist => DiscogsArtist}

class ArtistParser(xmlPath: String)
    extends DiscogsParser[DiscogsArtist](xmlPath) {

  /*********************** Xml Api ***********************/
  /**
    * Deserialize an xml artist node to an instance of
    * [[etl.sources.discogs.models.Artist]]
    *
    * @param artist xml node. Assumed to be <artist>
    * @return [[etl.sources.discogs.models.Artist]]
    */
  def deserialize(artist: Node): DiscogsArtist = {
    DiscogsArtist(
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
}
