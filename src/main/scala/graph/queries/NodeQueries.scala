package graph.queries

import neotypes.DeferredQueryBuilder
import neotypes.implicits.all.neotypesSyntaxCypherStringInterpolator

trait NodeQueries {

  /**
    * Construct variable names for use in Cypher expressions.
    * Examples:
    *   Ar100 (identifier for artist node with discogs id 100)
    *   AlAr100Ar200 (identifier for alias relationship from artist 100 to 200)
    *
    * @param entityCode The two character abbreviation for the entity, e.g. Ar for Artist
    *                   or Al for Alias (there is just one entitycode for a bidirectional
    *                   relationship
    * @param suffix The identifier suffix, either a discogs ID (node) or two ids
    *               (relationship)
    */
  def nodeIdentifier(
      entityCode: String,
      suffix: String
  ): String = {
    s"$entityCode$suffix"
  }

  /**
    * Construct a parameterized property criteria Cypher fragment, e.g.
    * {discogsId: $p1} [querybuilder] or
    * {discogsId: 100} [final cypher]
    *
    * For use in various Cypher clauses, e.g.
    *  MATCH (a: Artist) {discogsId: 100} RETURN a
    *                    ^^^^^^^^^^^^^^^^
    * @param propertyName the property name, e.g. discogsId or name
    * @param propertyValue the value to match on
    * @tparam T the type of propertyValue, a primitive
    */
  def propertyCriteria[T](
      propertyName: String,
      propertyValue: T
  ): DeferredQueryBuilder = {
    c"" + propertyName + c": {$propertyValue}"
  }

  /**
    * Construct a cypher fragment matching a node of identifier and label,
    * and optionally a property criteria fragment.
    * E.g. (Ar100: Artist) or (Ar100: Artist {discogsId: 100})
    */
  def labeledIdentifier(
      identifier: String,
      label: String,
      propertyCriteria: Option[DeferredQueryBuilder] = None
  ): DeferredQueryBuilder = {
    val baseFragment = c"" + s"$identifier: $label"
    propertyCriteria match {
      case Some(pc) => c"" + s"($baseFragment " + c"$pc)"
      case None     => baseFragment
    }
  }
}
