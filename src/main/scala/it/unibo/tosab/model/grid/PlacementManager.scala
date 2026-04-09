package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*

class PlacementManager(navigation: GridManager):
  def isPositionValid(
      entity: Entity,
      pos: Coordinate,
      currentCells: Map[Coordinate, Option[Entity]]
  ): Boolean =
    navigation.isWithinBounds(pos) && currentCells.get(pos).flatten.isEmpty && isRightField(
      entity,
      pos
    )

  private def isRightField(entity: Entity, pos: Coordinate): Boolean = entity match
    case c: Character => if c.isAnEnemy then pos._1 < 4 else pos._1 >= 4
    case _            => true
