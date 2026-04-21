package it.unibo.tosab.model.io

case class MonadIO[A](run: () => A):

  def map[B](f: A => B): MonadIO[B] = MonadIO(() => f(this.run()))

  def flatMap[B](f: A => MonadIO[B]): MonadIO[B] = MonadIO(() => f(this.run()).run())

object MonadIO:

  def readString(): MonadIO[String] = MonadIO(() => scala.io.StdIn.readLine())

  def printLine(msg: String): MonadIO[Unit] = MonadIO(() => println(msg))

  def sleep(milliseconds: Int): MonadIO[Unit] =
    MonadIO(() => Thread.sleep(milliseconds))
