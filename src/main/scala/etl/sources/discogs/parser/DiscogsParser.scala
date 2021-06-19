package etl.sources.discogs.parser

import scala.collection.mutable.ListBuffer
import scala.io.BufferedSource
import scala.xml._
import utils.AutomaticResourceMgmt.using

/**
  * Interface for parsing a discogs XML file and updating the neo4j db.
  * The interface has two groups of functionality: one for interacting
  * with the XML document, the other for interacting with neo4j.
  * @param xmlPath Path to the XML file to read
  * @tparam T Type of the corresponding discogs model for the file, e.g.
  *           etl.sources.discogs.models.Artist
  */
abstract class DiscogsParser[T](xmlPath: String) {

  /*********************** Xml Api ***********************/
  lazy val document: Elem = {
    XML.load(xmlPath)
  }

  lazy val fileLength: Int = getFileLength()

  private def getFileLength(): Int = {
    val file: BufferedSource = scala.io.Source.fromFile(xmlPath)

    using[Int, BufferedSource](file) { file =>
      file.getLines().size
    }
  }

  // To minimize noise in the Db, records whose data quality is not
  // in this allowlist are ignored by the parser
  val acceptedDataQualities =
    Seq("Correct", "Complete and Correct", "Needs Vote")

  /**
    * Deserialize an XML representation of an entity into an instance
    * of its discogs model T
    * @param xmlNode
    * @return
    */
  def deserialize(xmlNode: Node): T

  /**
    * Select all XML nodes of tag @param elem
    * @param elem The XML tag to select, e.g. artist, release, label
    * @return
    */
  def getRecords(elem: Elem): NodeSeq

  // Selector implementations are provided for elements that are common to
  // all three entities
  protected def getId(elem: Node): String = elem.\("id").text
  protected def getName(elem: Node): String = elem.\("name").text
  protected def getDataQuality(elem: Node): String = elem.\("data_quality").text

  protected def escapeDoubleQuotes(str: String): String = {
    // "Danny" -> \"Danny\"
    str.replaceAll(""""""", """\\"""")
  }

  protected def iteratorToIterable[T](iterator: Iterator[T]): Iterable[T] = {
    var iterable = ListBuffer[T]()
    while (iterator.hasNext) {
      val item = iterator.next()
      iterable += item
    }
    iterable
  }
}
