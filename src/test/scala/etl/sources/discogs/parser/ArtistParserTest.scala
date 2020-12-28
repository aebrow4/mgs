package etl.sources.discogs.parser

import scala.xml.Node
import org.scalatest.FunSpec

class ArtistParserTest extends FunSpec {
  val projectDirectory = System.getProperty("user.dir")
  val parser = new ArtistParser(
    projectDirectory + "/fixtures/artists.xml"
  )

  describe("artists parser") {
    it("deserializes") {
      val testId = 2
      val testNode = getArtistXmlNodeById(testId)
      val deserialized = parser.deserialize(testNode)

      assert(deserialized.discogsId == "2")
      assert(deserialized.name == "Mr. James Barth & A.D.")
      assert(deserialized.realName.contains("Cari Lekebusch & Alexi Delano"))
      assert(deserialized.dataQuality == "Correct")
      assert(deserialized.members == Seq("26", "27"))
      assert(
        deserialized.aliases == Seq(
          "2470",
          "19536",
          "103709",
          "384581",
          "1779857"
        )
      )
    }
  }

  /**
    * Parse the (fixture) xml doc and return the single artist record
    * from it matching @param discogsId. Assumes the ID passed in exists.
    * @param discogsId
    * @return
    */
  private def getArtistXmlNodeById(discogsId: Int): Node = {
    parser
      .getRecords(parser.document)
      .filter { node =>
        node.\("id").text == discogsId.toString
      }
      .head
  }
}
