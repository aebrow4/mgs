package graph

import graph.models.Artist

object Utils {
  // why cant i do [T <: BaseNode] instead of hardcoding Artist?
  /**
    * Convert an iterator of DB records into a map of (id -> record)
    * @param results The results iterator
    * @return Map of ids to records
    */
  def buildResultMap(results: Iterator[Artist]): Map[Long, Artist] = {
    val resultMap = Map[Long, Artist]()
    while (results.hasNext) {
      val currArtist = results.next()
      resultMap.+((currArtist.discogsId, currArtist))
    }
    resultMap
  }
}
