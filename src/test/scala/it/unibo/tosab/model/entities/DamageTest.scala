package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.CombatRules.{calculateDamage, calculatedAgainst}
import it.unibo.tosab.model.entities.{DamageInstance, DamageType, Stats}
import org.junit.Test

class DamageTest:

  val targetHp: Int = 100
  val targetPhysicalDefense: Int = 20
  val targetMagicalDefense: Int = 10
  val targetStats: Stats = Stats(
    currentHp = targetHp,
    physicalDefense = targetPhysicalDefense,
    magicalDefence = targetMagicalDefense
  )
  val damageAmount: Int = 50
  val targetEntity: Entity = Character("target", Faction.Player, Role.Soldier, targetStats)

  @Test def testPhysicalDamage(): Unit =
    val expectedDamage = damageAmount - targetPhysicalDefense
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Physical)
    val calculatedDamage = damage.calculatedAgainst(targetStats)
    assert(calculatedDamage == expectedDamage) // 50 - 20 = 30

  @Test def testMagicalDamage(): Unit =
    val expectedDamage = damageAmount - targetMagicalDefense
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Magical)
    val calculatedDamage = damage.calculatedAgainst(targetStats)
    assert(calculatedDamage == expectedDamage) // 50 - 10 = 40

  @Test def testZeroDamage(): Unit =
    val damageAmount = 10
    val expectedDamage = 0
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Physical)
    val calculatedDamage = damage.calculatedAgainst(targetStats)
    assert(calculatedDamage == expectedDamage) // 10 - 20 = -10 -> max(0, -10) = 0

  @Test def testPhysicalDamageOnEntity(): Unit =
    val expectedDamage = damageAmount - targetPhysicalDefense
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Physical)
    val damagedEntity = targetEntity.takeDamage(damage)
    assert(
      targetEntity.stats.currentHp - damagedEntity.stats.currentHp == expectedDamage
    ) // 50 - 20 = 30

  @Test def testMagicalDamageOnEntity(): Unit =
    val expectedDamage = damageAmount - targetMagicalDefense
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Magical)
    val damagedEntity = targetEntity.takeDamage(damage)
    assert(
      targetEntity.stats.currentHp - damagedEntity.stats.currentHp == expectedDamage
    ) // 50 - 10 = 40

  @Test def testZeroDamageOnEntity(): Unit =
    val damageAmount = 10
    val expectedDamage = 0
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Physical)
    val damagedEntity = targetEntity.takeDamage(damage)
    // 10 - 20 = -10 -> max(0, -10) = 0
    assert(targetEntity.stats.currentHp - damagedEntity.stats.currentHp == expectedDamage)
