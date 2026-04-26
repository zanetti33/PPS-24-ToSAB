package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, Obstacle}

trait LineOfSightManager:
  def isLineOfSightClear(
      from: Coordinate,
      to: Coordinate,
      occupiedCells: Set[Coordinate],
      getEntity: Coordinate => Option[Entity]
  ): Boolean

class HexagonalLineOfSightManager(gridManager: GridManager) extends LineOfSightManager:

  private def isInLine(from: Coordinate, to: Coordinate): Boolean =
    if from == to then true
    else from.x == to.x || gridManager.getDiagonal(from, to).contains(to)

  private def cellsInBetween(from: Coordinate, to: Coordinate): Set[Coordinate] =
    if from.x == to.x then (from.y until to.y).map(y => Coordinate(from.x, y)).toSet
    else gridManager.getDiagonal(from, to)

  override def isLineOfSightClear(
      from: Coordinate,
      to: Coordinate,
      occupiedCells: Set[Coordinate],
      getEntity: Coordinate => Option[Entity]
  ): Boolean =
    if !isInLine(from, to) then true
    else
      val blockingObstacles = occupiedCells
        .intersect(cellsInBetween(from, to))
        .filter(pos =>
          getEntity(pos).exists {
            case o: Obstacle => o.blocksVision
            case _           => false
          }
        )
      blockingObstacles.isEmpty
