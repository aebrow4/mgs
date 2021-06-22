package graph.queries

import graph.CypherUtils.{cypherIdentifier, joinCypher}
import graph.models.nodes.Artist
import neotypes.DeferredQueryBuilder

object ArtistQueries extends NodeQueries {
  val ArtistCode = "Ar"

  def getArtistIdentifier(discogsId: Long): String = {
    nodeIdentifier(ArtistCode, discogsId.toString)
  }

  /**
    * Query fragment to match a single artist on discogs id. e.g.
    * (AR100: Artist {discogsId: 100})
    */
  def matchArtistsQueryFragment(discogsId: Long): DeferredQueryBuilder = {
    matchArtistsQueryFragment(Set(discogsId))
  }

  /**
    * Query fragment to match multiple artists on discogs id. e.g.
    * (AR100: Artist {discogsId: 100}), (AR200: Artist {discogsId: 200})
    */
  def matchArtistsQueryFragment(discogsIds: Set[Long]): DeferredQueryBuilder = {
    joinCypher(discogsIds.map { discogsId =>
      val identifier = getArtistIdentifier(discogsId)
      labeledIdentifier(identifier, Artist.label)
    }.toSeq)
  }
}
