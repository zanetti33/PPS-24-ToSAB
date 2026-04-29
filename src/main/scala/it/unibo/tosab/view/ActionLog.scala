package it.unibo.tosab.view

import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.entities.EntityId

/** Represents a logged game action performed by a character.
  *
  * @param characterName
  *   the name of the character performing the action
  * @param action
  *   the game action that was performed
  * @param timestamp
  *   when the action occurred (defaults to current time)
  */
case class ActionLog(
    characterName: String,
    action: GameAction,
    timestamp: Long = System.currentTimeMillis()
):

  /** Returns a human-readable string representation of the action log.
    *
    * @return
    *   formatted string showing character name and action description
    */
  override def toString: String =
    s"$characterName ${formatAction(action)}"

  private def formatAction(action: GameAction): String =
    action match
      case GameAction.Move(targetPosition) =>
        s"moved to $targetPosition."
      case GameAction.Attack(targetId) =>
        s"attacked $targetId."
      case GameAction.Pass =>
        "passes turn."

object ActionLog:
  /** Creates an ActionLog from an EntityId and GameAction.
    *
    * @param characterId
    *   the ID of the character performing the action
    * @param action
    *   the game action performed
    * @return
    *   a new ActionLog with the character that executed the action
    */
  def apply(characterId: EntityId, action: GameAction): ActionLog =
    ActionLog(
      characterName = EntityId.value(characterId),
      action = action
    )
