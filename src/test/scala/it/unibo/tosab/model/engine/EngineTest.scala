package it.unibo.tosab.model.engine

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.engine.Engine.{DoesNothingEngine, Engine, ImmediatelyEndEngine}
import it.unibo.tosab.model.entities.*
import it.unibo.tosab.model.entities.Entity.*
import it.unibo.tosab.model.grid.Grid

class EngineTest:

  val grid: Grid = Grid()
  val combatState: GameState = GameState(GamePhase.Combat, grid)
  val gameOverState: GameState = GameState(GamePhase.GameOver, grid)

  @Test def doesNothingDoesNotChangePhase(): Unit =
    val engine: Engine = DoesNothingEngine
    val result = engine.applyAction(combatState, GameAction.Pass)
    assertEquals(combatState, result)

  @Test def doesNothingEngineDoesNotChangeGrid(): Unit =
    val archer = createEntity("archer", Faction.AI, Role.Archer)
    grid.setCell(archer, (1, 1))
    val result = DoesNothingEngine.applyAction(combatState, GameAction.Pass)
    assertEquals(Some(archer), result.grid.getEntity((1, 1)))

  @Test def immediatelyEndEngineChangesPhaseToGameOver(): Unit =
    val engine: Engine = ImmediatelyEndEngine
    val result = engine.applyAction(combatState, GameAction.Pass)
    assertEquals(gameOverState, result)
