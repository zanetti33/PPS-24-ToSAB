package it.unibo.tosab.model.entities

import org.junit.Test
import org.junit.Assert.*

class EntityTest:

  val soldier: Character = Entity.soldier("s1", Faction.Player)
  val archer: Entity = Entity.archer("a1", Faction.Player)
  val wall: Entity = Entity.wall("w1")

  @Test def testEntityExist(): Unit =
    assertNotNull(soldier)

  @Test def testEntityIsACharacter(): Unit =
    assertTrue(archer match
      case _: Character => true
      case _: Obstacle  => false
    )

  @Test def testWallIsAnObstacle(): Unit =
    assertTrue(wall match
      case _: Obstacle => true
      case _: Character => false
    )

  @Test def testSoldierHasRightHP(): Unit =
    assertEquals(50, soldier.stats.currentHp)

  @Test def testSoldierHasRightAttackType(): Unit =
    assertEquals(AttackType.Melee, soldier.stats.attackType)

  @Test def testMageIsAnEnemy(): Unit =
    val mage: Character = Entity.mage("m1", Faction.AI)
    assertTrue(mage.isAnEnemy)

  @Test def testSoldierIsAnAlly(): Unit =
    assertFalse(soldier.isAnEnemy)

  @Test def testTakeDamageReducesHp(): Unit =
    val damage = DamageInstance(20, DamageType.Physical)
    val damagedSoldier = soldier.takeDamage(damage)
    assertEquals(45, damagedSoldier.stats.currentHp) // 50 - (20 - 15) = 45
