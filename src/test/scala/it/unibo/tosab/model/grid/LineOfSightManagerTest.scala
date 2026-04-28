package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class LineOfSightManagerTest:

  val grid: Grid = GridFactory.createHexagonal(8)
  val lineOfSightManager = HexagonalLineOfSightManager(HexagonalGrid(8))
  val soldier: Character = Entity.soldier(EntityId("soldier"), Faction.Player)
  val wizard: Character = Entity.mage(EntityId("wizard"), Faction.AI)
  val wall: Obstacle = Entity.wall(EntityId("wall"))
  val bush: Obstacle = Entity.bush(EntityId("bush"))

  @Test def testLineOfSightClear(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(4, 2))
      .setCell(wizard, Coordinate(0, 0))
      .setCell(bush, Coordinate(1, 0))

    val occupiedCells = updatedGrid.getOccupiedCells
    assertTrue(
      lineOfSightManager.isLineOfSightClear(
        Coordinate(0, 0),
        Coordinate(4, 2),
        occupiedCells,
        updatedGrid.getEntity
      )
    )

  @Test def testLineOfSightBlockedByWall(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(4, 2))
      .setCell(wizard, Coordinate(0, 0))
      .setCell(wall, Coordinate(2, 1))

    val occupiedCells = updatedGrid.getOccupiedCells
    assertFalse(
      lineOfSightManager.isLineOfSightClear(
        Coordinate(0, 0),
        Coordinate(4, 2),
        occupiedCells,
        updatedGrid.getEntity
      )
    )

  @Test def testLineOfSightSameRow(): Unit =
    val updatedGrid = grid
      .setCell(soldier, Coordinate(0, 2))
      .setCell(wizard, Coordinate(0, 0))

    val occupiedCells = updatedGrid.getOccupiedCells
    assertTrue(
      lineOfSightManager.isLineOfSightClear(
        Coordinate(0, 0),
        Coordinate(0, 2),
        occupiedCells,
        updatedGrid.getEntity
      )
    )
