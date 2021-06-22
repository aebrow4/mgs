package graph.models.nodes

import graph.models.relationships.{AliasOf, HasAlias}

case class Artist(
    discogsId: Long,
    name: String,
    dataQuality: String,
    realName: Option[String] = None
) extends Node {
  val label: String = Artist.label

  def hydrate(
      aliases: Set[HasAlias],
      aliasOf: Option[AliasOf]
  ): HydratedArtist = {
    new HydratedArtist(
      discogsId,
      name,
      dataQuality,
      realName,
      aliases,
      aliasOf
    )
  }
}

object Artist {
  val label: String = Artist.toString
}

/**
  * An artist plus its relationships
  */
class HydratedArtist(
    override val discogsId: Long,
    override val name: String,
    override val dataQuality: String,
    override val realName: Option[String],
    aliases: Set[HasAlias] = Set(),
    aliasOf: Option[AliasOf] = None
    //members: Set[HasMember] = Set(),
    //memberOf: Option[MemberOf] = None
) extends Artist(discogsId, name, dataQuality, realName)
