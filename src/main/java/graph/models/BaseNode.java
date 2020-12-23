package graph.models;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
abstract class BaseNode {
  @Id
  @GeneratedValue
  public Long id;

  @Property("discogsId")
  public Long discogsId;

  @Property("dataQuality")
  public String dataQuality;

  public Long getDiscogsId() { return this.discogsId; }
  public String getDataQuality() { return this.dataQuality; }
}
