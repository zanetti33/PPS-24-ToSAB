package it.unibo.tosab.model.ai

import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.EntityId

/** This trait represents the AI component of the game. It defines a method to determine the next
 * action based on the current game state.
 */
trait CharacterAI:
  def determineNextAction(state: GameState, currentCharacterId: EntityId): GameAction

object CharacterAI:

  /** A simple implementation of the AI trait that always returns GameAction.Pass, effectively doing
    * nothing.
    */
  object DoesNothingCharacterAI extends CharacterAI:
    override def determineNextAction(state: GameState, currentCharacterId: EntityId): GameAction =
      GameAction.Pass

  /** A more complex implementation of the AI trait that tries to attack the closest enemy, and if
    * no attack is possible, it tries to move towards the closest enemy. If neither action is
    * possible, it passes.
    */
  val BasicCharacterAI: CharacterAI = ConfigurableCharacterAI(
    Behaviors.attackClosestEnemy,
    Behaviors.moveTowardsClosestEnemy
  )
