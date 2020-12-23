package graph.dataAccess;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.ogm.drivers.bolt.driver.BoltDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import graph.models.ArtistOgm;

public class DataAccessOgm {
  static public SessionFactory sessionFactory(){
    String user = "neo4j";
    String password = "E{6t&7Gm(W2TL{3";
    BoltDriver driver = new BoltDriver(GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic(user, password)));
    return new SessionFactory(
        driver,
        false,
        "graph.models");


  }
  public static void createArtist(ArtistOgm artist) {
    Session session = sessionFactory().openSession();
    session.save(artist);
  }
}
