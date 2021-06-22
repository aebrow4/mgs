package graph.models.relationships

trait Relationship {

  /**
    * Abbreviated name of the relationship, e.g.
    * Ar for Artist, Ha for HasAlias, or Oa for AliasOf
    */
  val relationshipCode: String

  // Name of the neo4j label, e.g. HasAlias
  val label: String
}
