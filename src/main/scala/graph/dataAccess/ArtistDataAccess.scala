package graph.dataAccess

import scala.concurrent.Future
import neotypes.Driver
import neotypes.generic.auto._
import neotypes.implicits.syntax.all._
import graph.models.nodes.Artist
import graph.CypherUtils.{joinCypher, propertyCriteria}
import graph.queries.ArtistQueries.{
  getArtistIdentifier,
  matchArtistsQueryFragment,
  matchArtistsQueryFragment
}

class ArtistDataAccess(driver: Driver[Future]) {

  /**
    * Get a single artist, returning a map of id to Artist,
    * or an empty map if not found.
    */
  def getByDiscogsId(id: Long): Future[Map[Long, Artist]] = {
    getByDiscogsId(Set(id))
  }

  /**
    * Get multiple artists, returning a map of id to Artist
    * for the records that are found
    */
  def getByDiscogsId(ids: Set[Long]): Future[Map[Long, Artist]] = {
    val artistsCql = joinCypher(ids.map { id =>
      c"$id"
    }.toSeq)
    val query = c"MATCH (a: Artist) WHERE [$artistsCql] RETURN a.discogsId, a"
      .query[(Long, Artist)]

    query.map(driver)
  }

  /**
    * Create a single artist, without any relationships
    */
  def create(artist: Artist): Future[Unit] = {
    create(Set(artist))
  }

  /**
    * Create multiple artists, without relationships
    * e.g.
    * CREATE
    *    (foo: Artist { discogsId: $p1, name: $p2 ...}),
    *    (bar: Artist { discogsId: $p3 ...}), ...
    */
  def create(artists: Set[Artist]): Future[Unit] = {
    val artistCypherFragments = artists
      .map { artist =>
        val id = artist.discogsId
        val identifier = s"a$id"
        c"" + s"($identifier: Artist " + c"{ $artist })"
      }

    val queryBuilder = joinCypher(artistCypherFragments.toSeq)

    val query = c"CREATE $queryBuilder".query[Unit]
    query.execute(driver)
  }

  /**
    * One or both of two updates are possible: a change to the Artist itself,
    * or a change to its relationships, i.e. aliases and members.
    *  If a relationship is being added, we assume the related node already exists.
    * @param artist The node being updated
    * @param aliases Artist nodes that are aliases of this node
    * @param members Artist nodes that are members of this node
    */
  def update(
      artist: Artist,
      aliases: Set[Artist],
      members: Set[Artist]
  ): Future[Unit] = {
    createAliasRelationship(artist, aliases)
  }

  private def createAliasRelationship(artist: Artist, aliases: Set[Artist]) = {
    val artistMatchQueryBuilder = matchArtistsQueryFragment(artist.discogsId)

    // (a100: Artist {discogsId: 100})
    val aliasMatchQueryBuilder = matchArtistsQueryFragment(
      aliases.map(_.discogsId)
    )

    // i.e. (nicoleMoudaber)-[r:HasAlias]->(hairLady)
    val hasAliasQueryBuilder = joinCypher(aliases.map { alias =>
      c"" + s"(${getArtistIdentifier(artist.discogsId)})-[r${artist.discogsId}${alias.discogsId}:HasAlias]->(a${alias.discogsId})"
    }.toSeq)

    // i.e. (hairLady)-[r:IsAlias]->(nicoleMoudaber)
    val isAliasQueryBuilder = joinCypher(aliases.map { alias =>
      c"" + s"(${getArtistIdentifier(alias.discogsId)}-[r${alias.discogsId}${artist.discogsId}:IsAlias]->(a${artist.discogsId})"
    }.toSeq)

    val queryBuilder =
      c"MATCH $artistMatchQueryBuilder, $aliasMatchQueryBuilder MERGE $hasAliasQueryBuilder MERGE $isAliasQueryBuilder"

    val query = queryBuilder.query[Unit]
    println(query)
    query.execute(driver)
  }
}
