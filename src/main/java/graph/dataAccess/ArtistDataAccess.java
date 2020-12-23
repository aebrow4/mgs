package graph.dataAccess;

import java.util.Optional;

import graph.models.Artist;

public class ArtistDataAccess extends DataAccess<Artist> {
  @Override
  Class<Artist> getEntityType() {
    return Artist.class;
  }

  @Override
  Optional<Artist> getByDiscogsId(Long discogsId) { return super.getByDiscogsId(discogsId); }

  public ArtistDataAccess(GetSession getSession) {
    super.getSession = getSession;
  }
}
