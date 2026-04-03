package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.Entity.createEntity
import it.unibo.tosab.model.entities.{Faction, Role}
import org.junit.*
import org.junit.Assert.*

class GridTest:
  import it.unibo.tosab.model.grid.Grid

  val grid = Grid()

  @Test def testUnitPosition(): Unit =
    val wizard = createEntity("wizard", Faction.AI, Role.Mage)
    grid.setCell(wizard, (3, 3))
    assertEquals(Some(wizard), grid.getEntity((3, 3)))

  @Test def testInvalidCell(): Unit =
    val soldier = createEntity("soldier", Faction.AI, Role.Soldier)
    grid.setCell(soldier, (8, 8))
    val occupiedCells = grid.getOccupiedCells
    assertFalse(occupiedCells.contains((8, 8)))

  @Test def testGetOccupiedCells(): Unit =
    val soldier = createEntity("soldier", Faction.AI, Role.Soldier)
    grid.setCell(soldier, (1, 1))
    val occupiedCells = grid.getOccupiedCells
    assertTrue(occupiedCells.contains((1, 1)))
    assertEquals(1, occupiedCells.size)

  @Test def testGetAdjacentCellsEvenRow(): Unit =
    val soldier = createEntity("soldier", Faction.AI, Role.Soldier)
    grid.setCell(soldier, (2, 1))
    val availableCellsSoldier = grid.getAdjacentAvailableCells(soldier)
    val expected = Set((1, 0), (1, 1), (2, 0), (2, 2), (3, 0), (3, 1))
    assertEquals(expected, availableCellsSoldier)

  @Test def testGetAdjacentCellsOddRow(): Unit =
    val soldier = createEntity("soldier", Faction.AI, Role.Soldier)
    grid.setCell(soldier, (3, 2))
    val availableCellsSoldier3 = grid.getAdjacentAvailableCells(soldier)
    val expected3 = Set((2, 2), (2, 3), (3, 1), (3, 3), (4, 2), (4, 3))
    assertEquals(expected3, availableCellsSoldier3)

  @Test def testGetAdjacentCellsWithOccupied(): Unit =
    val soldier = createEntity("soldier", Faction.AI, Role.Soldier)
    val wizard = createEntity("wizard", Faction.AI, Role.Mage)
    grid.setCell(soldier, (2, 1))
    grid.setCell(wizard, (2, 2))
    val availableCellsSoldierWithNeighbour = grid.getAdjacentAvailableCells(soldier)
    val expectedWithOccupied = Set((1, 0), (1, 1), (2, 0), (3, 0), (3, 1))
    assertEquals(expectedWithOccupied, availableCellsSoldierWithNeighbour)
