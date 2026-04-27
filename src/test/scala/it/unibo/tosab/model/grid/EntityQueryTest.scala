package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class EntityQueryTest:

  val soldier: Character = Entity.soldier(EntityId("soldier"), Faction.Player)
  val wizard: Character = Entity.mage(EntityId("wizard"), Faction.AI)
  val grid: Grid = GridFactory.createHexagonal(8)

  @Test def testGetPosition(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(3, 3))
    assertEquals(Some(Coordinate(3, 3)), updatedGrid.getPosition(wizard))

  @Test def testGetPositionById(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(3, 3))
    assertEquals(Some(Coordinate(3, 3)), updatedGrid.getPosition(wizard.id))

  @Test def testAllEntities(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val entities = updatedGrid.allEntities
    assertEquals(2, entities.size)
    assertTrue(entities.exists(_.id == soldier.id))
    assertTrue(entities.exists(_.id == wizard.id))

  @Test def testAllEntitiesWithPositions(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val entitiesWithPos = updatedGrid.allEntitiesWithPositions
    assertEquals(2, entitiesWithPos.size)
    assertTrue(entitiesWithPos.exists((e, _) => e.id == soldier.id))
    assertTrue(entitiesWithPos.exists((e, _) => e.id == wizard.id))

  @Test def testFilterEntities(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val aiEntities = updatedGrid.filterEntities(e => e.asInstanceOf[Character].isAnEnemy)
    assertFalse(aiEntities.exists(_.id == soldier.id))
    assertTrue(aiEntities.exists(_.id == wizard.id))

  @Test def testCollectEntities(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(4, 1))
      .setCell(wizard, Coordinate(2, 2))
    val mages = updatedGrid.collectEntities { case c: Character if c.role == Role.Mage => c }
    assertEquals(1, mages.size)
    assertTrue(mages.exists(_.id == wizard.id))
