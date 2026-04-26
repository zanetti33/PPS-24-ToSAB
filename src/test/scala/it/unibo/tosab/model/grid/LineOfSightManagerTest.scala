package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class LineOfSightManagerTest:

  val gridManager = HexagonalGrid(8)
  val lineOfSightManager = HexagonalLineOfSightManager(gridManager)
  val soldier: Character = Entity.soldier(EntityId("soldier"), Faction.Player)
  val wizard: Character = Entity.mage(EntityId("wizard"), Faction.AI)
  val wall: Obstacle = Entity.wall(EntityId("wall"))
  val bush: Obstacle = Entity.bush(EntityId("bush"))

  @Test def testLineOfSightClear(): Unit =
    val grid = GridFactory
      .createHexagonal(8)
      .setCell(soldier, Coordinate(4, 2))
      .setCell(wizard, Coordinate(0, 0))
      .setCell(bush, Coordinate(1, 0))

    val occupiedCells = grid.getOccupiedCells
    assertTrue(
      lineOfSightManager.isLineOfSightClear(
        Coordinate(0, 0),
        Coordinate(4, 2),
        occupiedCells,
        grid.getEntity
      )
    )

  @Test def testLineOfSightBlockedByWall(): Unit =
    val grid = GridFactory
      .createHexagonal(8)
      .setCell(soldier, Coordinate(4, 2))
      .setCell(wizard, Coordinate(0, 0))
      .setCell(wall, Coordinate(2, 1))

    val occupiedCells = grid.getOccupiedCells
    assertFalse(
      lineOfSightManager.isLineOfSightClear(
        Coordinate(0, 0),
        Coordinate(4, 2),
        occupiedCells,
        grid.getEntity
      )
    )

  @Test def testLineOfSightSameRow(): Unit =
    val grid = GridFactory
      .createHexagonal(8)
      .setCell(soldier, Coordinate(0, 2))
      .setCell(wizard, Coordinate(0, 0))

    val occupiedCells = grid.getOccupiedCells
    assertTrue(
      lineOfSightManager.isLineOfSightClear(
        Coordinate(0, 0),
        Coordinate(0, 2),
        occupiedCells,
        grid.getEntity
      )
    )
