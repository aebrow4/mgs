package graph.dataAccess

import config.MgsTest
import graph.dataAccess.Fixtures.{
  DefaultArtist,
  DefaultArtist2,
  DefaultArtist2DataQuality,
  DefaultArtist2Name,
  DefaultArtist2RealName,
  DefaultArtistDataQuality,
  DefaultArtistId,
  DefaultArtistId2,
  DefaultArtistName,
  DefaultArtistRealName,
  HairLady,
  Nicole
}

import scala.concurrent.Await
import scala.concurrent.duration._

class ArtistDataAccessTest extends MgsTest {
  val testDriverProvider = new LocalTestDriver()
  val testDriver = testDriverProvider.driver
  val artistDataAccess = new ArtistDataAccess(testDriver)

  override def beforeEach(): Unit = testDriverProvider.clearDb()

  describe("createArtists") {
    it("creates single") {
      Await.result(artistDataAccess.create(DefaultArtist), 2.seconds)

      val result =
        Await.result(
          artistDataAccess.getByDiscogsId(DefaultArtistId),
          2.seconds
        )
      val record = result.get(DefaultArtistId)
      assert(record.isDefined)
      record.foreach { r =>
        assert(r.discogsId == DefaultArtistId)
        assert(r.name == DefaultArtistName)
        assert(r.dataQuality == DefaultArtistDataQuality)
        assert(r.realName == DefaultArtistRealName)
      }
    }

    it("creates multiple") {
      Await.result(
        artistDataAccess.create(Set(DefaultArtist, DefaultArtist2)),
        2.seconds
      )

      val resultA =
        Await.result(
          artistDataAccess.getByDiscogsId(DefaultArtistId),
          2.seconds
        )
      val resultB =
        Await.result(
          artistDataAccess.getByDiscogsId(DefaultArtistId2),
          2.seconds
        )

      val recordA = resultA.get(DefaultArtistId)
      val recordB = resultB.get(DefaultArtistId2)
      assert(recordA.isDefined)
      recordA.map { r =>
        assert(r.discogsId == DefaultArtistId)
        assert(r.name == DefaultArtistName)
        assert(r.dataQuality == DefaultArtistDataQuality)
        assert(r.realName == DefaultArtistRealName)
      }
      assert(recordB.isDefined)
      recordB.map { r =>
        assert(r.discogsId == DefaultArtistId2)
        assert(r.name == DefaultArtist2Name)
        assert(r.dataQuality == DefaultArtist2DataQuality)
        assert(r.realName == DefaultArtist2RealName)
      }
    }
  }

  describe("getArtistByDiscogsId") {
    it("gets records") {
      Await.result(
        artistDataAccess.create(Set(DefaultArtist, DefaultArtist2)),
        2.seconds
      )

      val result = Await.result(
        artistDataAccess.getByDiscogsId(Set(DefaultArtistId, DefaultArtistId2)),
        2.seconds
      )

      val recordA = result.get(DefaultArtistId)
      assert(recordA.isDefined)
      recordA.map { r =>
        assert(r.discogsId == DefaultArtistId)
        assert(r.name == DefaultArtistName)
        assert(r.dataQuality == DefaultArtistDataQuality)
        assert(r.realName == DefaultArtistRealName)
      }
      val recordB = result.get(DefaultArtistId2)
      assert(recordB.isDefined)
      recordB.foreach { r =>
        assert(r.discogsId == DefaultArtistId2)
        assert(r.name == DefaultArtist2Name)
        assert(r.dataQuality == DefaultArtist2DataQuality)
        assert(r.realName == DefaultArtist2RealName)
      }
    }

    it("returns none if no record is found") {
      val result = Await.result(
        artistDataAccess.getByDiscogsId(45),
        2.seconds
      )

      assert(!result.contains(45))
    }
  }

  describe("update") {
    //it("creates alias relationships") {
    //  Await.result(
    //    artistDataAccess.create(Set(Nicole, HairLady)),
    //    2.seconds
    //  )

    //  // Make HairLady an alias of Nicole, bidirectionally
    //  Await.result(
    //    artistDataAccess.update(Nicole, Set(HairLady), Set()),
    //    2.seconds
    //  )

    //  val results = Await.result(
    //    artistDataAccess.getByDiscogsId(Set(DefaultArtistId, DefaultArtistId2)),
    //    2.seconds
    //  )
    //  val nicole = results.get(DefaultArtistId)

    //  assert(nicole.isDefined)
    //  nicole.map { nicole =>
    //    nic
    //  }
    //}
  }
}
