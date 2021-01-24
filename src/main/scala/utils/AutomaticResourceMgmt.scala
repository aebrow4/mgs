package utils

object AutomaticResourceMgmt {
  // Scala 2.13 has scala.util.Using
  // we are on 2.12, so here's our own implementation
  // for ensuring files are closed after use
  def using[A, B <: { def close(): Unit }](resource: B)(f: B => A): A = {
    try {
      f(resource)
    } finally {
      // TODO guard resource.close() in a try in order to not hide
      // exceptions from f(resource)
      resource.close()
    }
  }
}
