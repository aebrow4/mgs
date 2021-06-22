package graph

import neotypes.DeferredQueryBuilder
import neotypes.implicits.all.neotypesSyntaxCypherStringInterpolator

object CypherUtils {

  /**
    * Like mkString, it joins Cypher fragments together with a hardcoded
    * ", " delimiter between elements.
    */
  def joinCypher(fragments: Seq[DeferredQueryBuilder]): DeferredQueryBuilder = {
    val length = fragments.length
    if (fragments.length == 1) {
      fragments.head
    } else {
      var joined = c""
      for (i <- 0 until length) {
        joined += fragments(i)
        if (i < length - 1) joined += ", "
      }
      joined
    }
  }

}
