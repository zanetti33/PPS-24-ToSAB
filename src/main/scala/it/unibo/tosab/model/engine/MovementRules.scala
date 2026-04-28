package it.unibo.tosab.model.engine

import it.unibo.tosab.model.grid.{Coordinate, Grid, HexagonalGrid}

/** Strategy for computing legal movement options from a position. */
trait MovementRules:
  /**
    * Computes available moves from a position with their movement costs.
    *
    * @param grid current grid
    * @param position origin position
    * @return set of reachable neighbors `(coordinate, cost)`
    */
  def availableMoves(grid: Grid, position: Coordinate): Set[(Coordinate, Int)]

/** Default movement rules: step on empty neighbors or jump over entities with jump cost. */
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
