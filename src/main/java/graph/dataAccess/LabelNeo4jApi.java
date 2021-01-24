package graph.dataAccess;

import java.util.Optional;

import graph.models.Label;

public class LabelNeo4jApi extends Neo4jApi<Label> {
  @Override
  Class<Label> getEntityType() {
    return Label.class;
  }

  @Override
  Optional<Label> getByDiscogsId(Long discogsId) { return super.getByDiscogsId(discogsId); }

  public LabelNeo4jApi(GetSession getSession) {
    super.getSession = getSession;
  }
}
