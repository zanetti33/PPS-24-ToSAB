package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, EntityId}

trait GridMovement:
  def moveEntity(entityId: EntityId, targetPosition: Coordinate): Grid
  def getAdjacentAvailableCells(entity: Entity): Set[Coordinate]

class HexagonalGridMovement(grid: Grid, gridManager: GridManager) extends GridMovement:

  override def moveEntity(entityId: EntityId, targetPosition: Coordinate): Grid =
    grid.getPosition(entityId) match
      case Some(currentPosition)
          if gridManager.isWithinBounds(targetPosition) && !grid.cells.contains(targetPosition) =>
        grid.cells
          .get(currentPosition)
          .map(entity =>
            grid.copy(cells = grid.cells - currentPosition + (targetPosition -> entity))
          )
          .getOrElse(grid)
      case _ => grid

  override def getAdjacentAvailableCells(entity: Entity): Set[Coordinate] =
    val occupiedCells = grid.getOccupiedCells
    val entityPositions = grid.cells.filter((_, e) => e.id == entity.id).keys.toSet
    entityPositions.flatMap(gridManager.getNeighbors).diff(occupiedCells)
