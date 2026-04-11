package it.unibo.tosab.model.ai

import it.unibo.tosab.model.ai.CharacterAI.CharacterAI
import it.unibo.tosab.model.{GameAction, GameState}

class ConfigurableCharacterAI(behaviors: Behavior*) extends CharacterAI:

  def determineNextAction(state: GameState, actorId: String): GameAction =
    val meOpt = state.getCharacterById(actorId)
    val myPosOpt = state.getPositionOf(actorId)
    (meOpt, myPosOpt) match
      case (Some(me), Some(myPos)) =>
        behaviors.iterator
          .flatMap(behavior => behavior(state, me, myPos))
          .nextOption()
          .getOrElse(GameAction.Pass)
      case _ => GameAction.Pass
