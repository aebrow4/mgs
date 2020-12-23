import etl.sources.discogs.parser.DiscogsParser
import etl.sources.discogs.scripts.XmlSplitter
import graph.dataAccess.{ArtistDataAccess, Neo4jSessionFactory}
import org.neo4j.ogm.session.Session

object Main extends App {
  def sessionFactory(): Session =
    Neo4jSessionFactory.getInstance().getNeo4jSession()
  val path =
    "/Users/andrewb/Desktop/discogs"
  val artistsSplitter =
    new XmlSplitter(
      s"$path/discogs_20200101_artists.xml",
      "/Users/andrewb/Desktop/xml/artists",
      "Artists"
    )

  val releasesSplitter = {
    new XmlSplitter(
      s"$path/discogs_20190101_releases.xml",
      "/Users/andrewb/Desktop/xml/releases",
      "Releases"
    )
  }

  //artistsSplitter.split
  //releasesSplitter.split
  val splitXmlPath = "/Users/andrewb/Desktop/discogs/a100.xml"
  val artistDataAccess = new ArtistDataAccess({ () =>
    Neo4jSessionFactory.getInstance().getNeo4jSession
  })
  artistDataAccess.deleteAll()
  val loader = new DiscogsParser(splitXmlPath)
  loader.load("Artist")
}
