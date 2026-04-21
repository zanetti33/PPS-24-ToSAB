package it.unibo.tosab.model.entities

import org.junit.Test
import org.junit.Assert.*
import it.unibo.tosab.model.entities.Damageable.given

class EntityTest:

  val soldier: Character = Entity.soldier(EntityId("s1"), Faction.Player)

  @Test def testEntityExist(): Unit =
    assertNotNull(soldier)

  @Test def testEntityIsACharacter(): Unit =
    val archer: Entity = Entity.archer(EntityId("a1"), Faction.Player)
    assertTrue(archer match
      case _: Character => true
      case _: Obstacle  => false
    )

  @Test def testWallIsAnObstacle(): Unit =
    val wall: Entity = Entity.wall(EntityId("w1"))
    assertTrue(wall match
      case _: Obstacle  => true
      case _: Character => false
    )

  @Test def testSoldierHasRightHP(): Unit =
    assertEquals(50, soldier.stats.currentHp)

  @Test def testSoldierHasRightAttackType(): Unit =
    assertEquals(AttackType.Melee, soldier.stats.attackType)

  @Test def testMageIsAnEnemy(): Unit =
    val mage: Character = Entity.mage(EntityId("m1"), Faction.AI)
    assertTrue(mage.isAnEnemy)

  @Test def testSoldierIsAnAlly(): Unit =
    assertFalse(soldier.isAnEnemy)

  @Test def testTakeDamageReducesHp(): Unit =
    val damage = DamageInstance(20, DamageType.Physical)
    val damagedSoldier = soldier.takeDamage(damage)
    assertEquals(45, damagedSoldier.stats.currentHp) // 50 - (20 - 15) = 45

  @Test def testTakeDamageDoesNotReduceHpBelowZero(): Unit =
    val highDamage = DamageInstance(1000, DamageType.Physical)
    val damagedSoldier = soldier.takeDamage(highDamage)
    assertEquals(0, damagedSoldier.stats.currentHp)

  @Test def testObstacleProperties(): Unit =
    val bush = Entity.bush(EntityId("b1"))
    assertTrue(bush.isPassable)
    assertFalse(bush.blocksVision)
    val wall = Entity.wall(EntityId("w1"))
    assertFalse(wall.isPassable)
    assertTrue(wall.blocksVision)

  @Test def testWallIsNotDamageable(): Unit =
    val newWall: Obstacle = Entity.wall(EntityId("w2"))
    val damage = DamageInstance(20, DamageType.Physical)
    val damagedWall = newWall.takeDamage(damage)
    assertEquals(newWall, damagedWall)
