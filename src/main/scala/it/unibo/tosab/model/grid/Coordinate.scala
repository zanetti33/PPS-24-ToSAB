package it.unibo.tosab.model.grid

/** Represents a coordinate on the grid with x and y values. */
opaque type Coordinate = (Int, Int)

/** Companion object for Coordinate providing factory and utility methods. */
object Coordinate:
  /** Creates a Coordinate from x and y. */
  def apply(x: Int, y: Int): Coordinate = (x, y)

  /** Extracts the (x, y) tuple from a Coordinate.
    * @param coord
    *   the coordinate
    * @return
    *   the (x, y) tuple
    */
  def value(coord: Coordinate): (Int, Int) = coord

  /** Unapplies a Coordinate to its (x, y) tuple.
    * @param coord
    *   the coordinate
    * @return
    *   Some((x, y))
    */
  def unapply(coord: Coordinate): Option[(Int, Int)] = Some(coord)

  extension (coord: Coordinate)
    /** Gets the x value of the coordinate. */
    def x: Int = coord._1

    /** Gets the y value of the coordinate. */
    def y: Int = coord._2
