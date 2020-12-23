package graph.dataAccess;

import org.junit.Before;
import org.junit.Rule;
import org.neo4j.harness.junit.rule.Neo4jRule;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

abstract class Neo4jTest {
  @Rule
  public Neo4jRule neoServer = new Neo4jRule();

  protected Session testSession;

  @Before
  public void setUp() throws Exception {
    Configuration configuration = new Configuration.Builder()
        .uri(neoServer.boltURI().toString())
        .build();

    SessionFactory sessionFactory = new SessionFactory(configuration, "graph.models");
    testSession = sessionFactory.openSession();
    testSession.purgeDatabase();
  }
}
