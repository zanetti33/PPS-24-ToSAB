package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.ObstacleType.{Bush, Tree, Wall, Rock}

enum Faction:
  case Player, AI

enum Role:
  case Archer, Soldier, Mage

enum ObstacleType:
  case Bush, Wall, Tree, Rock

sealed trait Entity:
  def id: String

case class Character(id: String, faction: Faction, role: Role, stats: Stats) extends Entity

case class Obstacle(
    id: String,
    obstacleType: ObstacleType,
    hp: Option[Int], // None = non-damageable, Some(x) = damageable with x = hp
    isPassable: Boolean, // true = jumpable/walkable
    blocksVision: Boolean // true = blocks ranged attacks
) extends Entity

object Entity:

  extension (c: Character) def isAnEnemy: Boolean = c.faction == Faction.AI

  def archer(id: String, faction: Faction): Character =
    Character(id, faction, Role.Archer, Stats.baseArcherStats)

  def soldier(id: String, faction: Faction): Character =
    Character(id, faction, Role.Soldier, Stats.baseSoldierStats)

  def mage(id: String, faction: Faction): Character =
    Character(id, faction, Role.Mage, Stats.baseMageStats)

  def bush(id: String): Obstacle =
    Obstacle(id, Bush, hp = Some(50), isPassable = true, blocksVision = false)

  def wall(id: String): Obstacle =
    Obstacle(id, Wall, hp = None, isPassable = false, blocksVision = true)

  def tree(id: String): Obstacle =
    Obstacle(id, Tree, hp = Some(120), isPassable = false, blocksVision = true)

  def rock(id: String): Obstacle =
    Obstacle(id, Rock, hp = None, isPassable = true, blocksVision = true)
