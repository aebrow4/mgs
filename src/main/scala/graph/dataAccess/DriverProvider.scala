package graph.dataAccess

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.neo4j.driver.AuthTokens
import neotypes.GraphDatabase

object DriverProvider {
  def withDriver = driver

  private val user = "neo4j"
  private val password = sys.env("NEO4J_PASSWORD")
  private val ipAddr = sys.env("NEO4J_IP")
  private val uri = s"bolt://$ipAddr:7687"
  private val driver = GraphDatabase.driver[Future](uri, AuthTokens.basic(user, password))
}
