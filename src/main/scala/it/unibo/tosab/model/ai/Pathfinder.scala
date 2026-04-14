package it.unibo.tosab.model.ai

import it.unibo.tosab.model.entities.Entity
import it.unibo.tosab.model.grid.{Coordinate, Grid}

object Pathfinder:
  /** Evaluates adjacent cells and picks the one closest to the target. This is a "Greedy" approach
    */
  def findNextStep(
      grid: Grid,
      entity: Entity,
      startPos: Coordinate,
      targetPos: Coordinate
  ): Option[Coordinate] =
    val adjacentCells = grid.getAdjacentAvailableCells(entity)
    adjacentCells match
      case ac if ac.isEmpty => None
      case _                => Some(adjacentCells.minBy(grid.getDistance(_, targetPos)))
