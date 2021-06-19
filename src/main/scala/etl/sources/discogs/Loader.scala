package etl.sources.discogs

import scala.collection.mutable.ListBuffer
import etl.sources.discogs.parser.ArtistParser
import graph.dataAccess.ArtistDataAccess
import graph.models.nodes.Artist

/**
  * Entry point of the ETL process
  * Parses discogs XML files and loads records in batches
  * into the neo4j db.
  */
class Loader(artistParser: ArtistParser, artistDataAccess: ArtistDataAccess) {
  private val artistXml = artistParser.document
  private val BatchSize = 1000
  def batchCreate(): Unit = {
    // Our position in the file
    var recordsRead = 0
    val totalRecords = artistParser.getRecords(artistXml).size

    var batch = new ListBuffer[Artist]()
    for (node <- artistParser.getRecords(artistXml)) yield {
      val artist = artistParser.deserialize(node)
      if (artistParser.acceptedDataQualities.contains(artist.dataQuality)) {
        batch += artist.toNeo4jModel
      }
      recordsRead += 1
      println(s"totalRecords $totalRecords, recordsRead $recordsRead")

      if (batch.size == BatchSize || recordsRead == totalRecords) {
        artistDataAccess.create(batch.toSet)
        println(s"created $batch")
        batch = ListBuffer[Artist]()
      }
    }
  }
}
