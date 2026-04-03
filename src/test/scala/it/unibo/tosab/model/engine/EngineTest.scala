package it.unibo.tosab.model.engine

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.GameState.{ GamePhase, GameState }
import it.unibo.tosab.model.engine.Engine.{ DoesNothingEngine, Engine }
import it.unibo.tosab.model.grid.Grid

class EngineTest:

  val grid: Grid       = Grid()
  val combatState: GameState  = GameState(GamePhase.Combat, grid)

  @Test def dummyEngineDoesNotChangePhase(): Unit =
    val engine: Engine = DoesNothingEngine
    val result = engine.applyAction(combatState, GameAction.Pass)
    assertEquals(combatState, result)

  @Test def dummyEngineDoesNotChangeGrid(): Unit =
    grid.setCell("archer", (1, 1))
    val result = DoesNothingEngine.applyAction(combatState, GameAction.Pass)
    assertEquals("archer", result.grid.getEntity((1, 1)))
