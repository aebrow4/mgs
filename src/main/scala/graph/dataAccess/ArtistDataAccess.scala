package graph.dataAccess

import graph.models.Artist
import utils.Converters.scalaToJavaSet
import collection.JavaConverters._

/**
  * Scala wrapper for graph.dataAccess.ArtistNeo4jApi.
  * The java *Neo4jApi classes should only be referenced in the corresponding
  * scala *DataAccess classes, to prevent java types from leaking into scala code.
  *
  * Converts scala arguments to java, and java return values to scala
  * Ex:
  * java.util.Iterator -> Iterator (and vice versa), or
  * java.util.Set -> Set (and vice versa)
  *
  * One exception is that the neo4j-ogm model instances are always represented
  * in java. Methods in the java class that accept or return such values
  * (as opposed to collections of such values) are not overridden here and
  * may be called directly from the superclass.
  */
class ArtistDataAccess(getSession: GetSession)
    extends ArtistNeo4jApi(getSession) {
  def getByDiscogsId(
      discogsIds: scala.collection.Set[Long]
  ): Iterator[Artist] = {
    super
      .getByDiscogsId(
        scalaToJavaSet(discogsIds)
      )
      .asScala
  }

  def create(iterable: Iterable[Artist]): Unit = {
    super.create(iterable.asJava)
  }

  def update(iterable: Iterable[Artist]): Unit = {
    super.update(iterable.asJava)
  }
}
