package it.unibo.tosab.view

import it.unibo.tosab.model.io.MonadIO
import it.unibo.tosab.view.ActionLog

object LoggerUtils:

  def logAndDisplay(log: ActionLog): MonadIO[Unit] =
    MonadIO.printLine(log.toString).flatMap(_ => MonadIO.sleep(3000))
