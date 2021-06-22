package graph.dataAccess

import graph.models.nodes.Artist

object Fixtures {
  val DefaultArtistId = 100L
  val DefaultArtistName = "Nicole Moudaber"
  val DefaultArtistDataQuality = "Super!"
  val DefaultArtistRealName = Some("nicole")
  val DefaultArtist = Artist(
    discogsId = DefaultArtistId,
    name = DefaultArtistName,
    dataQuality = DefaultArtistDataQuality,
    realName = DefaultArtistRealName
  )
  val Nicole = DefaultArtist

  val DefaultArtistId2 = 200L
  val DefaultArtist2Name = "Hair lady"
  val DefaultArtist2DataQuality = "ok"
  val DefaultArtist2RealName = Some("nicole")
  val DefaultArtist2 = Artist(
    discogsId = DefaultArtistId2,
    name = DefaultArtist2Name,
    dataQuality = DefaultArtist2DataQuality,
    realName = DefaultArtist2RealName
  )
  val HairLady = DefaultArtist2
}
