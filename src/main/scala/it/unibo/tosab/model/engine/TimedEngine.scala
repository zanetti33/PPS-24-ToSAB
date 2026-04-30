package it.unibo.tosab.model.engine

import it.unibo.tosab.model.{GamePhase, GameState}

/** Engine decorator that ends the game after a fixed number of rounds.
  *
  * @param base
  *   wrapped engine used for normal behavior
  * @param maxTurns
  *   maximum number of rounds before forcing `GameOver` (default: 30)
  */
class TimedEngine(val base: Engine, val maxTurns: Int = 30) extends Engine:
  private var currentTurnCount = 0

  export base.{startNewRound => _, *}

  override def startNewRound(state: GameState): GameState =
    currentTurnCount += 1
    if currentTurnCount > maxTurns then
      // If we've reached the max turns, we end the game immediately with a draw
      state.copy(phase = GamePhase.GameOver, turnQueue = Seq.empty)
    else base.startNewRound(state)
