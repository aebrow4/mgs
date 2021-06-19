package etl.sources.discogs.parser

import scala.xml.Node
import config.MgsTest

class ArtistParserTest extends MgsTest {
  private val projectDirectory = System.getProperty("user.dir")
  private val parser = Some(
    new ArtistParser(
      s"$projectDirectory/fixtures/artists.xml"
    )
  )

  it("deserializes") {
    val testId = 1
    val testNode = getArtistXmlNodeById(testId)
    val deserialized = parser.get.deserialize(testNode)

    assert(deserialized.discogsId == "1")
    assert(deserialized.name == "Led Zeppelin")
    assert(deserialized.realName.contains("Assorted englishmen"))
    assert(deserialized.dataQuality == "Correct")
    assert(deserialized.members == Seq("2", "3"))
    assert(
      deserialized.aliases == Seq(
        "4"
      )
    )
  }

  /**
    * Parse the (fixture) xml doc and return the single artist record
    * from it matching @param discogsId. Assumes the ID passed in exists.
    * @param discogsId
    * @return
    */
  private def getArtistXmlNodeById(discogsId: Int): Node = {
    parser.get
      .getRecords(parser.get.document)
      .filter { node =>
        node.\("id").text == discogsId.toString
      }
      .head
  }
}
