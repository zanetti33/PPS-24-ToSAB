package it.unibo.tosab.view

import it.unibo.tosab.model.{DomainEvent, GameState}
import it.unibo.tosab.model.io.MonadIO
import it.unibo.tosab.update.GameLoopSubscriber

object ConsoleGameLogger extends GameLoopSubscriber:
  override def update(event: DomainEvent): Unit =
    event match
      case DomainEvent.ActionApplied(actorId, action) =>
        LoggerUtils.logAndDisplay(ActionLog(actorId, action)).run()
      case DomainEvent.DamageInflicted(attackerId, targetId, amount) =>
        MonadIO.printLine(s"$attackerId dealt $amount damage to $targetId.").run()
      case DomainEvent.UnitDied(unitId) =>
        MonadIO.printLine(s"[DEATH] Unit $unitId has been defeated.").run()
      case DomainEvent.GridUpdated(grid) =>
        DisplayGrid.display(grid)
      case DomainEvent.GameEnded(finalState) =>
        MonadIO.printLine(formatGameOverMessage(finalState)).run()

  private def formatGameOverMessage(finalState: GameState): String =
    finalState.winningFaction match
      case Some(faction) => s"\n[GAME OVER] Winner: $faction"
      case None          => "\n[GAME OVER] No winner could be determined."
