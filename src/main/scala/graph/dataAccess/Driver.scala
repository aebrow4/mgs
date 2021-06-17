package graph.dataAccess

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.neo4j.driver.AuthTokens
import neotypes.GraphDatabase

class Driver(
    user: String,
    password: String,
    ipAddr: String
) {

  private val uri = s"bolt://$ipAddr:7687"
  val driver =
    GraphDatabase.driver[Future](uri, AuthTokens.basic(user, password))
}
