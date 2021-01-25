package graph.utils

import graph.models.BaseNode

object DbUtils {

  /**
    * Convert an iterator of DB records into a map of (id -> record)
    * @param results The results iterator
    * @return Map of ids to records
    */
  def buildResultMap[T <: BaseNode](
      results: Iterator[T]
  ): Map[Long, T] = {
    val resultMap = Map[Long, T]()
    while (results.hasNext) {
      val currArtist = results.next()
      resultMap.+((currArtist.discogsId, currArtist))
    }
    resultMap
  }
}
