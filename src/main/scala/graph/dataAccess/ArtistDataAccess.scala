package graph.dataAccess

import scala.concurrent.Future
import neotypes.Driver
import neotypes.generic.auto._
import neotypes.implicits.syntax.all._
import graph.models.nodes.Artist
import graph.CqlUtils.mkCql

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
    val artistsCql = mkCql(ids.map { id =>
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
    val artistCql = artists
      .map { artist =>
        val identifier = artist.name
        c"(" + identifier + c": Artist { $artist })"
      }

    val cql = mkCql(artistCql.toSeq)

    val query = c"CREATE $cql".query[Unit]
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
  ): Future[Unit] = ???
}
