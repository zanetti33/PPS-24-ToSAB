package it.unibo.tosab.model.entities

import org.junit.Test
import org.junit.Assert.*
import it.unibo.tosab.model.entities.Entity.*
import it.unibo.tosab.model.entities.Faction.{AI, Player}
import it.unibo.tosab.model.entities.Role.*

class EntityTest:

  val soldier: Entity = createEntity("s1", Player, Soldier)
  val archer: Entity = createEntity("a1", Player, Archer)
  val mage: Entity = createEntity("m1", AI, Mage)

  @Test def testEntityExist(): Unit =
    assertNotNull(soldier)

  @Test def testEntityIsASoldier(): Unit =
    assertEquals(Soldier, soldier.role)

  @Test def testEntityIsAnArcher(): Unit =
    assertEquals(Archer, archer.role)

  @Test def testEntityIsAMage(): Unit =
    assertEquals(Mage, mage.role)

  @Test def testSoldierHasRightHP(): Unit =
    assertEquals(50, soldier.stats.currentHp)

  @Test def testSoldierHasRightAttackType(): Unit =
    assertEquals(AttackType.Melee, soldier.stats.attackType)

  @Test def testMageIsAnEnemy(): Unit =
    assertTrue(mage.isAnEnemy)

  @Test def testSoldierIsAnAlly(): Unit =
    assertFalse(soldier.isAnEnemy)

  @Test def testEntityTakesDamage(): Unit =
    val damagedSoldier = soldier.takeDamage(20)
    assertEquals(30, damagedSoldier.stats.currentHp) // 50 - 20 = 30
