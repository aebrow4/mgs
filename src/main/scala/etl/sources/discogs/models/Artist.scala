package etl.sources.discogs.models

import graph.models.ArtistOgm

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

  def toOgm: ArtistOgm = {
    println(dataQuality)
    new ArtistOgm(
      discogsId.toInt,
      name,
      dataQuality,
      optToJava(realName)
    )
  }
}
