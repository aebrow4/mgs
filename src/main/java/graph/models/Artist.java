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
  public Optional<Set<Artist>> getAliases() {
    return this.aliases.isEmpty() ? Optional.empty() : Optional.of(this.aliases);
  }
  public Optional<Artist> getAliasOf() {
    return this.aliasOf == null ? Optional.empty() : Optional.of(this.aliasOf);
  }
  public Optional<Set<Artist>> getMembers() {
    return this.members.isEmpty() ? Optional.empty() : Optional.of(this.members);
  }
  public Optional<Artist> getMemberOf() {
    return this.memberOf == null ? Optional.empty() : Optional.of(this.memberOf);
  }
  public Artist setRealName(String realName) {
    this.realName = realName;
    return this;
  }

  public Artist addAlias(Artist alias) {
    this.aliases.add(alias);
    return this;
  }
  public Artist setAliasOf(Artist artist) {
    this.aliasOf = artist;
    return this;
  }

  /**
   * Establish the HasAlias and IsAliasOf relationships between an artist and
   * an alias.
   * @param alias
   * @param artist
   */
  public static void createBidirectionalAliasEdges(Artist alias, Artist artist) {
    alias.setAliasOf(artist);
    artist.addAlias(alias);
  }

  private Artist addMember(Artist member) {
    this.members.add(member);
    return this;
  }
  private Artist setGroup(Artist memberOf) {
    this.memberOf = memberOf;
    return this;
  }

  /**
   * Establish the HasMember and IsMemberOf relationships between a group member and
   * a group.
   * @param member
   * @param group
   */
  public static void createBidirectionalMemberEdges(Artist member, Artist group) {
    member.setGroup(group);
    group.addMember(member);
  }

  public static Class<Artist> getEntityType() {
    return Artist.class;
  }

  public Artist(
      Long discogsId,
      String name,
      String dataQuality,
      Optional<String> realName,
      Optional<Set<Artist>> aliases,
      Optional<Artist> aliasOf,
      Optional<Set<Artist>> members,
      Optional<Artist> memberOf) {
    this.discogsId = discogsId;
    this.name = name;
    this.dataQuality = dataQuality;
    this.realName = realName.orElse("");
    this.aliases = aliases.orElse(new HashSet<>());
    this.aliasOf = aliasOf.orElse(null);
    this.members = aliases.orElse(new HashSet<>());
    this.memberOf = aliasOf.orElse(null);
  }

  // Unfortunately Neo4j needs a nullary constructor to use for reflection
  public Artist(){
  }
}
