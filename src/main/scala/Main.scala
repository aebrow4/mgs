import etl.sources.discogs.parser.DiscogsLoader
import etl.sources.discogs.scripts.XmlSplitter

object Main extends App {
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
  //executeQuery(deleteAll)
  val loader = new DiscogsLoader(splitXmlPath)
  loader.load("Artist")
}
