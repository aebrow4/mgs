package graph.dataAccess;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import graph.models.Artist;

public class ArtistDataAccess extends DataAccess<Artist> {
  @Override
  Class<Artist> getEntityType() {
    return Artist.class;
  }

  @Override
  public Optional<Artist> getByDiscogsId(Long discogsId) { return super.getByDiscogsId(discogsId); }

  @Override
  public Iterator<Artist> getByDiscogsId(Set<Long> discogsIds) { return super.getByDiscogsId(discogsIds); }

  /**
   * Establish the HasAlias and IsAliasOf relationships between an artist and
   * an alias.
   * @param alias
   * @param artist
   */
  public void createBidirectionalAliasEdges(Artist alias, Artist artist) {
    this.setAliasOf(alias, artist);
    this.addAlias(artist, alias);
  }
  /**
   * Establish directed HasAlias edge from artist to alias.
   * The edge is added to any existing alias edges/
   * @param artist.aliases
   * @param artist
   * @param alias
   * @return
   */
  private void addAlias(Artist artist, Artist alias) {
    Set<Artist> aliases = artist.getAliases();
    aliases.add(alias);
    artist.setAliases(aliases);
  }

  /**
   * Establish the IsAliasOf edge from alias to aliasOf
   * @param alias
   * @param aliasOf
   */
  private void setAliasOf(Artist alias, Artist aliasOf) {
    alias.setAliasOf(aliasOf);
  }

  /**
   * Establish the HasMember and IsMemberOf relationships between a group member and
   * a group.
   * @param member
   * @param group
   */
  public void createBidirectionalMemberEdges(Artist member, Artist group) {
    this.setMemberOf(member, group);
    this.addMember(group, member);
  }

  /**
   * Establish the HasMember edge from group to member
   * @param group.members
   * @param group
   * @param member
   */
  private void addMember(Artist group, Artist member) {
    Set<Artist> members = group.getMembers();
    members.add(member);
    group.setMembers(members);
  }

  /**
   * Establish the IsMemberOf edge from member to group
   * @param member
   * @param group
   */
  private void setMemberOf(Artist member, Artist group) {
    member.setMemberOf(group);
  }

  public ArtistDataAccess(GetSession getSession) {
    super.getSession = getSession;
  }
}
