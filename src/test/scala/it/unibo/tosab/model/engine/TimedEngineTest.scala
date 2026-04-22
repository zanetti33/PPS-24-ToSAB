package it.unibo.tosab.model.engine

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.entities.EntityId
import it.unibo.tosab.model.grid.Grid

class TimedEngineTest:

  private val baseState = GameState(GamePhase.Combat, Grid(), turnQueue = Seq(EntityId("unit-1")))

  private class RecordingEngine extends Engine:
    var startRoundCalls: Int = 0
    var applyActionCalls: Int = 0

    override def startNewRound(state: GameState): GameState =
      startRoundCalls += 1
      state.copy(turnQueue = Seq(EntityId("delegated")))

    override def applyUnitAction(state: GameState, intent: CommandIntent): EngineOutcome =
      applyActionCalls += 1
      EngineOutcome(nextState = state.copy(turnQueue = Seq(EntityId("action-delegated"))))

  @Test def testStartNewRoundDelegatesUntilMaxTurns(): Unit =
    val base = new RecordingEngine()
    val timed = new TimedEngine(base, maxTurns = 2)

    val first = timed.startNewRound(baseState)
    val second = timed.startNewRound(baseState)

    assertEquals(2, base.startRoundCalls)
    assertEquals(Seq(EntityId("delegated")), first.turnQueue)
    assertEquals(Seq(EntityId("delegated")), second.turnQueue)
    assertEquals(GamePhase.Combat, second.phase)

  @Test def testStartNewRoundEndsGameAfterMaxTurnsWithoutDelegating(): Unit =
    val base = new RecordingEngine()
    val timed = new TimedEngine(base, maxTurns = 2)

    timed.startNewRound(baseState)
    timed.startNewRound(baseState)
    val third = timed.startNewRound(baseState)

    assertEquals(2, base.startRoundCalls)
    assertEquals(GamePhase.GameOver, third.phase)
    assertTrue(third.turnQueue.isEmpty)

  @Test def testApplyUnitActionIsDelegatedToBaseEngine(): Unit =
    val base = new RecordingEngine()
    val timed = new TimedEngine(base, maxTurns = 1)
    val intent = CommandIntent(EntityId("unit-1"), GameAction.Pass)

    val outcome = timed.applyUnitAction(baseState, intent)

    assertEquals(1, base.applyActionCalls)
    assertEquals(Seq(EntityId("action-delegated")), outcome.nextState.turnQueue)
