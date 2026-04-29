package it.unibo.tosab.update

import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.{DomainEvent, GameState}
import it.unibo.tosab.model.ai.CharacterAI
import it.unibo.tosab.model.engine.{CommandIntent, Engine}

/** Main game loop that processes game states and actions until the game ends. It uses a
  * `CharacterAI` to determine actions for the current character and an `Engine` to apply those
  * actions to the game state.
  */
object GameLoop extends GameLoopPublisher:
  @annotation.tailrec
  def run(currentState: GameState)(using ai: CharacterAI, engine: Engine): GameState =
    currentState.phase match
      case GameOver =>
        publish(DomainEvent.GameEnded(currentState))
        currentState
      case Setup => run(currentState.copy(phase = Combat))
      case Combat =>
        currentState.turnQueue match
          case Nil => run(engine.startNewRound(currentState))
          case currentCharacterId :: _ =>
            val action = ai.determineNextAction(currentState, currentCharacterId)
            val outcome = engine.applyUnitAction(
              currentState,
              intent = CommandIntent(currentCharacterId, action)
            )
            outcome.events.foreach(publish)
            val nextState = outcome.nextState
            if nextState.gridChanged(currentState) then
              publish(DomainEvent.GridUpdated(nextState.grid))
            run(nextState)
