package it.unibo.tosab.update

import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.ai.AI.AI
import it.unibo.tosab.model.engine.Engine.Engine

object GameLoop:
  @annotation.tailrec
  def run(currentState: GameState)(using ai: AI, engine: Engine): GameState =
    currentState.phase match
      case GameOver => currentState
      case Setup    => run(currentState.copy(phase = Combat))
      case Combat =>
        currentState.turnQueue match
          case Nil => run(engine.startNewRound(currentState))
          case currentCharacterId :: _ =>
            val action = ai.determineNextAction(currentState, currentCharacterId)
            val nextState = engine.applyUnitAction(currentState, currentCharacterId, action)
            run(nextState)
