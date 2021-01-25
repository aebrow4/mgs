import etl.sources.discogs.parser.ArtistParser
import graph.dataAccess.{ArtistDataAccess, Neo4jSessionFactory}
import java.io.File
import org.neo4j.ogm.session.Session

object Main extends App {
  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
  def sessionFactory(): Session =
    Neo4jSessionFactory.getInstance().getNeo4jSession()
  val inputDir =
    "/Users/andrewb/Desktop/xml"
  val outputDir = "/Users/andrewb/Desktop/xml"
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

  val artistDataAccess = new ArtistDataAccess({ () =>
    Neo4jSessionFactory.getInstance().getNeo4jSession
  })

  private def createArtistsFromDir(inputDir: String): Unit = {
    for (f <- getListOfFiles(inputDir)) yield {
      val parser = new ArtistParser(f.getPath, artistDataAccess)
      parser.batchCreate()
    }
  }
  private def updateArtistsFromDir(inputDir: String): Unit = {
    for (f <- getListOfFiles(inputDir)) yield {
      val parser = new ArtistParser(f.getPath, artistDataAccess)
      parser.batchUpdate()
    }
  }
  createArtistsFromDir(inputDir)
  //updateArtistsFromDir(inputDir)
}
