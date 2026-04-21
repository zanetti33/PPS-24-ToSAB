package it.unibo.tosab.update

import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.{DomainEvent, GameState}
import it.unibo.tosab.model.ai.CharacterAI.CharacterAI
import it.unibo.tosab.model.engine.Engine.Engine

object GameLoop extends GameLoopPublisher:
  @annotation.tailrec
  def run(currentState: GameState)(using ai: CharacterAI, engine: Engine): GameState =
    currentState.phase match
      case GameOver =>
        publish(DomainEvent.GameEnded(currentState))
        currentState
      case Setup    => run(currentState.copy(phase = Combat))
      case Combat =>
        currentState.turnQueue match
          case Nil => run(engine.startNewRound(currentState))
          case currentCharacterId :: _ =>
            val action = ai.determineNextAction(currentState, currentCharacterId)
            val outcome = engine.applyUnitAction(currentState, currentCharacterId, action)
            outcome.events.foreach(publish)
            val nextState = outcome.nextState
            publish(DomainEvent.GridUpdated(nextState.grid))
            run(nextState)
