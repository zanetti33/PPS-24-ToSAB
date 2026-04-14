package it.unibo.tosab.update

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.ai.CharacterAI.{CharacterAI, DoesNothingCharacterAI}
import it.unibo.tosab.model.engine.Engine.{DoesNothingEngine, Engine, ImmediatelyEndEngine, TurnBasedCombatEngine}
import it.unibo.tosab.model.entities.{Entity, Faction}
import it.unibo.tosab.model.grid.Grid

class GameLoopTest:

  private val playerId = "player-1"
  private val lonePlayerPosition = (4, 4)

  given defaultAI: CharacterAI = DoesNothingCharacterAI
  given defaultEngine: Engine = DoesNothingEngine
  val grid: Grid = Grid()

  @Test def testRunDoesntDoAnything(): Unit =
    val gameOver = GameState(GamePhase.GameOver, grid)
    val result = GameLoop.run(gameOver)
    assertEquals(GamePhase.GameOver, result.phase)

  @Test def testRunEndsTheGame(): Unit =
    given engine: Engine = ImmediatelyEndEngine
    val gameOver = GameState(GamePhase.Combat, grid)
    val result = GameLoop.run(gameOver)
    assertEquals(GamePhase.GameOver, result.phase)

  @Test def testRunEndsWhenOnlyOneFactionRemains(): Unit =
    given engine: Engine = TurnBasedCombatEngine
    val lonePlayerGrid =
      Grid().setCell(Entity.soldier(playerId, Faction.Player), lonePlayerPosition)
    val combatState = GameState(GamePhase.Combat, lonePlayerGrid)

    val result = GameLoop.run(combatState)

    assertEquals(GamePhase.GameOver, result.phase)

