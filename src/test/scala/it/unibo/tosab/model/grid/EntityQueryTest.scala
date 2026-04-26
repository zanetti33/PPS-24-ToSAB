package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class EntityQueryTest:

  val soldier: Character = Entity.soldier(EntityId("soldier"), Faction.Player)
  val wizard: Character = Entity.mage(EntityId("wizard"), Faction.AI)

  @Test def testGetPosition(): Unit =
    val grid = GridFactory.createHexagonal(8).setCell(wizard, Coordinate(3, 3))
    assertEquals(Some(Coordinate(3, 3)), grid.getPosition(wizard))

  @Test def testGetPositionById(): Unit =
    val grid = GridFactory.createHexagonal(8).setCell(wizard, Coordinate(3, 3))
    assertEquals(Some(Coordinate(3, 3)), grid.getPosition(wizard.id))

  @Test def testAllEntities(): Unit =
    val grid = GridFactory
      .createHexagonal(8)
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val entities = grid.allEntities
    assertEquals(2, entities.size)
    assertTrue(entities.exists(_.id == soldier.id))
    assertTrue(entities.exists(_.id == wizard.id))

  @Test def testAllEntitiesWithPositions(): Unit =
    val grid = GridFactory
      .createHexagonal(8)
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val entitiesWithPos = grid.allEntitiesWithPositions
    assertEquals(2, entitiesWithPos.size)
    assertTrue(entitiesWithPos.exists((e, _) => e.id == soldier.id))
    assertTrue(entitiesWithPos.exists((e, _) => e.id == wizard.id))

  @Test def testFilterEntities(): Unit =
    val grid = GridFactory
      .createHexagonal(8)
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val aiEntities = grid.filterEntities(e => e.asInstanceOf[Character].isAnEnemy)
    assertFalse(aiEntities.exists(_.id == soldier.id))
    assertTrue(aiEntities.exists(_.id == wizard.id))

  @Test def testCollectEntities(): Unit =
    val grid = GridFactory
      .createHexagonal(8)
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val mages = grid.collectEntities { case c: Character if c.role == Role.Mage => c }
    assertEquals(1, mages.size)
    assertTrue(mages.exists(_.id == wizard.id))
