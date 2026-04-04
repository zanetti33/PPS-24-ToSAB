package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.ai.AI.{AI, DoesNothingAI}
import it.unibo.tosab.model.grid.Grid

class AITest:

  val state: GameState = GameState(GamePhase.Setup, Grid())

  @Test def DoesNothingAIAlwaysReturnsPass(): Unit =
    val action = DoesNothingAI.determineNextAction(state)
    assertEquals(GameAction.Pass, action)

  @Test def DoesNothingAIIsConsistent(): Unit =
    val a1 = DoesNothingAI.determineNextAction(state)
    val a2 = DoesNothingAI.determineNextAction(GameState(GamePhase.Combat, Grid()))
    assertEquals(a1, a2)

  @Test def DoesNothingAIImplementsAITrait(): Unit =
    val ai: AI = DoesNothingAI
    val action = ai.determineNextAction(state)
    assertEquals(GameAction.Pass, action)
