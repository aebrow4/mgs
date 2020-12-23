package graph.dataAccess;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.ogm.drivers.bolt.driver.BoltDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {
  private static String user = "neo4j";
  private static String password = "E{6t&7Gm(W2TL{3";
  private final static SessionFactory sessionFactory = new SessionFactory(
      new BoltDriver(GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic(user, password))),
  false, "graph.models");
  private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

  public static Neo4jSessionFactory getInstance() {
    return factory;
  }

  // prevent external instantiation
  private Neo4jSessionFactory() {
  }

  public Session getNeo4jSession() {
    return sessionFactory.openSession();
  }
}
