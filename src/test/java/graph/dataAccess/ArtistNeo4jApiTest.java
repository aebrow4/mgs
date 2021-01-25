package graph.dataAccess;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import graph.models.Artist;

import static graph.dataAccess.Factories.ArtistFactory;

public class ArtistNeo4jApiTest extends Neo4jTest {
    private final ArtistNeo4jApi neo4jApi = new ArtistNeo4jApi(
        () -> super.testSession
    );

    @Test
    public void testGetByDiscogsId() {
        Artist artist = ArtistFactory();
        Long id = artist.getDiscogsId();

        neo4jApi.create(artist);

        Optional<Artist> result = neo4jApi.getByDiscogsId(id);
        assertThat(result.isPresent());
        assertThat(result.get().getDiscogsId()).isEqualTo(id);
    }

    @Test
    public void testGetByDiscogsidNotFound() {
        Artist artist = ArtistFactory();
        Long id = artist.getDiscogsId();

        neo4jApi.create(artist);

        Optional<Artist> emptyResult = neo4jApi.getByDiscogsId(id + 1);
        assertThat(emptyResult.isEmpty());
    }

    @Test
    public void testCreateArtist() {
        String realName = "David Koresh";
        Artist artist = ArtistFactory()
            .setRealName(realName);
        Long id = artist.getDiscogsId();
        String name = artist.getName();
        String dataQuality = artist.getDataQuality();

        neo4jApi.create(artist);

        Optional<Artist> result = neo4jApi.getByDiscogsId(id);
        assertThat(result.isPresent());

        Artist res = result.get();
        assertThat(res.getDiscogsId()).isEqualTo(id);
        assertThat(res.getName()).isEqualTo(name);
        assertThat(res.getDataQuality()).isEqualTo(dataQuality);
        assertThat(res.getRealName()).contains(realName);
    }

    @Test
    public void testCreateArtistWithAliases() {
        Artist alias1 = ArtistFactory();
        Artist alias2 = ArtistFactory();
        Artist artist = ArtistFactory();
        neo4jApi.createBidirectionalAliasEdges(alias1, artist);
        neo4jApi.createBidirectionalAliasEdges(alias2, artist);
        Long alias1Id = alias1.getDiscogsId();
        Long alias2Id = alias2.getDiscogsId();
        Long artistId = artist.getDiscogsId();

        neo4jApi.create(artist);

        Artist alias1Result = neo4jApi.getByDiscogsId(alias1Id).get();
        Artist alias2Result = neo4jApi.getByDiscogsId(alias2Id).get();
        Artist artistResult = neo4jApi.getByDiscogsId(artistId).get();

        assertThat(alias1Result.getAliasOf().get().getDiscogsId()).isEqualTo(artistId);
        assertThat(alias2Result.getAliasOf().get().getDiscogsId()).isEqualTo(artistId);
        Artist[] aliases = artistResult.getAliases().toArray(new Artist[0]);
        List<Long> aliasesDiscogsIds = Arrays.stream(aliases).map(a -> a.getDiscogsId()).collect(Collectors.toList());
        assertThat(aliasesDiscogsIds).contains(alias1Id);
        assertThat(aliasesDiscogsIds).contains(alias2Id);
    }

    @Test
    public void testCreateArtistWithMembers() {
        Artist member1 = ArtistFactory();
        Artist member2 = ArtistFactory();
        Artist group = ArtistFactory();
        neo4jApi.createBidirectionalMemberEdges(member1, group);
        neo4jApi.createBidirectionalMemberEdges(member2, group);
        Long member1Id = member1.getDiscogsId();
        Long member2Id = member2.getDiscogsId();
        Long groupId = group.getDiscogsId();

        neo4jApi.create(group);

        Artist member1Result = neo4jApi.getByDiscogsId(member1Id).get();
        Artist member2Result = neo4jApi.getByDiscogsId(member2Id).get();
        Artist groupResult = neo4jApi.getByDiscogsId(groupId).get();

        assertThat(member1Result.getMemberOf().get().getDiscogsId()).isEqualTo(groupId);
        assertThat(member2Result.getMemberOf().get().getDiscogsId()).isEqualTo(groupId);
        Artist[] members = groupResult.getMembers().toArray(new Artist[0]);
        List<Long> membersDiscogsIds = Arrays.stream(members).map(m -> m.getDiscogsId()).collect(Collectors.toList());
        assertThat(membersDiscogsIds).contains(member1Id);
        assertThat(membersDiscogsIds).contains(member2Id);
    }
}

