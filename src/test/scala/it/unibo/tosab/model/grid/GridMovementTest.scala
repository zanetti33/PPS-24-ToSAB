package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class GridMovementTest:

  val grid: Grid = GridFactory.createHexagonal(8)
  val soldier: Character = Entity.soldier(EntityId("soldier"), Faction.Player)
  val wizard: Character = Entity.mage(EntityId("wizard"), Faction.AI)

  @Test def testMoveEntity(): Unit =
    var updatedGrid = grid.setCell(wizard, Coordinate(1, 3))
    updatedGrid = updatedGrid.moveEntity(wizard.id, Coordinate(2, 3))
    assertEquals(Some(wizard), updatedGrid.getEntity(Coordinate(2, 3)))
    assertEquals(None, updatedGrid.getEntity(Coordinate(1, 3)))

  @Test def testMoveEntityOutOfBounds(): Unit =
    var updatedGrid = grid.setCell(wizard, Coordinate(1, 3))
    val gridBefore = updatedGrid
    updatedGrid = updatedGrid.moveEntity(wizard.id, Coordinate(10, 10))
    assertEquals(gridBefore, updatedGrid)

  @Test def testMoveEntityToOccupiedCell(): Unit =
    var updatedGrid = grid
      .setCell(soldier, Coordinate(4, 2))
      .setCell(wizard, Coordinate(1, 3))
    val gridBefore = updatedGrid
    updatedGrid = updatedGrid.moveEntity(wizard.id, Coordinate(4, 2))
    assertEquals(gridBefore, updatedGrid)

  @Test def testGetAdjacentAvailableCellsEvenRow(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(2, 1))
    val availableCells = updatedGrid.getAdjacentAvailableCells(wizard)
    val expected = Set(
      Coordinate(1, 0),
      Coordinate(1, 1),
      Coordinate(2, 0),
      Coordinate(2, 2),
      Coordinate(3, 0),
      Coordinate(3, 1)
    )
    assertEquals(expected, availableCells)

  @Test def testGetAdjacentAvailableCellsOddRow(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(3, 2))
    val availableCells = updatedGrid.getAdjacentAvailableCells(wizard)
    val expected = Set(
      Coordinate(2, 2),
      Coordinate(2, 3),
      Coordinate(3, 1),
      Coordinate(3, 3),
      Coordinate(4, 2),
      Coordinate(4, 3)
    )
    assertEquals(expected, availableCells)

  @Test def testGetAdjacentCellsWithNeighbour(): Unit =
    val updatedGrid = grid
      .setCell(wizard, Coordinate(3, 1))
      .setCell(soldier, Coordinate(4, 2))
    val availableCells = updatedGrid.getAdjacentAvailableCells(wizard)
    val expected =
      Set(Coordinate(3, 0), Coordinate(2, 1), Coordinate(2, 2), Coordinate(3, 2), Coordinate(4, 1))
    assertEquals(expected, availableCells)
