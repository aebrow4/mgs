package graph.queries

import graph.models.relationships.BidirectionalRelationship
import neotypes.DeferredQueryBuilder
import neotypes.implicits.all.neotypesSyntaxCypherStringInterpolator

trait RelationshipQueries {

  /**
    * Generate the pair of identifiers for a relationship from A (owning node)
    * to B (owned by node)
    * and the conjugate relationship from B to A.
    * E.g. ([HaAr100Ar200:HasAlias], [OaAr200Ar100:AliasOf])
    * Semantically, artist 100 has alias artist 200; artist 200 alias of artist 100
    * @param owningIdentifier Identifier for the owning node, e.g. Ar100
    * @param ownedByIdentifier Identifier for the owned node, e.g. Ar200
    * @param relationship Bidirectional relationship between A and B
    */
  def relationshipIdentifier(
      owningIdentifier: String,
      ownedByIdentifier: String,
      relationship: BidirectionalRelationship
  ): (String, String) = {
    val owningRelationship = relationship.owningRelationship
    val owningRelationshipCode = owningRelationship.relationshipCode
    val ownedByRelationship = relationship.ownedByRelationship
    val ownedByRelationshipCode = ownedByRelationship.relationshipCode

    val owningRelationshipIdentifier =
      s"$owningRelationshipCode$owningIdentifier$ownedByIdentifier"

    val ownedByRelationshipIdentifier =
      s"$ownedByRelationshipCode$ownedByIdentifier$owningIdentifier"

    (
      s"[$owningRelationshipIdentifier:${owningRelationship.label}]",
      s"[$ownedByRelationshipIdentifier:${ownedByRelationship.label}]"
    )
  }
  def bidirectionalRelationshipQueryFrag(
      owningNode: DeferredQueryBuilder,
      ownedNode: DeferredQueryBuilder,
      relationship: BidirectionalRelationship
  ): DeferredQueryBuilder = {
    val owningRelationship = relationship.owningRelationship
    val ownedByRelationship = relationship.ownedByRelationship
    val owningRelationshipIdentifier = s"${owningRelationship.relationshipCode}"

    // extract the node ids from the passed in queries?

  }
}
