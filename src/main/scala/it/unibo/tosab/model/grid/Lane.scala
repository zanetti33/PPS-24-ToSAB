package it.unibo.tosab.model.grid

/** Represents a lane with from and to indices. */
case class Lane(from: Int, to: Int)

/** Utility for calculating grid lanes. */
object GridLane:

  /** Calculates lanes for a given grid size.
    * @param gridSize
    *   the grid size
    * @return
    *   tuple of three lanes
    */
  def calculateLanes(gridSize: Int): (Lane, Lane, Lane) =
    val remainder = gridSize % 3
    val step = gridSize / 3
    remainder match
      case 0 => (Lane(0, step), Lane(step, 2 * step), Lane(2 * step, 3 * step))
      case 1 => (Lane(0, step + 1), Lane(step, 2 * step + 1), Lane(2 * step, 3 * step + 1))
      case 2 => (Lane(0, step + 1), Lane(step + 1, 2 * step + 2), Lane(2 * step + 1, 3 * step + 2))
