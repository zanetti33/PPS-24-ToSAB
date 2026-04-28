package it.unibo.tosab.model.engine

import it.unibo.tosab.model.grid.{Coordinate, Grid, HexagonalGrid}

trait MovementRules:
  /** Given a grid and a position, returns a set of available moves from that position. Each move is
    * represented as a tuple of the target coordinate and the associated movement cost.
    */
  def availableMoves(grid: Grid, position: Coordinate): Set[(Coordinate, Int)]

object StandardMovementRules extends MovementRules:
  private val singleStepMovementCost = 1

  override def availableMoves(grid: Grid, position: Coordinate): Set[(Coordinate, Int)] =
    val hexGrid = HexagonalGrid(grid.size)
    val neighbors = hexGrid.getNeighbors(position)

    val directMoves = neighbors
      .filter(nextPosition => grid.getEntity(nextPosition).isEmpty)
      .map(nextPosition => (nextPosition, singleStepMovementCost))

    val jumpMoves = neighbors.flatMap { neighborPos =>
      grid.getEntity(neighborPos).flatMap { entity =>
        entity.jumpCost.flatMap { jumpCost =>
          val deltaX = neighborPos.x - position.x
          val deltaY = neighborPos.y - position.y
          val jumpTarget = Coordinate(position.x + 2 * deltaX, position.y + 2 * deltaY)

          if grid.isWithinBounds(jumpTarget) && grid.getEntity(jumpTarget).isEmpty then
            Some((jumpTarget, singleStepMovementCost + jumpCost))
          else None
        }
      }
    }

    directMoves ++ jumpMoves
