package it.unibo.tosab.view

case class IO[A](run: () => A):

  def map[B](f: A => B): IO[B] = IO(() => f(this.run()))

  def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(this.run()).run())

object IO:

  def readString(): IO[String] = IO(() => scala.io.StdIn.readLine())

  def printLine(msg: String): IO[Unit] = IO(() => println(msg))

  def sleep(milliseconds: Int): IO[Unit] =
    IO(() => Thread.sleep(milliseconds))
