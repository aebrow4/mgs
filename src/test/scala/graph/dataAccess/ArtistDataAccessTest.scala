package graph.dataAccess

import graph.models.nodes.Artist

import scala.concurrent.Await
import scala.concurrent.duration._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.BeforeAndAfterEach

class ArtistDataAccessTest extends AnyFunSpec with BeforeAndAfterEach {
  val testDriverProvider = new LocalTestDriver()
  val testDriver = testDriverProvider.driver
  val artistDataAccess = new ArtistDataAccess(testDriver)

  override def beforeEach(): Unit = testDriverProvider.clearDb()

  describe("createArtists") {
    it("creates") {
      val simpleArtist = Artist(
        discogsId = 123,
        name = "Bill",
        dataQuality = "legit",
        realName = Some("william")
      )

      val result =
        Await.result(artistDataAccess.create(simpleArtist), 2.seconds)

      assert(result.discogsId == 123)
      assert(result.name == "Bill")
      assert(result.dataQuality == "legit")
      assert(result.realName.contains("william"))

    }
  }

  describe("getArtistByDiscogsId") {
    it("returns record when it exists") {
      val simpleArtist = Artist(
        discogsId = 123,
        name = "Bill",
        dataQuality = "legit",
        realName = Some("william")
      )

      Await.result(artistDataAccess.create(simpleArtist), 2.seconds)

      val result = Await.result(artistDataAccess.getByDiscogsId(123), 2.seconds)

      assert(result.isDefined)
      result.foreach { result =>
        assert(result.discogsId == 123)
        assert(result.name == "Bill")
        assert(result.dataQuality == "legit")
        assert(result.realName.contains("william"))
      }
    }

    it("returns none if no record is found") {
      val result = Await.result(artistDataAccess.getByDiscogsId(555), 2.seconds)

      assert(result.isEmpty)
    }
  }
}
