package it.unibo.tosab.view

import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.io.MonadIO
import it.unibo.tosab.update.{GameLoopEvent, GameLoopSubscriber}

object ConsoleGameLogger extends GameLoopSubscriber:
  override def update(event: GameLoopEvent): Unit =
    event match
      case GameLoopEvent.ActionChosen(actorId, actor, action) =>
        val log = actor
          .map(character => ActionLog(character, action))
          .getOrElse(ActionLog(actorId, action))
        LoggerUtils.logAndDisplay(log).run()
      case GameLoopEvent.GridUpdated(grid) =>
        DisplayGrid.display(grid)
      case GameLoopEvent.GameEnded(finalState) =>
        MonadIO.printLine(formatGameOverMessage(finalState)).run()

  private def formatGameOverMessage(finalState: GameState): String =
    finalState.winningFaction match
      case Some(faction) => s"\n[GAME OVER] Winner: $faction"
      case None          => "\n[GAME OVER] No winner could be determined."
