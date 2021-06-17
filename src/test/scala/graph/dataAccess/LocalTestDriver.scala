package graph.dataAccess

import scala.concurrent.Future
import neotypes.implicits.syntax.all._

class LocalTestDriver
    extends Driver(
      "neo4j",
      sys.env("NEO4J_LOCAL_TEST_PASSWORD"),
      "localhost"
    ) {
  def clearDb(): Future[Unit] =
    "MATCH (n) DETACH DELETE n".query[Unit].single(driver)
}
