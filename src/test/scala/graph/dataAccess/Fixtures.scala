package graph.dataAccess

import graph.models.nodes.Artist

object Fixtures {
  val DefaultArtistId = 123L
  val DefaultArtist = Artist(
    discogsId = DefaultArtistId,
    name = "Bill",
    dataQuality = "legit",
    realName = Some("william")
  )

  val DefaultArtistId2 = 200L
  val DefaultArtist2 = Artist(
    discogsId = DefaultArtistId2,
    name = "julio",
    dataQuality = "good",
    realName = Some("jim")
  )
}
