package etl.sources.discogs.models

import graph.models
import graph.models.Artist

case class Artist(
    discogsId: String,
    name: String,
    dataQuality: String,
    realName: Option[String],
    aliases: Option[Seq[String]],
    members: Option[Seq[String]]
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
      optToJava(None), // for now
      optToJava(None),
      optToJava(None),
      optToJava(None)
    )
  }
}
