package graph.models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;


@NodeEntity
public class Artist extends BaseNode {

  @Relationship(type="HasAlias") private Set<Artist> aliases;
  @Relationship(type="IsAliasOf") private Artist aliasOf;

  @Relationship(type="HasMember") private Set<Artist> members;
  @Relationship(type="IsMemberOf") private Artist memberOf;

  @Property("name")
  private String name;

  // Optional types aren't supported, else this would be one
  @Property("realName")
  private String realName;

  public String getName() { return this.name; }
  public Optional<String> getRealName() {
    return this.realName.isEmpty() ? Optional.empty() : Optional.of(this.realName);
  }
  public Set<Artist> getAliases() {
    return this.aliases == null ? new HashSet<>() : this.aliases;
  }
  public Optional<Artist> getAliasOf() {
    return this.aliasOf == null ? Optional.empty() : Optional.of(this.aliasOf);
  }
  public Set<Artist> getMembers() {
    // We should just initialize members as an emtpy set when not present
    // rather than needing to do this check
    return this.members == null ? new HashSet<>() : this.members;
  }
  public Optional<Artist> getMemberOf() {
    return this.memberOf == null ? Optional.empty() : Optional.of(this.memberOf);
  }
  public Artist setRealName(String realName) {
    this.realName = realName;
    return this;
  }

  public Artist setAliases(Set<Artist> aliases) {
    this.aliases = aliases;
    return this;
  }
  public Artist setAliasOf(Artist artist) {
    this.aliasOf = artist;
    return this;
  }

  public void setMembers(Set<Artist> members) {
    this.members = members;
  }

  public void setMemberOf(Artist group) {
    this.memberOf = group;
  }


  public static Class<Artist> getEntityType() {
    return Artist.class;
  }

  public Artist(
      Long discogsId,
      String name,
      String dataQuality,
      Optional<String> realName,
      Set<Artist> aliases,
      Optional<Artist> aliasOf,
      Set<Artist> members,
      Optional<Artist> memberOf) {
    this.discogsId = discogsId;
    this.name = name;
    this.dataQuality = dataQuality;
    this.realName = realName.orElse("");
    this.aliases = aliases;
    this.aliasOf = aliasOf.orElse(null);
    this.members = members;
    this.memberOf = memberOf.orElse(null);
  }

  // Unfortunately Neo4j needs a nullary constructor to use for reflection
  public Artist(){
  }
}
