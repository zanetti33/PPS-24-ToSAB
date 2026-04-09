package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class GridTest:
  import it.unibo.tosab.model.grid.Grid

  val grid = Grid()

  @Test def testUnitPosition(): Unit =
    val wizard = Entity.mage("wizard", Faction.AI)
    grid.setCell(wizard, (3, 3))
    assertEquals(Some(wizard), grid.getEntity((3, 3)))

  @Test def testInvalidCell(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    grid.setCell(soldier, (8, 8))
    val occupiedCells = grid.getOccupiedCells
    assertFalse(occupiedCells.contains((8, 8)))

  @Test def testGetOccupiedCells(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    grid.setCell(soldier, (1, 1))
    val occupiedCells = grid.getOccupiedCells
    assertTrue(occupiedCells.contains((1, 1)))
    assertEquals(1, occupiedCells.size)

  @Test def testGetAdjacentCellsEvenRow(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    grid.setCell(soldier, (2, 1))
    val availableCellsSoldier = grid.getAdjacentAvailableCells(soldier)
    val expected = Set((1, 0), (1, 1), (2, 0), (2, 2), (3, 0), (3, 1))
    assertEquals(expected, availableCellsSoldier)

  @Test def testGetAdjacentCellsOddRow(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    grid.setCell(soldier, (3, 2))
    val availableCellsSoldier3 = grid.getAdjacentAvailableCells(soldier)
    val expected3 = Set((2, 2), (2, 3), (3, 1), (3, 3), (4, 2), (4, 3))
    assertEquals(expected3, availableCellsSoldier3)

  @Test def testGetAdjacentCellsWithNeighbour(): Unit =
    val soldier = Entity.soldier("soldier", Faction.AI)
    val wizard = Entity.mage("wizard", Faction.AI)
    grid.setCell(soldier, (2, 1))
    grid.setCell(wizard, (2, 2))
    val availableCellsSoldierWithNeighbour = grid.getAdjacentAvailableCells(soldier)
    val expectedWithoutOccupied = Set((1, 0), (3, 1), (1, 1), (2, 0), (3, 0))
    assertEquals(expectedWithoutOccupied, availableCellsSoldierWithNeighbour)

  @Test def testDistance(): Unit =
    val distanceMax = grid.getDistance((0, 0), (7, 7))
    val distance = grid.getDistance((2, 1), (4, 1))
    assertEquals(11, distanceMax)
    assertEquals(2, distance)
