package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*

import scala.annotation.tailrec

class PlacementManager(navigation: GridManager):
  def isPositionValid(
      entity: Entity,
      pos: Coordinate,
      currentCells: Map[Coordinate, Entity]
  ): Boolean =
    navigation.isWithinBounds(pos) && !currentCells.contains(pos) && isRightField(
      entity,
      pos
    )

  def generateRandomPosition(grid: Grid): Coordinate =
    val pos = (scala.util.Random.nextInt(grid.size), scala.util.Random.nextInt(grid.size))
    if !grid.cells.contains(pos) then pos
    else generateRandomPosition(grid)

  private def isRightField(entity: Entity, pos: Coordinate): Boolean = entity match
    case c: Character => if c.isAnEnemy then pos._1 < 4 else pos._1 >= 4
    case _            => true
