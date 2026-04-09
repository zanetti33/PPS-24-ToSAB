package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.Entity

class PlacementManager(navigation: GridManager):
  def isPositionValid(
      entity: Entity,
      pos: Coordinate,
      currentCells: Map[Coordinate, Option[Entity]]
  ): Boolean =
    navigation.isWithinBounds(pos) && currentCells.get(pos).flatten.isEmpty && isRightField(
      entity.isAnEnemy,
      pos
    )

  private def isRightField(enemy: Boolean, pos: Coordinate): Boolean =
    if enemy then pos._1 < 4 else pos._1 >= 4
