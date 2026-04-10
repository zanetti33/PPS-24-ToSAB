package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class GridTest:
  import it.unibo.tosab.model.grid.Grid

  val grid = Grid()

  @Test def testUnitPosition(): Unit =
    val wizard = Entity.mage("wizard", Faction.AI)
    val updatedGrid = grid.setCell(wizard, (3, 3))
    assertEquals(Some(wizard), updatedGrid.getEntity((3, 3)))

  @Test def testInvalidCell(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    val updatedGrid = grid.setCell(soldier, (8, 8))
    val occupiedCells = updatedGrid.getOccupiedCells
    assertFalse(occupiedCells.contains((8, 8)))

  @Test def testGetOccupiedCells(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    val updatedGrid = grid.setCell(soldier, (1, 1))
    val occupiedCells = updatedGrid.getOccupiedCells
    assertTrue(occupiedCells.contains((1, 1)))
    assertEquals(1, occupiedCells.size)

  @Test def testGetAdjacentCellsEvenRow(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    val updatedGrid = grid.setCell(soldier, (2, 1))
    val availableCellsSoldier = updatedGrid.getAdjacentAvailableCells(soldier)
    val expected = Set((1, 0), (1, 1), (2, 0), (2, 2), (3, 0), (3, 1))
    assertEquals(expected, availableCellsSoldier)

  @Test def testGetAdjacentCellsOddRow(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    val updatedGrid = grid.setCell(soldier, (3, 2))
    val availableCellsSoldier3 = updatedGrid.getAdjacentAvailableCells(soldier)
    val expected3 = Set((2, 2), (2, 3), (3, 1), (3, 3), (4, 2), (4, 3))
    assertEquals(expected3, availableCellsSoldier3)

  @Test def testGetAdjacentCellsWithNeighbour(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    val wizard = Entity.mage("wizard", Faction.AI)
    val updatedGrid = grid.setCell(soldier, (2, 1)).setCell(wizard, (2, 2))
    val availableCellsSoldierWithNeighbour = updatedGrid.getAdjacentAvailableCells(soldier)
    val expectedWithoutOccupied = Set((1, 0), (3, 1), (1, 1), (2, 0), (3, 0))
    assertEquals(expectedWithoutOccupied, availableCellsSoldierWithNeighbour)

  @Test def testDistance(): Unit =
    val distanceMax = grid.getDistance((0, 0), (7, 7))
    val distance = grid.getDistance((2, 1), (4, 1))
    assertEquals(11, distanceMax)
    assertEquals(2, distance)

  @Test def testPlaceObstacle(): Unit =
    val wall = Entity.wall("wall")
    val updatedGrid = grid.setCell(wall, (4, 4))
    assertEquals(Some(wall), updatedGrid.getEntity((4, 4)))

  @Test def testPopulateGrid(): Unit =
    val updatedGrid = grid.placeObstacles()
    val occupiedCells = updatedGrid.getOccupiedCells
    println(occupiedCells)
    assertTrue(occupiedCells.nonEmpty)
    for cell <- occupiedCells do
      val entity = updatedGrid.getEntity(cell)
      assertTrue(entity.exists(_.isInstanceOf[Obstacle]))

  @Test def testFilterEntities(): Unit =
    val soldier = Entity.soldier("soldier", Faction.Player)
    val wizard = Entity.mage("wizard", Faction.AI)
    val updatedGrid = grid.setCell(soldier, (2, 1)).setCell(wizard, (2, 2))
    val aiEntities = updatedGrid.filterEntities(e => e.asInstanceOf[Character].isAnEnemy)
    assertFalse(aiEntities.exists(_.id == soldier.id))
    assertTrue(aiEntities.exists(_.id == wizard.id))
