package it.unibo.tosab.view

import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.entities.Character

case class ActionLog(
    characterId: String,
    action: GameAction,
    timestamp: Long = System.currentTimeMillis()
):

  override def toString: String =
    s"$characterId ${formatAction(action)}"

  private def formatAction(action: GameAction): String =
    action match
      case GameAction.Move(targetPosition) =>
        s"moved to $targetPosition."
      case GameAction.Attack(targetId) =>
        s"attacked $targetId."
      case GameAction.Pass =>
        "passes turn."

object ActionLog:

  def apply(character: Character, action: GameAction): ActionLog =
    ActionLog(
      characterId = s"${character.id} (${character.faction})",
      action = action
    )
