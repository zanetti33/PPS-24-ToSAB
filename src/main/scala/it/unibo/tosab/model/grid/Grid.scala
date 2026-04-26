package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, EntityId}

case class Grid(size: Int, cells: Map[Coordinate, Entity], gridManager: GridManager):
  private val gridPlacement = PlacementManager(gridManager)
  private val lineOfSightManager = HexagonalLineOfSightManager(gridManager)
  private lazy val gridMovement = HexagonalGridMovement(this, gridManager)
  private val obstacleManager = ObstacleManager(this, gridPlacement, size)

  // Core grid operations
  def setCell(entity: Entity, position: Coordinate): Grid =
    if gridPlacement.isPositionValid(entity, position, cells) then
      this.copy(cells = this.cells + (position -> entity))
    else this

  def getEntity(position: Coordinate): Option[Entity] =
    cells.get(position)

  def getOccupiedCells: Set[Coordinate] =
    cells.keySet

  def getAllCells: Map[Coordinate, Entity] = cells

  def getDistance(p1: Coordinate, p2: Coordinate): Int =
    gridManager.getDistance(p1, p2)

  def isWithinBounds(position: Coordinate): Boolean =
    gridManager.isWithinBounds(position)

  // Entity query delegation
  def getPosition(entity: Entity): Option[Coordinate] =
    GridEntityQuery(cells).getPosition(entity)

  def getPosition(entityId: EntityId): Option[Coordinate] =
    GridEntityQuery(cells).getPosition(entityId)

  def allEntities: List[Entity] =
    GridEntityQuery(cells).allEntities

  def allEntitiesWithPositions: List[(Entity, Coordinate)] =
    GridEntityQuery(cells).allEntitiesWithPositions

  def filterEntities(predicate: Entity => Boolean): Iterable[Entity] =
    GridEntityQuery(cells).filterEntities(predicate)

  def collectEntities[T](pf: PartialFunction[Entity, T]): Iterable[T] =
    GridEntityQuery(cells).collectEntities(pf)

  // Entity modification
  def replaceEntity(updatedEntity: Entity): Grid =
    getPosition(updatedEntity.id)
      .map(position => copy(cells = cells.updated(position, updatedEntity)))
      .getOrElse(this)

  def removeEntity(entityId: EntityId): Grid =
    getPosition(entityId)
      .map(position => copy(cells = cells - position))
      .getOrElse(this)

  def removeEntities(predicate: Entity => Boolean): Grid =
    copy(cells = cells.filterNot((_, entity) => predicate(entity)))

  // Movement and vision delegation
  def moveEntity(entityId: EntityId, targetPosition: Coordinate): Grid =
    gridMovement.moveEntity(entityId, targetPosition)

  def getAdjacentAvailableCells(entity: Entity): Set[Coordinate] =
    gridMovement.getAdjacentAvailableCells(entity)

  def isLineOfSightClear(from: Coordinate, to: Coordinate): Boolean =
    lineOfSightManager.isLineOfSightClear(from, to, getOccupiedCells, getEntity)

  // Obstacle management delegation
  def placeObstacles(): Grid =
    obstacleManager.placeObstacles()

object Grid:
  def apply(
      size: Int = 8,
      cells: Map[Coordinate, Entity] = Map.empty,
      gridManager: GridManager = HexagonalGrid(8)
  ): Grid =
    new Grid(size, cells, gridManager)
