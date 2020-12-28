package utils

import java.util
import collection.JavaConverters._

object Converters {

  /**
    * Convert a scala set of scala longs to a java set of java longs
    * @param set
    * @return
    */
  def scalaToJavaSet(
      set: scala.collection.Set[Long]
  ): util.Set[java.lang.Long] = {
    var output = Set[java.lang.Long]()
    for (i <- set) yield {
      output += i
    }
    output.asJava
  }
}
