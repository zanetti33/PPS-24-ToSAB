package it.unibo.tosab.view

import it.unibo.tosab.view.ActionLog

object LoggerUtils:

  def logAndDisplay(log: ActionLog): IO[Unit] =
    IO.printLine(log.toString).flatMap(_ => IO.sleep(3000))
