package it.unibo.tosab.update

import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.ai.CharacterAI.CharacterAI
import it.unibo.tosab.model.engine.Engine.Engine

object GameLoop extends GameLoopPublisher:
  @annotation.tailrec
  def run(currentState: GameState)(using ai: CharacterAI, engine: Engine): GameState =
    currentState.phase match
      case GameOver =>
        publish(GameLoopEvent.GameEnded(currentState))
        currentState
      case Setup    => run(currentState.copy(phase = Combat))
      case Combat =>
        currentState.turnQueue match
          case Nil => run(engine.startNewRound(currentState))
          case currentCharacterId :: _ =>
            val actor = currentState.getCharacterById(currentCharacterId)
            val action = ai.determineNextAction(currentState, currentCharacterId)
            publish(GameLoopEvent.ActionChosen(currentCharacterId, actor, action))
            val nextState = engine.applyUnitAction(currentState, currentCharacterId, action)
            publish(GameLoopEvent.GridUpdated(nextState.grid))
            run(nextState)
