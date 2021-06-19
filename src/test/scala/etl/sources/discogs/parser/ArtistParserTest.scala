package etl.sources.discogs.parser

import graph.dataAccess.{ArtistDataAccess, LocalTestDriver}
import org.scalatest.funspec.AnyFunSpec

import scala.xml.Node
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FunSpec, FunSpecLike}

class ArtistParserTest extends AnyFunSpec with BeforeAndAfterEach {
  private val projectDirectory = System.getProperty("user.dir")
  val testDriverProvider = new LocalTestDriver()
  val testDriver = testDriverProvider.driver
  val artistDataAccess = new ArtistDataAccess(testDriver)
  val parser = Some(
    new ArtistParser(
      s"$projectDirectory/fixtures/artists.xml",
      artistDataAccess
    )
  )

  override def beforeEach(): Unit = {
    testDriverProvider.clearDb()
  }

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

  it("creates records") {
    //parser.get.batchCreate()

    //val createdArtists =
    //  buildResultMap(artistDataAccess.getByDiscogsId(Set(1L, 2L, 3L, 4L)))

    //val ledZeppelin = createdArtists.get(1)
    //assert(ledZeppelin.get.getName == "Led Zeppelin")
    //assert(ledZeppelin.get.getAliases.size() == 0)
    //assert(ledZeppelin.get.getMembers.size() == 0)
  }

  ///**
  //  * Parse the (fixture) xml doc and return the single artist record
  //  * from it matching @param discogsId. Assumes the ID passed in exists.
  //  * @param discogsId
  //  * @return
  //  */
  private def getArtistXmlNodeById(discogsId: Int): Node = {
    parser.get
      .getRecords(parser.get.document)
      .filter { node =>
        node.\("id").text == discogsId.toString
      }
      .head
  }
}
