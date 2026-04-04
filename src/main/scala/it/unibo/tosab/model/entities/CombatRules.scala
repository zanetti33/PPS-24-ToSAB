package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.*

object CombatRules:
  def calculateDamage(damageInstance: DamageInstance, targetStats: Stats): Int =
    damageInstance.damageType match
      case DamageType.Physical => Math.max(0, damageInstance.amount - targetStats.physicalDefense)
      case DamageType.Magical  => Math.max(0, damageInstance.amount - targetStats.magicalDefence)

  extension (damage: DamageInstance)
    def calculatedAgainst(targetStats: Stats): Int = calculateDamage(damage, targetStats)
