package it.unibo.tosab.model.engine

import it.unibo.tosab.model.{GamePhase, GameState}

class TimedEngine(val base: Engine, val maxTurns: Int) extends Engine:
  private var currentTurnCount = 0

  override def startNewRound(state: GameState): GameState =
    currentTurnCount += 1
    if currentTurnCount > maxTurns then
      // If we've reached the max turns, we end the game immediately with a draw
      state.copy(phase = GamePhase.GameOver, turnQueue = Seq.empty)
    else base.startNewRound(state)

  override def applyUnitAction(state: GameState, intent: CommandIntent): EngineOutcome =
    base.applyUnitAction(state, intent)
