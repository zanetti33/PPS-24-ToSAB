package it.unibo.tosab.view

import it.unibo.tosab.model.{DomainEvent, GameState}
import it.unibo.tosab.update.GameLoopSubscriber

/** Logs and displays game events to the console during gameplay.
  *
  * Extends GameLoopSubscriber to receive and handle domain events from the game loop, formatting
  * them for console output including actions, damage, unit deaths, grid updates, and game
  * completion messages.
  */
object ConsoleGameLogger extends GameLoopSubscriber:
  /** Processes domain events and displays relevant information to the console. */
  override def update(event: DomainEvent): Unit =
    event match
      case DomainEvent.ActionApplied(actorId, action) =>
        LoggerUtils.logAndDisplay(ActionLog(actorId, action)).run()
      case DomainEvent.DamageInflicted(attackerId, targetId, amount) =>
        IO.printLine(s"$attackerId dealt $amount damage to $targetId.").run()
      case DomainEvent.UnitDied(unitId) =>
        IO.printLine(s"[DEATH] Unit $unitId has been defeated.").run()
      case DomainEvent.GridUpdated(grid) =>
        DisplayGrid.display(grid)
      case DomainEvent.GameEnded(finalState) =>
        IO.printLine(formatGameOverMessage(finalState)).run()

  private def formatGameOverMessage(finalState: GameState): String =
    finalState.winningFaction match
      case Some(faction) => s"\n[GAME OVER] Winner: $faction"
      case None          => "\n[GAME OVER] No winner could be determined."
