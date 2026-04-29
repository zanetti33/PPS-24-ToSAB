package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*

/** Manages entity placement rules. */
class PlacementManager(gridSurface: GridManager):
  private val factionSplitRow = gridSurface.size / 2

  /** Checks if a position is valid for an entity.
    * @param entity
    *   the entity to place
    * @param pos
    *   the position to check
    * @param currentCells
    *   current cells map
    * @return
    *   true if valid
    */
  def isPositionValid(
      entity: Entity,
      pos: Coordinate,
      currentCells: Map[Coordinate, Entity]
  ): Boolean =
    gridSurface.isWithinBounds(pos) && !currentCells.contains(pos) && isRightField(entity, pos)

  /** Generates a random valid position.
    * @param grid
    *   the grid
    * @return
    *   a valid coordinate
    */
  def generateRandomPosition(grid: Grid): Coordinate =
    val pos = Coordinate(scala.util.Random.nextInt(grid.size), scala.util.Random.nextInt(grid.size))
    if !grid.cells.contains(pos) then pos
    else generateRandomPosition(grid)

  private def isRightField(entity: Entity, pos: Coordinate): Boolean = entity match
    case c: Character =>
      if c.isAnEnemy then pos.x < factionSplitRow
      else pos.x >= factionSplitRow
    case _ => true
