package graph.dataAccess;

import java.util.Optional;

import graph.models.Label;

public class LabelDataAccess extends DataAccess<Label> {
  @Override
  Class<Label> getEntityType() {
    return Label.class;
  }

  @Override
  Optional<Label> getByDiscogsId(Long discogsId) { return super.getByDiscogsId(discogsId); }

  public LabelDataAccess(GetSession getSession) {
    super.getSession = getSession;
  }
}
