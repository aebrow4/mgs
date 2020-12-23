package etl.sources.discogs.parser

import scala.xml._
import etl.sources.discogs.models.Artist
import graph.dataAccess.DataAccess.createArtist
import graph.dataAccess.DataAccessOgm

/**
  * Load the contents of an XML file into the Neo4j db.
  * Assumes the file will be one of three types: Artists, Labels,
  * or Releases
  * @param xmlPath path of XML file to read
  */
class DiscogsLoader(xmlPath: String) {
  def load(fileType: String): Unit = {
    val document = parse
    getArtistsXml(document).foreach(loadArtist)
  }

  private def parse: Elem = {
    XML.load(xmlPath)
  }

  /**
    * Given an xml <artist> node, create an Artist in Neo4j
    * @param artistXml [[scala.xml.Node]]
    */
  private def loadArtist(artistXml: Node): Unit = {
    val artist = serializeArtist(artistXml)
    //createArtist(artist.toNeo4j)
    DataAccessOgm.createArtist(artist.toOgm)
  }

  /**
    * Serialize an xml artist node to an instance of
    * [[etl.sources.discogs.models.Artist]]
    *
    * @param artist xml node. Assumed to be <artist>
    * @return [[etl.sources.discogs.models.Artist]]
    */
  private def serializeArtist(artist: Node): Artist = {
    Artist(
      discogsId = getId(artist),
      name = escapeDoubleQuotes(getName(artist)),
      dataQuality = getDataQuality(artist),
      realName = getRealName(artist),
      aliases = getAliases(artist),
      members = getMembers(artist)
    )
  }

  private def getId(elem: Node): String = elem.\("id").text
  private def getName(elem: Node): String = elem.\("name").text
  private def getDataQuality(elem: Node): String = elem.\("data_quality").text
  private def getRealName(elem: Node): Option[String] = {
    val realName = elem.\("realname").text
    if (realName.nonEmpty) {
      Some(realName)
    } else {
      None
    }
  }

  /**
    * Get the discogs Ids of any aliases for this artist
    * @param elem  Artist node
    * @return The Ids of the associated Artist records, if any
    */
  private def getAliases(elem: Node): Option[Seq[String]] = {
    val artistIds = elem.\("aliases").\("name").toList.map { nameTag =>
      nameTag.attribute("id").toString
    }
    if (artistIds.nonEmpty) {
      Some(artistIds)
    } else {
      None
    }
  }

  /**
    * Get the discogs Ids of any members for this artist
    * @param elem  Artist node
    * @return The Ids of the associated Artist records, if any
    */
  private def getMembers(elem: Node): Option[Seq[String]] = {
    val names = elem.\("members").\("name").map(_.text)
    if (names.nonEmpty) {
      Some(names)
    } else {
      None
    }
  }

  private def getArtistsXml(elem: Elem): NodeSeq = elem.\("artist")
  private def getReleasesXml(elem: Elem): NodeSeq = elem.\("release")
  private def getLabelsXml(elem: Elem): NodeSeq = elem.\("label")

  private def escapeDoubleQuotes(str: String): String = {
    // "Danny" -> \"Danny\"
    str.replaceAll(""""""", """\\"""")
  }
}
