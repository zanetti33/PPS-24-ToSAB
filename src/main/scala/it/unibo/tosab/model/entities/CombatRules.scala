package it.unibo.tosab.model.entities

import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.Damageable.given
import it.unibo.tosab.model.entities.*

case class AttackProfile(damages: Seq[DamageInstance]):
  def isEmpty: Boolean = damages.isEmpty
  def totalDamageInstances: Int = damages.length
  def hasPhysicalDamage: Boolean = damages.exists(_.damageType == DamageType.Physical)
  def hasMagicalDamage: Boolean = damages.exists(_.damageType == DamageType.Magical)
  def isMixedDamage: Boolean = hasPhysicalDamage && hasMagicalDamage

object CombatRules:
  private def crop(value: Int) = Math.max(0, value)

  private def calculateDamage(damageInstance: DamageInstance, targetStats: Stats): Int =
    damageInstance.damageType match
      case DamageType.Physical => crop(damageInstance.amount - targetStats.physicalDefense)
      case DamageType.Magical => crop(damageInstance.amount - targetStats.magicalDefence)

  def createDamage(attacker: Character): AttackProfile =
    val damageList = scala.collection.mutable.ListBuffer[DamageInstance]()

    if attacker.stats.physicalAttack > 0 then
      damageList += DamageInstance(attacker.stats.physicalAttack, DamageType.Physical)

    if attacker.stats.magicalAttack > 0 then
      damageList += DamageInstance(attacker.stats.magicalAttack, DamageType.Magical)

    AttackProfile(damageList.toSeq)

  def canAttack(state: GameState, attacker: Character, target: Character): Boolean =
    (for
      attackerPosition <- state.getPositionOf(attacker.id)
      targetPosition <- state.getPositionOf(target.id)
    yield
      attacker.faction != target.faction &&
      attacker.isAlive &&
      target.isAlive &&
      state.grid.getDistance(attackerPosition, targetPosition) <= attacker.stats.attackRange
    ).getOrElse(false)

  def resolveAttack(attacker: Character, target: Character): Character =
    val attackProfile = createDamage(attacker)
    attackProfile.damages.foldLeft(target)((currentTarget, damage) =>
      currentTarget.takeDamage(damage)
    )

  extension (damage: DamageInstance)
    def calculatedAgainst(targetStats: Stats): Int = calculateDamage(damage, targetStats)

  extension (attacker: Character)
    def damageInstance: AttackProfile = createDamage(attacker)
    def canAttack(target: Character, state: GameState): Boolean = CombatRules.canAttack(state, attacker, target)
    def isAlive: Boolean =
      val defeatedHpThreshold = 0
      attacker.stats.currentHp > defeatedHpThreshold
    def isDefeated: Boolean = !attacker.isAlive

  extension (obstacle: Obstacle)
    def isDestroyed: Boolean =
      val destroyedHpThreshold = 0
      obstacle.hp.exists(_ <= destroyedHpThreshold)

