package etl.sources.discogs.models

import graph.models

case class Artist(
    discogsId: String,
    name: String,
    dataQuality: String,
    realName: Option[String],
    aliases: Seq[String],
    members: Seq[String]
) {

  // TODO move this somewhere else
  def optToJava[T](option: Option[T]): java.util.Optional[T] = {
    if (option.isDefined) {
      java.util.Optional.of(option.get)
    } else {
      java.util.Optional.empty()
    }
  }

  def toOgm: models.Artist = {
    new models.Artist(
      discogsId.toLong,
      name,
      dataQuality,
      optToJava(realName),
      new java.util.HashSet(),
      optToJava(None),
      new java.util.HashSet(),
      optToJava(None)
    )
  }
}
