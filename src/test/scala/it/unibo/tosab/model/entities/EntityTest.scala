package it.unibo.tosab.model.entities

import org.junit.Test
import org.junit.Assert.*
import it.unibo.tosab.model.entities.Entity.*

class EntityTest:

  val soldier: Entity = createSoldier("s1")
  val archer: Entity = createArcher("a1")
  val mage: Entity = createMage("m1")

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

  @Test def entityTakesDamage(): Unit =
    val damagedSoldier = soldier.takeDamage(20)
    assertEquals(30, damagedSoldier.stats.currentHp) // 50 - 20 = 30