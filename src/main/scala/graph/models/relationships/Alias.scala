package graph.models.relationships

/**
  * Artists may have aliases, which are also represented
  * by artist records. The directed relationship from artist to alias
  * is HasAlias, and from alias to artist is AliasOf
  */
sealed trait Alias extends BidirectionalRelationship {
  override val owningRelationship: Relationship = HasAlias
  override val ownedByRelationship: Relationship = AliasOf
}

case object HasAlias extends Relationship {
  val relationshipCode = "Ha"
  val label = "HasAlias"
}
case object AliasOf extends Relationship {
  val relationshipCode = "Oa"
  val label = "AliasOf"
}

case class HasAlias(aliasDiscogsId: Long)
case class AliasOf(aliasOfDiscogsId: Long)
