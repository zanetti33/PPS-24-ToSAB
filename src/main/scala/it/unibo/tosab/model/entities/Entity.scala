package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.Role.{Archer, Mage, Soldier}
import it.unibo.tosab.model.entities.Stats.*

enum Role:
  case Archer, Soldier, Mage

trait Entity:
  def id: String
  def role: Role
  def stats: Stats

  def takeDamage(amount: Int): Entity

case class Character(id: String, role: Role, stats: Stats) extends Entity:

  def takeDamage(amount: Int): Entity =
    val newHp = Math.max(0, stats.currentHp - amount)
    val newStats = stats.copy(currentHp = newHp)
    this.copy(stats = newStats)

object Entity:

  def createArcher(id: String): Entity =
    Character(id, Archer, baseArcherStats)

  def createSoldier(id: String): Entity =
    Character(id, Soldier, stats = baseSoldierStats)

  def createMage(id: String): Entity =
    Character(id, Mage, stats = baseMageStats)