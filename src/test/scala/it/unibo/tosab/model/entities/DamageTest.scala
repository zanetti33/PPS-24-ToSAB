package it.unibo.tosab.model.entities

import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.entities.Damageable.given
import it.unibo.tosab.model.grid.Grid
import org.junit.Test

class DamageTest:

  private val targetId = "target"
  private val mageId = "mage"
  private val soldierId = "soldier"
  private val attackerId = "attacker"
  private val enemyId = "enemy"
  private val allyId = "ally"
  private val targetHpAfterAttack = 40
  private val targetPhysicalDefenseAfterAttack = 5
  private val attackerPhysicalAttack = 30
  private val inRangeAttackerPosition = (4, 3)
  private val inRangeEnemyPosition = (3, 3)
  private val allyPosition = (5, 5)
  private val farAttackerPosition = (7, 7)
  private val farEnemyPosition = (0, 0)
  private val lowDamageAmount = 10
  private val noDamageExpected = 0
  private val expectedResolvedTargetHp = 15

  val targetHp: Int = 100
  val targetPhysicalDefense: Int = 20
  val targetMagicalDefense: Int = 10
  val targetStats: Stats = Stats(
    currentHp = targetHp,
    physicalDefense = targetPhysicalDefense,
    magicalDefence = targetMagicalDefense
  )
  val damageAmount: Int = 50
  val targetEntity: Character = Character(targetId, Faction.Player, Role.Soldier, targetStats)

  private def createSoldier(id: String, faction: Faction): Character = Entity.soldier(id, faction)

  @Test def testPhysicalDamage(): Unit =
    val expectedDamage = damageAmount - targetPhysicalDefense
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Physical)
    val calculatedDamage = CombatRules.calculatedAgainst(damage)(targetStats)
    assert(calculatedDamage == expectedDamage) // 50 - 20 = 30

  @Test def testMagicalDamage(): Unit =
    val expectedDamage = damageAmount - targetMagicalDefense
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Magical)
    val calculatedDamage = CombatRules.calculatedAgainst(damage)(targetStats)
    assert(calculatedDamage == expectedDamage) // 50 - 10 = 40

  @Test def testZeroDamage(): Unit =
    val damageAmount = lowDamageAmount
    val expectedDamage = noDamageExpected
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Physical)
    val calculatedDamage = CombatRules.calculatedAgainst(damage)(targetStats)
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
    val damageAmount = lowDamageAmount
    val expectedDamage = noDamageExpected
    val damage = DamageInstance(amount = damageAmount, damageType = DamageType.Physical)
    val damagedEntity = targetEntity.takeDamage(damage)
    // 10 - 20 = -10 -> max(0, -10) = 0
    assert(targetEntity.stats.currentHp - damagedEntity.stats.currentHp == expectedDamage)

  @Test def testCreateDamageUsesMainAttackStat(): Unit =
    val mage = Entity.mage(mageId, Faction.Player)
    val soldier = Entity.soldier(soldierId, Faction.Player)

    val mageAttackProfile = CombatRules.createDamage(mage)
    val soldierAttackProfile = CombatRules.createDamage(soldier)

    assert(mageAttackProfile.hasMagicalDamage && !mageAttackProfile.hasPhysicalDamage)
    assert(soldierAttackProfile.hasPhysicalDamage && !soldierAttackProfile.hasMagicalDamage)

  @Test def testCreateDamageWithBothPhysicalAndMagical(): Unit =
    val hybridCharacter = Character(
      "hybrid",
      Faction.Player,
      Role.Soldier,
      Stats.baseSoldierStats.copy(physicalAttack = 20, magicalAttack = 15)
    )

    val attackProfile = CombatRules.createDamage(hybridCharacter)

    assert(attackProfile.isMixedDamage, "Attack should contain both physical and magical damage")
    assert(attackProfile.totalDamageInstances == 2, "Should have exactly 2 damage instances")
    assert(attackProfile.hasPhysicalDamage, "Should have physical damage")
    assert(attackProfile.hasMagicalDamage, "Should have magical damage")

  @Test def testCanAttackDependsOnRangeAndFaction(): Unit =
    val attacker = createSoldier(attackerId, Faction.Player)
    val enemy = createSoldier(enemyId, Faction.AI)
    val ally = createSoldier(allyId, Faction.Player)
    val inRangeState = GameState(
      GamePhase.Combat,
      Grid()
        .setCell(attacker, inRangeAttackerPosition)
        .setCell(enemy, inRangeEnemyPosition)
        .setCell(ally, allyPosition)
    )
    val outOfRangeState = GameState(
      GamePhase.Combat,
      Grid().setCell(attacker, farAttackerPosition).setCell(enemy, farEnemyPosition)
    )

    assert(CombatRules.canAttack(inRangeState, attacker, enemy))
    assert(!CombatRules.canAttack(inRangeState, attacker, ally))
    assert(!CombatRules.canAttack(outOfRangeState, attacker, enemy))

  @Test def testResolveAttackReturnsDamagedTarget(): Unit =
    val attacker = Character(
      attackerId,
      Faction.Player,
      Role.Soldier,
      Stats(physicalAttack = attackerPhysicalAttack)
    )
    val target = Character(
      targetId,
      Faction.AI,
      Role.Soldier,
      Stats(currentHp = targetHpAfterAttack, physicalDefense = targetPhysicalDefenseAfterAttack)
    )

    val updatedTarget = CombatRules.resolveAttack(attacker, target)

    assert(updatedTarget.stats.currentHp == expectedResolvedTargetHp)
