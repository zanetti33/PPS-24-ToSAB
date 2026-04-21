package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class GridTest:
  import it.unibo.tosab.model.grid.Grid

  val grid = Grid()

  @Test def testUnitPosition(): Unit =
    val wizard = Entity.mage(EntityId("wizard"), Faction.AI)
    val updatedGrid = grid.setCell(wizard, Coordinate(3, 3))
    assertEquals(Some(wizard), updatedGrid.getEntity(Coordinate(3, 3)))

  @Test def testInvalidCell(): Unit =
    val soldier = Entity.soldier(EntityId("soldier"), Faction.AI)
    val updatedGrid = grid.setCell(soldier, Coordinate(8, 8))
    val occupiedCells = updatedGrid.getOccupiedCells
    assertFalse(occupiedCells.contains(Coordinate(8, 8)))

  @Test def testGetOccupiedCells(): Unit =
    val soldier = Entity.soldier(EntityId("soldier"), Faction.AI)
    val updatedGrid = grid.setCell(soldier, Coordinate(1, 1))
    val occupiedCells = updatedGrid.getOccupiedCells
    assertTrue(occupiedCells.contains(Coordinate(1, 1)))
    assertEquals(1, occupiedCells.size)

  @Test def testGetAdjacentCellsEvenRow(): Unit =
    val soldier = Entity.soldier(EntityId("soldier"), Faction.AI)
    val updatedGrid = grid.setCell(soldier, Coordinate(2, 1))
    val availableCellsSoldier = updatedGrid.getAdjacentAvailableCells(soldier)
    val expected = Set(
      Coordinate(1, 0),
      Coordinate(1, 1),
      Coordinate(2, 0),
      Coordinate(2, 2),
      Coordinate(3, 0),
      Coordinate(3, 1)
    )
    assertEquals(expected, availableCellsSoldier)

  @Test def testGetAdjacentCellsOddRow(): Unit =
    val soldier = Entity.soldier(EntityId("soldier"), Faction.AI)
    val updatedGrid = grid.setCell(soldier, Coordinate(3, 2))
    val availableCellsSoldier3 = updatedGrid.getAdjacentAvailableCells(soldier)
    val expected3 = Set(
      Coordinate(2, 2),
      Coordinate(2, 3),
      Coordinate(3, 1),
      Coordinate(3, 3),
      Coordinate(4, 2),
      Coordinate(4, 3)
    )
    assertEquals(expected3, availableCellsSoldier3)

  @Test def testGetAdjacentCellsWithNeighbour(): Unit =
    val soldier = Entity.soldier(EntityId("soldier"), Faction.AI)
    val wizard = Entity.mage(EntityId("wizard"), Faction.AI)
    val updatedGrid = grid.setCell(soldier, Coordinate(2, 1)).setCell(wizard, Coordinate(2, 2))
    val availableCellsSoldierWithNeighbour = updatedGrid.getAdjacentAvailableCells(soldier)
    val expectedWithoutOccupied =
      Set(Coordinate(1, 0), Coordinate(3, 1), Coordinate(1, 1), Coordinate(2, 0), Coordinate(3, 0))
    assertEquals(expectedWithoutOccupied, availableCellsSoldierWithNeighbour)

  @Test def testDistance(): Unit =
    val distanceMax = grid.getDistance(Coordinate(0, 0), Coordinate(7, 7))
    val distance = grid.getDistance(Coordinate(2, 1), Coordinate(4, 1))
    assertEquals(11, distanceMax)
    assertEquals(2, distance)

  @Test def testPlaceObstacle(): Unit =
    val wall = Entity.wall(EntityId("wall"))
    val updatedGrid = grid.setCell(wall, Coordinate(4, 4))
    assertEquals(Some(wall), updatedGrid.getEntity(Coordinate(4, 4)))

  @Test def testPopulateGrid(): Unit =
    val updatedGrid = grid.placeObstacles()
    val occupiedCells = updatedGrid.getOccupiedCells
    println(occupiedCells)
    assertTrue(occupiedCells.nonEmpty)
    for cell <- occupiedCells do
      val entity = updatedGrid.getEntity(cell)
      assertTrue(entity.exists(_.isInstanceOf[Obstacle]))

  @Test def testFilterEntities(): Unit =
    val soldier = Entity.soldier(EntityId("soldier"), Faction.Player)
    val wizard = Entity.mage(EntityId("wizard"), Faction.AI)
    val updatedGrid = grid.setCell(soldier, Coordinate(2, 1)).setCell(wizard, Coordinate(2, 2))
    val aiEntities = updatedGrid.filterEntities(e => e.asInstanceOf[Character].isAnEnemy)
    assertFalse(aiEntities.exists(_.id == soldier.id))
    assertTrue(aiEntities.exists(_.id == wizard.id))
