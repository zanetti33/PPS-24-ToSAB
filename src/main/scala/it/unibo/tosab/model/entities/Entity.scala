package it.unibo.tosab.model.entities

enum Faction:
  case Player, AI

enum Role:
  case Archer, Soldier, Mage

sealed trait Entity:
  def id: String

case class Character(id: String, faction: Faction, role: Role, stats: Stats) extends Entity

case class Obstacle(
                     id: String,
                     hp: Option[Int],      // None = non-damageable, Some(x) = damageable, x = hp
                     isPassable: Boolean,  // true = jumpable/walkable
                     blocksVision: Boolean // true = blocks ranged attacks
                   ) extends Entity

object Entity:

  extension (c: Character)
    def isAnEnemy: Boolean = c.faction == Faction.AI

  def archer(id: String, faction: Faction): Character =
    Character(id, faction, Role.Archer, Stats.baseArcherStats)

  def soldier(id: String, faction: Faction): Character =
    Character(id, faction, Role.Soldier, Stats.baseSoldierStats)

  def mage(id: String, faction: Faction): Character =
    Character(id, faction, Role.Mage, Stats.baseMageStats)

  def bush(id: String): Obstacle =
    Obstacle(id, hp = Some(50), true, false)

  def wall(id: String): Obstacle =
    Obstacle(id, hp = Some(200), false, true)
