package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.GameState.{ GamePhase, GameState }
import it.unibo.tosab.model.ai.AI.{ AI, DummyAI }
import it.unibo.tosab.model.grid.Grid

class AITest:

  val state: GameState = GameState(GamePhase.Setup, Grid())

  @Test def dummyAIAlwaysReturnsPass(): Unit =
    val action = DummyAI.determineNextAction(state)
    assertEquals(GameAction.Pass, action)

  @Test def dummyAIIsConsistent(): Unit =
    val a1 = DummyAI.determineNextAction(state)
    val a2 = DummyAI.determineNextAction(GameState(GamePhase.Combat, Grid()))
    assertEquals(a1, a2)

  @Test def dummyAIImplementsAITrait(): Unit =
    val ai: AI = DummyAI
    val action = ai.determineNextAction(state)
    assertEquals(GameAction.Pass, action)

