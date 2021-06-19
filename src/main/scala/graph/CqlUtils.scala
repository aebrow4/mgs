package graph

import neotypes.DeferredQueryBuilder
import neotypes.implicits.all.neotypesSyntaxCypherStringInterpolator

object CqlUtils {

  /**
    * Like mkString, it joins cql fragments together with a hardcoded
    * ", " delimiter between elements.
    */
  def mkCql(cql: Seq[DeferredQueryBuilder]): DeferredQueryBuilder = {
    val length = cql.length
    if (cql.length == 1) {
      cql.head
    } else {
      var joined = c""
      for (i <- 0 until length) {
        joined += cql(i)
        if (i < length - 1) joined += ", "
      }
      joined
    }
  }
}
