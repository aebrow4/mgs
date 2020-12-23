package graph.dataAccess;

import java.util.Collection;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.rule.Neo4jRule;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import static org.assertj.core.api.Assertions.assertThat;
import graph.models.ArtistOgm;

public class ArtistDataAccessTest {

    @Rule
    public Neo4jRule neoServer = new Neo4jRule();
    private Session session;

    @Before
    public void setUp() throws Exception {

        Configuration configuration = new Configuration.Builder()
            .uri(neoServer.boltURI().toString())
            //.uri(new File("target/graph.db").toURI().toString()) // For Embedded
            .build();

        SessionFactory sessionFactory = new SessionFactory(configuration, ArtistOgm.class.getPackage().getName());
        session = sessionFactory.openSession();
        session.purgeDatabase();
    }

    @Test
    public void sampleTest() {
        String realName = null;
        ArtistOgm artist = new ArtistOgm(-1L, "testartist", "lit", Optional.ofNullable(realName));
        session.save(artist);

        Collection<ArtistOgm> allArtists = session.loadAll(ArtistOgm.class);
        assertThat(allArtists).hasSize(1);

        assertThat(allArtists.iterator().next().getDiscogsId()).isEqualTo(-1L);
    }
}

