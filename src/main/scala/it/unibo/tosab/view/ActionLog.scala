package it.unibo.tosab.view

import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.entities.EntityId

case class ActionLog(
    characterName: String,
    action: GameAction,
    timestamp: Long = System.currentTimeMillis()
):

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
  def apply(characterId: EntityId, action: GameAction): ActionLog =
    ActionLog(
      characterName = EntityId.value(characterId),
      action = action
    )
