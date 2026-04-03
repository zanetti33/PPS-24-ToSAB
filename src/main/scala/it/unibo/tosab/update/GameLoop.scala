package it.unibo.tosab.update

import it.unibo.tosab.model.GamePhase.GameOver
import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.ai.AI.AI
import it.unibo.tosab.model.engine.Engine.Engine

object GameLoop:
  @annotation.tailrec
  def run(currentState: GameState)(using ai: AI, engine: Engine): GameState =
    currentState.phase match
      case GameOver => currentState
      // could be written in a row, but this way it's more readable
      case _ =>
        val action = ai.determineNextAction(currentState)
        val nextState = engine.applyAction(currentState, action)
        run(nextState)
