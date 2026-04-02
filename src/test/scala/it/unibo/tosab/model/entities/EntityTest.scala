package it.unibo.tosab.model.entities

import org.junit.Test
import org.junit.Assert.*
import it.unibo.tosab.model.entities.Entity.*
import it.unibo.tosab.model.entities.Faction.{AI, Player}

class EntityTest:

  val soldier: Entity = createSoldier("s1", Player)
  val archer: Entity = createArcher("a1", Player)
  val mage: Entity = createMage("m1", AI)

  @Test def entityExistTest(): Unit =
    assertNotNull(soldier)

  @Test def entityIsASoldier(): Unit =
    assertEquals(Role.Soldier, soldier.role)

  @Test def entityIsAnArcher(): Unit =
    assertEquals(Role.Archer, archer.role)

  @Test def entityIsAMage(): Unit =
    assertEquals(Role.Mage, mage.role)

  @Test def soldierHasRightHP(): Unit =
    assertEquals(50, soldier.stats.currentHp)

  @Test def soldierHasRightAttackType(): Unit =
    assertEquals("Melee", soldier.stats.attackType)

  @Test def mageIsAnEnemy(): Unit =
    assertTrue(mage.isAnEnemy)

  @Test def soldierIsAnAlly(): Unit =
    assertFalse(soldier.isAnEnemy)

  @Test def entityTakesDamage(): Unit =
    val damagedSoldier = soldier.takeDamage(20)
    assertEquals(30, damagedSoldier.stats.currentHp) // 50 - 20 = 30