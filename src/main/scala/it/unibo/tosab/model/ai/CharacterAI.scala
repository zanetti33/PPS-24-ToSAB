package it.unibo.tosab.model.ai

import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.GameState

object CharacterAI:
  /** This trait represents the AI component of the game. It defines a method to determine the next
    * action based on the current game state.
    */
  trait CharacterAI:
    def determineNextAction(state: GameState, currentCharacterId: String): GameAction

  /** A simple implementation of the AI trait that always returns GameAction.Pass, effectively doing
    * nothing.
    */
  object DoesNothingCharacterAI extends CharacterAI:
    override def determineNextAction(state: GameState, currentCharacterId: String): GameAction =
      GameAction.Pass

  /** A more complex implementation of the AI trait that tries to attack the closest enemy, and if no
    * attack is possible, it tries to move towards the closest enemy. If neither action is possible, it passes.
    */
  object BasicCharacterAI extends CharacterAI:
    def determineNextAction(state: GameState, actorId: String): GameAction =
      val meOpt = state.getCharacterById(actorId)
      val myPosOpt = state.getPositionOf(actorId)
      // Using pattern matching to safely unwrap the Options
      (meOpt, myPosOpt) match
        case (Some(me), Some(myPos)) => Behaviors.attackClosestEnemy(state, me, myPos)
          .orElse(Behaviors.moveTowardsClosestEnemy(state, me, myPos))
          .getOrElse(GameAction.Pass) // If no attack or move is possible, pass
        case _ => GameAction.Pass