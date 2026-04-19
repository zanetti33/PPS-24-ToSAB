package it.unibo.tosab.update

import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.ai.CharacterAI.CharacterAI
import it.unibo.tosab.model.engine.Engine.Engine
import it.unibo.tosab.view.{ActionLog, LoggerUtils}

object GameLoop:
  @annotation.tailrec
  def run(currentState: GameState)(using ai: CharacterAI, engine: Engine): GameState =
    currentState.phase match
      case GameOver => currentState
      case Setup    => run(currentState.copy(phase = Combat))
      case Combat =>
        currentState.turnQueue match
          case Nil => run(engine.startNewRound(currentState))
          case currentCharacterId :: _ =>
            val action = ai.determineNextAction(currentState, currentCharacterId)
            val log = currentState
              .getCharacterById(currentCharacterId)
              .map(character => ActionLog(character, action))
            LoggerUtils.logAndDisplay(log.getOrElse(ActionLog("Unknown", action))).run()
            val nextState = engine.applyUnitAction(currentState, currentCharacterId, action)
            run(nextState)
