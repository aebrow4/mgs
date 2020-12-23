package graph.models;

import java.util.Optional;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


@NodeEntity
public class ArtistOgm {
  @Id @GeneratedValue
  public Long id;

  @Property("discogsId")
  private Long discogsId;

  @Property("name")
  private String name;

  @Property("dataQuality")
  private String dataQuality;

  @Property("realName")
  private String realName;

  public Long getDiscogsId() {
    return this.discogsId;
  }


  public ArtistOgm(
      Long discogsId,
      String name,
      String dataQuality,
      Optional<String> realName) {
    this.discogsId = discogsId;
    this.name = name;
    this.dataQuality = dataQuality;
    if (realName.isPresent()) {
      this.realName = realName.get();
    }
  }
}
