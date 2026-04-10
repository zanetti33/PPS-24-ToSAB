package it.unibo.tosab.model.engine

import it.unibo.tosab.model.entities.{Entity, Faction}
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.Grid
import org.junit.Assert.*
import org.junit.Test

class TurnOrderManagerTest:
  private val slowUnitId = "s1"
  private val secondSlowUnitId = "s2"
  private val fastUnitId = "f1"
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
    grid.setCell(fastUnit, (4, 4))
    grid.setCell(slowUnit, (4, 1))
    val gameState = GameState(GamePhase.Combat, grid, Nil)
    val result = TurnOrderManager.determineTurnOrder(gameState)
    assertEquals(expectedQueue, result)

  @Test def testTurnOrderWithSameSpeedsBasedOnPosition(): Unit =
    val expectedQueue = Seq(secondSlowUnitId, slowUnitId)
    val grid = Grid()
    grid.setCell(slowUnit, (4, 4))
    grid.setCell(secondSlowUnit, (4, 1))
    val gameState = GameState(GamePhase.Combat, Grid(), expectedQueue)
    val result = TurnOrderManager.determineTurnOrder(gameState)
    assertEquals(expectedQueue, result)

