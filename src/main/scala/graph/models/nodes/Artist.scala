package graph.models.nodes

case class Artist(
    discogsId: Long,
    name: String,
    dataQuality: String,
    realName: Option[String] = None
)
