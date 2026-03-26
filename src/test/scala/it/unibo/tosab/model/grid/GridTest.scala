package it.unibo.tosab.model.grid
import org.junit.*
import org.junit.Assert.*

class GridTest:
  import it.unibo.tosab.model.grid.Grid

  val grid = Grid()

  @Test def testUnitPosition(): Unit =
    grid.setCell("soldier", (3, 3))
    assertEquals("soldier", grid.getEntity((3, 3)))

  @Test def testInvalidCell(): Unit =
    grid.setCell("soldier", (8, 8))
    val occupiedCells = grid.getOccupiedCells
    assertFalse(occupiedCells.contains((8, 8)))

  @Test def testGetOccupiedCells(): Unit =
    grid.setCell("soldier", (1, 1))
    val occupiedCells = grid.getOccupiedCells
    assertTrue(occupiedCells.contains((1, 1)))
    assertEquals(1, occupiedCells.size)

  @Test def testGetAdjacentCellsEvenRow(): Unit =
    grid.setCell("soldier", (2, 1))
    val availableCellsSoldier1 = grid.getAdjacentAvailableCells("soldier")
    val expected1 = Set((1, 0), (1, 1), (2, 0), (2, 2), (3, 0), (3, 1))
    assertEquals(expected1, availableCellsSoldier1)

  @Test def testGetAdjacentCellsOddRow(): Unit =
    grid.setCell("soldier", (3, 2))
    val availableCellsSoldier3 = grid.getAdjacentAvailableCells("soldier")
    val expected3 = Set((2, 2), (2, 3), (3, 1), (3, 3), (4, 2), (4, 3))
    assertEquals(expected3, availableCellsSoldier3)

  @Test def testGetAdjacentCellsWithOccupied(): Unit =
    grid.setCell("soldier", (2, 1))
    grid.setCell("enemy", (2, 2))
    val availableCellsSoldierWithNeighbour = grid.getAdjacentAvailableCells("soldier")
    val expectedWithOccupied = Set((1, 0), (1, 1), (2, 0), (3, 0), (3, 1))
    assertEquals(expectedWithOccupied, availableCellsSoldierWithNeighbour)