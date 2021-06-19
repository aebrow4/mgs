package etl.sources.discogs

import config.MgsTest
import graph.dataAccess.{ArtistDataAccess, LocalTestDriver}

class LoaderTest extends MgsTest {
  val testDriverProvider = new LocalTestDriver()
  val testDriver = testDriverProvider.driver
  val artistDataAccess = new ArtistDataAccess(testDriver)

  override def beforeEach(): Unit = testDriverProvider.clearDb()

  it("loads all records if there is less than a full batch") {}
  it("loads all records if there is more than a batch") {}
}
