package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*

class PlacementManager(gridSurface: GridManager):
  private val factionSplitRow = gridSurface.size / 2

  def isPositionValid(
      entity: Entity,
      pos: Coordinate,
      currentCells: Map[Coordinate, Entity]
  ): Boolean =
    gridSurface.isWithinBounds(pos) && !currentCells.contains(pos) && isRightField(entity, pos)

  def generateRandomPosition(grid: Grid): Coordinate =
    val pos = Coordinate(scala.util.Random.nextInt(grid.size), scala.util.Random.nextInt(grid.size))
    if !grid.cells.contains(pos) then pos
    else generateRandomPosition(grid)

  private def isRightField(entity: Entity, pos: Coordinate): Boolean = entity match
    case c: Character =>
      if c.isAnEnemy then pos.x < factionSplitRow
      else pos.x >= factionSplitRow
    case _ => true
