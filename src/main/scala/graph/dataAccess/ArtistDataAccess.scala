package graph.dataAccess

import graph.models.nodes.Artist

import scala.concurrent.Future
import neotypes.Driver
import neotypes.generic.auto._
import neotypes.implicits.syntax.all._

class ArtistDataAccess(driver: Driver[Future]) {
  def getByDiscogsId(id: Long): Future[Option[Artist]] = {
    val query =
      c"MATCH (a: Artist) WHERE a.discogsId=$id RETURN a LIMIT 1"
        .query[Option[Artist]]
    println(s"query=$query")
    query.single(driver)
  }

  def getByDiscogsIds(ids: Set[Long]): Future[List[Artist]] = ???

  /**
    * Create a single artist, without any relationships
    * @param artist
    * @return
    */
  def create(artist: Artist): Future[Artist] = {
    val query = c"CREATE (a: Artist { $artist }) RETURN a".query[Artist]
    query.single(driver)
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
