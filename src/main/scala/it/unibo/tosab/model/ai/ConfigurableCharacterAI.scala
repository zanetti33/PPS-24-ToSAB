package it.unibo.tosab.model.ai

import it.unibo.tosab.model.ai.CharacterAI
import it.unibo.tosab.model.entities.EntityId
import it.unibo.tosab.model.{GameAction, GameState}

/**
  * Character AI composed of an ordered list of behaviors.
  * The first behavior returning an action wins.
  */
class ConfigurableCharacterAI(behaviors: Behavior*) extends CharacterAI:

  /**
    * Evaluates configured behaviors in order and returns the first action found.
    *
    * @param state current game state
    * @param actorId id of the acting character
    * @return selected action, or `Pass` when no behavior applies
    */
  def determineNextAction(state: GameState, actorId: EntityId): GameAction =
    val meOpt = state.getCharacterById(actorId)
    val myPosOpt = state.getPositionOf(actorId)
    (meOpt, myPosOpt) match
      case (Some(me), Some(myPos)) =>
        behaviors.iterator
          .flatMap(behavior => behavior(state, me, myPos))
          .nextOption()
          .getOrElse(GameAction.Pass)
      case _ => GameAction.Pass
