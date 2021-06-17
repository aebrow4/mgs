package etl.sources.discogs.models

import graph.models.nodes.{Artist => Neo4jArtist}

case class Artist(
    discogsId: String,
    name: String,
    dataQuality: String,
    realName: Option[String],
    aliases: Seq[String],
    members: Seq[String]
) {

  def toNeo4jModel: Neo4jArtist = {
    Neo4jArtist(
      discogsId.toLong,
      name,
      dataQuality,
      realName
    )
  }
}
