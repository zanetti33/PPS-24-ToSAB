package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class GridTest:
  import it.unibo.tosab.model.grid.Grid

  val grid = Grid()
  val soldier: Character = Entity.soldier(EntityId("soldier"), Faction.Player)
  val wizard: Character = Entity.mage(EntityId("wizard"), Faction.AI)
  val wall: Obstacle = Entity.wall(EntityId("wall"))

  @Test def testUnitPosition(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(3, 3))
    assertEquals(Some(wizard), updatedGrid.getEntity(Coordinate(3, 3)))

  @Test def testInvalidCell(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(8, 8))
    val occupiedCells = updatedGrid.getOccupiedCells
    assertFalse(occupiedCells.contains(Coordinate(8, 8)))

  @Test def testGetOccupiedCells(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(1, 1))
    val occupiedCells = updatedGrid.getOccupiedCells
    assertTrue(occupiedCells.contains(Coordinate(1, 1)))
    assertEquals(1, occupiedCells.size)

  @Test def testGetAdjacentCellsEvenRow(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(2, 1))
    val availableCellsWizard = updatedGrid.getAdjacentAvailableCells(wizard)
    val expected = Set(
      Coordinate(1, 0),
      Coordinate(1, 1),
      Coordinate(2, 0),
      Coordinate(2, 2),
      Coordinate(3, 0),
      Coordinate(3, 1)
    )
    assertEquals(expected, availableCellsWizard)

  @Test def testGetAdjacentCellsOddRow(): Unit =
    val updatedGrid = grid.setCell(wizard, Coordinate(3, 2))
    val availableCellsWizard = updatedGrid.getAdjacentAvailableCells(wizard)
    val expected = Set(
      Coordinate(2, 2),
      Coordinate(2, 3),
      Coordinate(3, 1),
      Coordinate(3, 3),
      Coordinate(4, 2),
      Coordinate(4, 3)
    )
    assertEquals(expected, availableCellsWizard)

  @Test def testGetAdjacentCellsWithNeighbour(): Unit =
    val soldier = Entity.soldier(EntityId("soldier"), Faction.AI)
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
    val updatedGrid = grid.setCell(soldier, Coordinate(2, 1)).setCell(wizard, Coordinate(2, 2))
    val aiEntities = updatedGrid.filterEntities(e => e.asInstanceOf[Character].isAnEnemy)
    assertFalse(aiEntities.exists(_.id == soldier.id))
    assertTrue(aiEntities.exists(_.id == wizard.id))

  @Test def testEnemyVisibility(): Unit =
    val bush = Entity.bush(EntityId("bush"))
    val updatedGrid = grid
      .setCell(soldier, Coordinate(4, 2))
      .setCell(wizard, Coordinate(0, 0))
      .setCell(bush, Coordinate(1, 0))
    assertTrue(updatedGrid.isLineOfSightClear(Coordinate(0, 0), Coordinate(4, 2)))
    val updatedGrid2 = updatedGrid
      .setCell(wall, Coordinate(2, 1))
    assertFalse(updatedGrid2.isLineOfSightClear(Coordinate(0, 0), Coordinate(4, 2)))

  @Test def testIsInSameRow(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(0, 2))
      .setCell(wizard, Coordinate(0, 0))
    assertTrue(updatedGrid.isLineOfSightClear(Coordinate(0, 0), Coordinate(0, 2)))
