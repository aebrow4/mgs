package etl.sources.discogs

import graph.dataAccess.{ArtistDataAccess, LocalTestDriver}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec

class LoaderTest extends AnyFunSpec with BeforeAndAfterEach {
  val testDriverProvider = new LocalTestDriver()
  val testDriver = testDriverProvider.driver
  val artistDataAccess = new ArtistDataAccess(testDriver)

  override def beforeEach(): Unit = testDriverProvider.clearDb()

}
