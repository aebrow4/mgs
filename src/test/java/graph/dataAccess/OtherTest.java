//package graph.dataAccess;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.neo4j.graphdb.Label.*;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.neo4j.driver.Config;
//import org.neo4j.driver.Driver;
//import org.neo4j.driver.GraphDatabase;
//import org.neo4j.driver.Result;
//import org.neo4j.driver.Session;
//import org.neo4j.driver.Value;
//import org.neo4j.driver.Values;
//import org.neo4j.graphdb.Node;
//import org.neo4j.graphdb.Transaction;
//import org.neo4j.harness.ServerControls;
//import org.neo4j.harness.TestServerBuilders;
//
//import graph.models.ArtistOgm;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class OtherTest {
//  // end::test-harness-setup[]
//	private static final Config driverConfig = Config.builder().withoutEncryption().build();
//
//	// tag::test-harness-setup[]
//	private ServerControls embeddedDatabaseServer; // <2>
//
//	@BeforeAll // <3>
//	void initializeNeo4j() {
//
//		this.embeddedDatabaseServer = TestServerBuilders.newInProcessBuilder()
//			//.withProcedure(ArtistOgm.class) // <4>
//			//.withFunction(GetGeometry.class)
//			//.withFixture("" // <5>
//			//	+ " CREATE (:Place {name: 'Malmö', longitude: 12.995098, latitude: 55.611730})"
//			//	+ " CREATE (:Place {name: 'Aachen', longitude: 6.083736, latitude: 50.776381})"
//			//	+ " CREATE (:Place {name: 'Lost place'})"
//			//	// end::test-harness-setup[]
//			//	+ " CREATE (:Thing {name: 'A box', geometry: ["
//			//	+ "   point({x:  0, y:  0}), "
//			//	+ "   point({x: 10, y:  0}), "
//			//	+ "   point({x: 10, y: 10}), "
//			//	+ "   point({x:  0, y: 10}), "
//			//	+ "   point({x:  0, y:  0})] }"
//			//	+ " )"
//			//	// tag::test-harness-setup[]
//			//)
//			// end::test-harness-setup[]
//			.withFixture(graphDatabaseService -> {
//				try (Transaction transaction = graphDatabaseService.beginTx()) {
//					Node node = graphDatabaseService.createNode(label("Thing"));
//					node.setProperty("name", "An empty thing");
//					transaction.success();
//				}
//				return null;
//			})
//			// tag::test-harness-setup[]
//			.newServer(); // <6>
//	}
//	// end::test-harness-setup[]
//
//	// tag::test-harness-usage1[]
//	@Test
//	void shouldConvertLocations() {
//		try (Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI(), driverConfig);
//				 Session session = driver.session()) {
//
//			 Result result = session.run(""
//					+ " MATCH (n:Place) WITH collect(n) AS nodes"
//					+ " CALL examples.convertLegacyLocation(nodes) YIELD node"
//					+ " RETURN node ORDER BY node.name");
//
//			 assertThat(result.keys()).isEqualTo("asdf");
//			//assertThat(result.stream())
//			//		.hasSize(2)
//			//		.extracting(r -> {
//			//			Value node = r.get("node");
//			//			return node.get("location").asPoint();
//			//		})
//			//		.containsExactly(
//			//				Values.point(4326, 6.083736, 50.776381).asPoint(),
//			//				Values.point(4326, 12.995098, 55.611730).asPoint()
//			//		);
//		}
//	}
//	// end::test-harness-usage1[]
//
//	@AfterAll
//	void shutdownNeo4j() { // <7>
//		this.embeddedDatabaseServer.close();
//	}
//}

