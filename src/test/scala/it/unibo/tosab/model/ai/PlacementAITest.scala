package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.grid.{Grid, GridLane, HexagonalGrid, Lane, PlacementManager}
import it.unibo.tosab.model.ai.PlacementAI
import it.unibo.tosab.model.entities.{Character, Role}

class PlacementAITest:
  val grid = Grid()

  @Test def testPlacementAITroopsArePlaced(): Unit =
    val updatedGrid = PlacementAI.placeAITroops(grid)
    val occupiedCells = updatedGrid.getOccupiedCells
    assertFalse(occupiedCells.isEmpty)

  @Test def testPlacementAINumberOfTroops(): Unit =
    val updatedGrid = PlacementAI.placeAITroops(grid)
    val occupiedCells = updatedGrid.getOccupiedCells
    val numberOfAITroops = occupiedCells.size
    assertTrue(numberOfAITroops > 0 && numberOfAITroops <= 5)

  @Test def testCalculateLanes(): Unit =
    val (lane1, lane2, lane3) = GridLane.calculateLanes(3)
    assertEquals(Lane(0, 1), lane1)
    assertEquals(Lane(1, 2), lane2)
    assertEquals(Lane(2, 3), lane3)

    val (lane4, lane5, lane6) = GridLane.calculateLanes(4)
    assertEquals(Lane(0, 2), lane4)
    assertEquals(Lane(1, 3), lane5)
    assertEquals(Lane(2, 4), lane6)

    val (lane7, lane8, lane9) = GridLane.calculateLanes(5)
    assertEquals(Lane(0, 2), lane7)
    assertEquals(Lane(2, 4), lane8)
    assertEquals(Lane(3, 5), lane9)

  @Test def testTroopRolesIfNumberOfRolesIsLowerThanMaxTroops(): Unit =
    val updatedGrid = PlacementAI.placeAITroops(grid, 2)
    val roles = updatedGrid.getOccupiedCells
      .flatMap(pos => updatedGrid.getEntity(pos))
      .collect {
        case c: Character if c.faction == it.unibo.tosab.model.entities.Faction.AI => c.role
      }
      .toSeq
    assertEquals(2, roles.size)
    assertNotEquals(roles.head, roles(1))

  @Test def testTroopRolesIfNumberOfRolesIsHigherThanMaxTroops(): Unit =
    val updatedGrid = PlacementAI.placeAITroops(grid, 5)
    val roles = updatedGrid.getOccupiedCells
      .flatMap(pos => updatedGrid.getEntity(pos))
      .collect {
        case c: Character if c.faction == it.unibo.tosab.model.entities.Faction.AI => c.role
      }
      .toSeq
    assertTrue(roles.size >= 3 && roles.size <= 5)
    assertTrue(roles.count(_ == Role.Soldier) >= 1)
    assertTrue(roles.count(_ == Role.Archer) >= 1)
    assertTrue(roles.count(_ == Role.Mage) >= 1)
