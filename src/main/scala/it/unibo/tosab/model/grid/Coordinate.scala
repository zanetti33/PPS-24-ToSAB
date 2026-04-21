package it.unibo.tosab.model.grid

opaque type Coordinate = (Int, Int)

object Coordinate:
  def apply(x: Int, y: Int): Coordinate = (x, y)
  def x(coord: Coordinate): Int = coord._1
  def y(coord: Coordinate): Int = coord._2
  def value(coord: Coordinate): (Int, Int) = coord
  def unapply(coord: Coordinate): Option[(Int, Int)] = Some(coord)
