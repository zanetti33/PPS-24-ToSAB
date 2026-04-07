package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.CombatRules.calculatedAgainst
import it.unibo.tosab.model.entities.Faction.{AI, Player}
import it.unibo.tosab.model.entities.Role.{Archer, Mage, Soldier}
import it.unibo.tosab.model.entities.Stats.*

enum Faction:
  case Player, AI

enum Role:
  case Archer, Soldier, Mage

sealed trait Entity:
  def id: String

case class Character(id: String, faction: Faction, role: Role, stats: Stats) extends Entity:
  def isAnEnemy: Boolean = faction match
    case Faction.Player => false
    case Faction.AI     => true

  def takeDamage(damageInstance: DamageInstance): Character =
    val damageTaken = damageInstance.calculatedAgainst(stats)
    val newHp = Math.max(0, stats.currentHp - damageTaken)
    val newStats = stats.copy(currentHp = newHp)
    this.copy(stats = newStats)

case class Obstacle(
                     id: String,
                     hp: Option[Int],      // None = non-damageable, Some(x) = damageable, x = hp
                     isPassable: Boolean,  // true = jumpable/walkable
                     blocksVision: Boolean // true = blocks ranged attacks
                   ) extends Entity

//companion object -> factory method
object Entity:

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
