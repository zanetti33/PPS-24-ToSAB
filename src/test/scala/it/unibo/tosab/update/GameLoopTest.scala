package it.unibo.tosab.update

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameState.{GamePhase, GameState}
import it.unibo.tosab.model.ai.AI.{AI, DoesNothingAI}
import it.unibo.tosab.model.engine.Engine.{DoesNothingEngine, Engine, ImmediatelyEndEngine}
import it.unibo.tosab.model.grid.Grid

class GameLoopTest:

  given defaultAI: AI = DoesNothingAI
  given defaultEngine: Engine = DoesNothingEngine
  val grid: Grid = Grid()

  @Test def runDoesntDoAnything(): Unit =
    val gameOver = GameState(GamePhase.GameOver, grid)
    val result   = GameLoop.run(gameOver)
    assertEquals(GamePhase.GameOver, result.phase)

  @Test def runEndsTheGame(): Unit =
    given engine: Engine = ImmediatelyEndEngine
    val gameOver = GameState(GamePhase.Combat, grid)
    val result   = GameLoop.run(gameOver)
    assertEquals(GamePhase.GameOver, result.phase)

