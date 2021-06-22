package graph.models.relationships

/**
  * Base trait for all bidirectional relationships, i.e. a
  * directed relationship from A to B and it's conjugate relationship
  * from B to A.
  */
trait BidirectionalRelationship {

  /**
    * The "possessing" relationship, e.g. HasAlias, HasMember, or HasLabel.
    */
  val owningRelationship: Relationship

  /**
    * The inverse relationship, e.g. AliasOf, MemberOf, LabelOf
    */
  val ownedByRelationship: Relationship
}
