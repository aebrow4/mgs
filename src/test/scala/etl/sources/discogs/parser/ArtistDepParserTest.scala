package etl.sources.discogs.parser

import scala.xml.Node
import org.scalatest.{BeforeAndAfter, FunSpec, FunSpecLike}
import org.scalatestplus.junit.JUnitSuite

class ArtistDepParserTest extends JUnitSuite {
  //private val projectDirectory = System.getProperty("user.dir")
  //var testSession: Option[Session] = None
  //var dataAccess: Option[ArtistDataAccessDep] = None
  //var parser: Option[ArtistParser] = None
  //@Rule
  //var neoServer = new Neo4jRule()

  //@Before
  //def setup: Unit = {
  //  val config = new Configuration.Builder()
  //    .uri(neoServer.boltURI().toString)
  //    .build()
  //  val sessionFactory = new SessionFactory(config, "graph.models")
  //  testSession = Some(sessionFactory.openSession())
  //  testSession.get.purgeDatabase()
  //  dataAccess = Some(new ArtistDataAccessDep({ () => testSession.get }))
  //  parser = Some(
  //    new ArtistParser(
  //      s"$projectDirectory/fixtures/artists.xml",
  //      dataAccess.get
  //    )
  //  )
  //}

  //@Test
  //def itDeserializes() {
  //  val testId = 1
  //  val testNode = getArtistXmlNodeById(testId)
  //  val deserialized = parser.get.deserialize(testNode)

  //  assert(deserialized.discogsId == "1")
  //  assert(deserialized.name == "Led Zeppelin")
  //  assert(deserialized.realName.contains("Assorted englishmen"))
  //  assert(deserialized.dataQuality == "Correct")
  //  assert(deserialized.members == Seq("2", "3"))
  //  assert(
  //    deserialized.aliases == Seq(
  //      "4"
  //    )
  //  )
  //}

  //@Test
  //def createRecords() {
  //  parser.get.batchCreate()

  //  val createdArtists =
  //    buildResultMap(dataAccess.get.getByDiscogsId(Set(1L, 2L, 3L, 4L)))

  //  val ledZeppelin = createdArtists.get(1)
  //  assert(ledZeppelin.get.getName == "Led Zeppelin")
  //  assert(ledZeppelin.get.getAliases.size() == 0)
  //  assert(ledZeppelin.get.getMembers.size() == 0)
  //}

  ///**
  //  * Parse the (fixture) xml doc and return the single artist record
  //  * from it matching @param discogsId. Assumes the ID passed in exists.
  //  * @param discogsId
  //  * @return
  //  */
  //private def getArtistXmlNodeById(discogsId: Int): Node = {
  //  parser.get
  //    .getRecords(parser.get.document)
  //    .filter { node =>
  //      node.\("id").text == discogsId.toString
  //    }
  //    .head
  //}
}
