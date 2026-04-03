package it.unibo.tosab.update

import it.unibo.tosab.model.GameState.GamePhase.GameOver
import it.unibo.tosab.model.GameState.GameState
import it.unibo.tosab.model.ai.AI.AI
import it.unibo.tosab.model.engine.Engine.Engine

object GameLoop:
  @annotation.tailrec
  def run(currentState: GameState)(using ai: AI, engine: Engine): GameState = currentState.phase match
    case GameOver => currentState
    case _ =>
      val action = ai.determineNextAction(currentState)
      val nextState = engine.applyAction(currentState, action)
      run(nextState)
