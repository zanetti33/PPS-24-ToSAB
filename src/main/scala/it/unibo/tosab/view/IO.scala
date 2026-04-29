package it.unibo.tosab.view

/** IO monad for handling input/output.
  *
  * @param run
  *   the thunk that encapsulates the side effect
  * @tparam A
  *   the type of value produced by the IO action
  */
case class IO[A](run: () => A):

  /** Transforms the result of this IO action using the given function. */
  def map[B](f: A => B): IO[B] = IO(() => f(this.run()))

  /** Chains IO actions. */
  def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(this.run()).run())

object IO:

  /** Creates an IO action that reads a line from standard input. */
  def readString(): IO[String] = IO(() => scala.io.StdIn.readLine())

  /** Creates an IO action that prints a message to standard output. */
  def printLine(msg: String): IO[Unit] = IO(() => println(msg))

  /** Creates an IO action that pauses execution for the specified duration */
  def sleep(milliseconds: Int): IO[Unit] =
    IO(() => Thread.sleep(milliseconds))
