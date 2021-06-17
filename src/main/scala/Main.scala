import etl.sources.discogs.parser.ArtistParser
import graph.dataAccess.{ArtistDataAccess, Driver}
import neotypes.implicits.syntax.all._

import java.io.File
import scala.concurrent.duration._

import scala.concurrent.Await

object Main extends App {
  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
  //def sessionFactory(): Session =
  //  Neo4jSessionFactory.getInstance().getNeo4jSession()
  val inputDir =
    "/Users/andrewb/Desktop/xml"
  val outputDir = "/Users/andrewb/Desktop/xml"

  val driver = new Driver(
    user = "neo4j",
    password = sys.env("NEO4J_PASSWORD"),
    ipAddr = sys.env("NEO4J_IP")
  ).driver

  private def helloWorldNeottypes = {
    val people =
      "MATCH (a: Artist) RETURN a.name LIMIT 10".query[(String)].list(driver)
    val res = Await.result(people, 2.seconds)
    println(res)
  }
  val artistApi = new ArtistDataAccess(driver)
  //val res = Await.result(artistApi.getByDiscogsIds(Set(7057893)), 2.seconds)
  val res = Await.result(artistApi.getByDiscogsId(7058109), 2.seconds)
  println(res)

  //val artistsSplitter =
  //  new XmlSplitter(
  //    s"$inputDir/discogs_20200101_artists.xml",
  //    s"$outputDir/artists",
  //    "Artists"
  //  )

  //val releasesSplitter = {
  //  new XmlSplitter(
  //    s"$inputDir/discogs_20190101_releases.xml",
  //    s"$outputDir/releases",
  //    "Releases"
  //  )
  //}

  //artistsSplitter.split
  //releasesSplitter.split
  //val splitXmlPath = "/Users/andrewb/Desktop/discogs/a100.xml"

  // java driver stuff
  //val artistDataAccess = new ArtistDataAccess({ () =>
  //  Neo4jSessionFactory.getInstance().getNeo4jSession
  //})

  //private def createArtistsFromDir(inputDir: String): Unit = {
  //  for (f <- getListOfFiles(inputDir)) yield {
  //    val parser = new ArtistParser(f.getPath, artistDataAccess)
  //    parser.batchCreate()
  //  }
  //}
  //private def updateArtistsFromDir(inputDir: String): Unit = {
  //  for (f <- getListOfFiles(inputDir)) yield {
  //    val parser = new ArtistParser(f.getPath, artistDataAccess)
  //    parser.batchUpdate()
  //  }
  //}
  //createArtistsFromDir(inputDir)
  //updateArtistsFromDir(inputDir)
}
