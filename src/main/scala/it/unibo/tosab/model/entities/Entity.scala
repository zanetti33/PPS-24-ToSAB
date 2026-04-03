package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.Faction.{AI, Player}
import it.unibo.tosab.model.entities.Role.{Archer, Mage, Soldier}
import it.unibo.tosab.model.entities.Stats.*

enum Faction:
  case Player, AI

enum Role:
  case Archer, Soldier, Mage

trait Entity:
  def id: String
  def faction: Faction
  def role: Role
  def stats: Stats

  def isAnEnemy: Boolean
  
  def takeDamage(amount: Int): Entity

case class Character(id: String, faction: Faction, role: Role, stats: Stats) extends Entity:

  def isAnEnemy: Boolean = faction match
    case Player => false
    case AI => true

  def takeDamage(amount: Int): Entity =
    val newHp = Math.max(0, stats.currentHp - amount)
    val newStats = stats.copy(currentHp = newHp)
    this.copy(stats = newStats)

object Entity:

  private val statsBasedOnRole: Map[Role, Stats] =
    Map(
      Archer -> baseArcherStats,
      Soldier -> baseSoldierStats,
      Mage -> baseMageStats
    )

  def createEntity(id: String, faction: Faction, role: Role): Entity =
    Character(id, faction, role, statsBasedOnRole(role))
