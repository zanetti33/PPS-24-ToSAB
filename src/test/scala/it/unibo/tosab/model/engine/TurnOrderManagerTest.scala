package it.unibo.tosab.model.engine

import it.unibo.tosab.model.entities.{Entity, EntityId, Faction}
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.{Coordinate, Grid}
import org.junit.Assert.*
import org.junit.Test

class TurnOrderManagerTest:
  private val slowUnitId = EntityId("s1")
  private val secondSlowUnitId = EntityId("s2")
  private val fastUnitId = EntityId("f1")
  private val slowUnit = Entity.soldier(slowUnitId, Faction.Player)
  private val secondSlowUnit = Entity.mage(secondSlowUnitId, Faction.Player)
  private val fastUnit = Entity.archer(fastUnitId, Faction.Player)

  @Test def testDetermineTurnOrderWithEmptyQueueReturnsEmpty(): Unit =
    val gameState = GameState(GamePhase.Combat, Grid(), Seq.empty)
    val result = TurnOrderManager.determineTurnOrder(gameState)
    assertTrue(result.isEmpty)

  @Test def testTurnOrderWithDifferentSpeeds(): Unit =
    val expectedQueue = Seq(fastUnitId, slowUnitId)
    val grid = Grid()
      .setCell(fastUnit, Coordinate(4, 4))
      .setCell(slowUnit, Coordinate(4, 1))
    val gameState = GameState(GamePhase.Combat, grid, Nil)
    val result = TurnOrderManager.determineTurnOrder(gameState)
    assertEquals(expectedQueue, result)

  @Test def testTurnOrderWithSameSpeedsBasedOnPosition(): Unit =
    val expectedQueue = Seq(secondSlowUnitId, slowUnitId)
    val grid = Grid()
      .setCell(slowUnit, Coordinate(4, 4))
      .setCell(secondSlowUnit, Coordinate(4, 1))
    val gameState = GameState(GamePhase.Combat, grid, expectedQueue)
    val result = TurnOrderManager.determineTurnOrder(gameState)
    assertEquals(expectedQueue, result)
