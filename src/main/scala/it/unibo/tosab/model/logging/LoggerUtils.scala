package it.unibo.tosab.model.logging

import it.unibo.tosab.model.io.MonadIO

object LoggerUtils:

  def logAndDisplay(log: ActionLog): MonadIO[Unit] =
    MonadIO.printLine(log.toString).flatMap(_ => MonadIO.sleep(1000))
