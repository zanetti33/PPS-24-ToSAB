package it.unibo.tosab.view

import it.unibo.tosab.view.ActionLog

/** Utility object for logging and displaying game actions.
  */
object LoggerUtils:

  /** Logs an action and displays it to the user with a delay.
    *
    * @param log
    *   the action log to display
    * @return
    *   an IO action that prints the log and waits
    */
  def logAndDisplay(log: ActionLog): IO[Unit] =
    IO.printLine(log.toString).flatMap(_ => IO.sleep(3000))
