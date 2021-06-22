package graph.queries

import graph.CypherUtils.{cypherIdentifier, joinCypher}
import graph.queries.ArtistQueries.{
  ArtistCode,
  nodeIdentifier,
  getArtistIdentifier
}
import neotypes.implicits.all.neotypesSyntaxCypherStringInterpolator

object AliasQueries extends RelationshipQueries {
  val AliasCode = "Al"

  /**
    * Construct an identifier for a HasAlias or IsAlias relationship, e.g.
    * AlAr100Ar200
    */
  def getAliasIdentifier(fromId: Long, toId: Long): String = {
    val suffix = s"$ArtistCode$fromId$ArtistCode$toId"
    nodeIdentifier(AliasCode, suffix)
  }

  /**
    * I.e.
    * (Ar100: Artist)-[AlAr100Ar200:IsAlias]->(Ar200: Artist)
    */
  def relationshipQueryFragment(fromId: Long, destId: Long) = {
    val owningIdentifier = getArtistIdentifier(fromId)
    val ownedIdentifier = getArtistIdentifier(destId)
    c"" + s"($owningIdentifier: Artist)-[r$destId$fromId:HasAlias]->($ownedIdentifier: Artist)"
  }

  //def relationShipQueryFragment(fromId: Long, destIds: Set[Long]) = {
  //  joinCypher(destIds.map { destId =>
  //    c"" + s"(${getArtistIdentifier(destId)})-[r$destId$fromId:IsAlias]->(a$fromId)"
  //  }.toSeq)
  //}
}
