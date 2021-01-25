package graph.models;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public abstract class BaseNode {
  @Id
  @Property("discogsId")
  public Long discogsId;

  @Property("dataQuality")
  public String dataQuality;

  public Long getDiscogsId() { return this.discogsId; }
  public String getDataQuality() { return this.dataQuality; }
}
