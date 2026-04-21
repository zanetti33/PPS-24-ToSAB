package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.ObstacleType.{Bush, Tree, Wall, Rock}

enum Faction:
  case Player, AI

enum Role:
  case Archer, Soldier, Mage

enum ObstacleType:
  case Bush, Wall, Tree, Rock

type EntityId = String

sealed trait Entity:
  def id: EntityId

case class Character(id: EntityId, faction: Faction, role: Role, stats: Stats) extends Entity

case class Obstacle(
    id: EntityId,
    obstacleType: ObstacleType,
    hp: Option[Int], // None = non-damageable, Some(x) = damageable with x = hp
    isPassable: Boolean, // true = jumpable/walkable
    blocksVision: Boolean // true = blocks ranged attacks
) extends Entity

object Entity:

  extension (c: Character) def isAnEnemy: Boolean = c.faction == Faction.AI

  def archer(id: EntityId, faction: Faction): Character =
    Character(id, faction, Role.Archer, Stats.baseArcherStats)

  def soldier(id: EntityId, faction: Faction): Character =
    Character(id, faction, Role.Soldier, Stats.baseSoldierStats)

  def mage(id: EntityId, faction: Faction): Character =
    Character(id, faction, Role.Mage, Stats.baseMageStats)

  def bush(id: EntityId): Obstacle =
    Obstacle(id, Bush, hp = Some(50), isPassable = true, blocksVision = false)

  def wall(id: EntityId): Obstacle =
    Obstacle(id, Wall, hp = None, isPassable = false, blocksVision = true)

  def tree(id: EntityId): Obstacle =
    Obstacle(id, Tree, hp = Some(120), isPassable = false, blocksVision = true)

  def rock(id: EntityId): Obstacle =
    Obstacle(id, Rock, hp = None, isPassable = true, blocksVision = true)
