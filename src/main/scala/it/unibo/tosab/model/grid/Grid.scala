package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.{Character, Entity, Faction, Obstacle}

type Coordinate = (Int, Int)

class Grid(val size: Int = 8):
  private val grid = HexagonalGrid(size)
  private val gridPlacement = PlacementManager(grid)

  private var cells: Map[Coordinate, Option[Entity]] =
    (for { x <- 0 until size; y <- 0 until size } yield (x, y) -> None).toMap

  // def placeObstacles

  def setCell(entity: Entity, position: Coordinate): Unit =
    if gridPlacement.isPositionValid(entity, position, cells) then
      cells = cells + (position -> Some(entity))
    else println(s"Cell $position is not valid for this entity.")

  def getEntity(position: Coordinate): Option[Entity] =
    if grid.isWithinBounds(position) then cells(position) else None

  def getOccupiedCells: Set[Coordinate] =
    // Verifica che cells non sia vuota o non contenga riferimenti nulli
    cells.filter(_._2.isDefined).keySet

  def getDistance(p1: Coordinate, p2: Coordinate): Int =
    grid.getDistance(p1, p2)

  def getAdjacentAvailableCells(entity: Entity): Set[Coordinate] =
    val occupiedCells = getOccupiedCells
    val entityID = entity.id
    val entityPositions = cells.filter((_, e) => e.exists(_.id == entityID)).keys.toSet
    entityPositions.flatMap(grid.getNeighbors).diff(occupiedCells)
