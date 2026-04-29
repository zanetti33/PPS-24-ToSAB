package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, EntityId}

/** Represents the game grid with entities and managers. */
case class Grid(size: Int, cells: Map[Coordinate, Entity], gridManager: GridManager):
  private val gridPlacement = PlacementManager(gridManager)
  private val lineOfSightManager = HexagonalLineOfSightManager(gridManager)
  private lazy val gridMovement = HexagonalGridMovement(this, gridManager)
  private val obstacleManager = ObstacleManager(this, gridPlacement, size)

  // Core grid operations
  /** Sets an entity at the specified position if valid.
    * @param entity
    *   the entity to place
    * @param position
    *   the position to place the entity
    * @return
    *   the updated grid
    */
  def setCell(entity: Entity, position: Coordinate): Grid =
    if gridPlacement.isPositionValid(entity, position, cells) then
      this.copy(cells = this.cells + (position -> entity))
    else this

  /** Gets the entity at the specified position. */
  def getEntity(position: Coordinate): Option[Entity] =
    cells.get(position)

  /** Gets all occupied cell coordinates. */
  def getOccupiedCells: Set[Coordinate] =
    cells.keySet

  /** Gets all cells with entities. */
  def getAllCells: Map[Coordinate, Entity] = cells

  /** Calculates distance between two coordinates.
    * @param p1
    *   first coordinate
    * @param p2
    *   second coordinate
    * @return
    *   the distance
    */
  def getDistance(p1: Coordinate, p2: Coordinate): Int =
    gridManager.getDistance(p1, p2)

  /** Checks if a position is within grid bounds. */
  def isWithinBounds(position: Coordinate): Boolean =
    gridManager.isWithinBounds(position)

  /** Gets the position of the given entity. */
  def getPosition(entity: Entity): Option[Coordinate] =
    GridEntityQuery(cells).getPosition(entity)

  /** Gets the position of the entity with the given ID. */
  def getPosition(entityId: EntityId): Option[Coordinate] =
    GridEntityQuery(cells).getPosition(entityId)

  /** Returns all entities on the grid. */
  def allEntities: List[Entity] =
    GridEntityQuery(cells).allEntities

  /** Returns all entities with their positions. */
  def allEntitiesWithPositions: List[(Entity, Coordinate)] =
    GridEntityQuery(cells).allEntitiesWithPositions

  /** Filters entities based on a predicate.
    * @param predicate
    *   the filter predicate
    * @return
    *   iterable of matching entities
    */
  def filterEntities(predicate: Entity => Boolean): Iterable[Entity] =
    GridEntityQuery(cells).filterEntities(predicate)

  /** Collects entities using a partial function.
    * @param pf
    *   the partial function
    * @return
    *   iterable of collected results
    */
  def collectEntities[T](pf: PartialFunction[Entity, T]): Iterable[T] =
    GridEntityQuery(cells).collectEntities(pf)

  // Entity modification
  /** Replaces an entity with an updated version.
    * @param updatedEntity
    *   the updated entity
    * @return
    *   the updated grid
    */
  def replaceEntity(updatedEntity: Entity): Grid =
    getPosition(updatedEntity.id)
      .map(position => copy(cells = cells.updated(position, updatedEntity)))
      .getOrElse(this)

  /** Removes an entity by ID.
    * @return
    *   the updated grid
    */
  def removeEntity(entityId: EntityId): Grid =
    getPosition(entityId)
      .map(position => copy(cells = cells - position))
      .getOrElse(this)

  /** Removes entities matching a predicate.
    * @return
    *   the updated grid
    */
  def removeEntities(predicate: Entity => Boolean): Grid =
    copy(cells = cells.filterNot((_, entity) => predicate(entity)))

  /** Moves an entity to a target position.
    * @param entityId
    *   the entity ID to move
    * @param targetPosition
    *   the target position
    * @return
    *   the updated grid
    */
  def moveEntity(entityId: EntityId, targetPosition: Coordinate): Grid =
    gridMovement.moveEntity(entityId, targetPosition)

  /** Gets adjacent available cells for an entity. */
  def getAdjacentAvailableCells(entity: Entity): Set[Coordinate] =
    gridMovement.getAdjacentAvailableCells(entity)

  /** Checks if line of sight is clear between two positions.
    * @param from
    *   starting position
    * @param to
    *   ending position
    * @return
    *   true if clear
    */
  def isLineOfSightClear(from: Coordinate, to: Coordinate): Boolean =
    lineOfSightManager.isLineOfSightClear(from, to, getOccupiedCells, getEntity)

  /** Places obstacles on the grid.
    * @return
    *   the updated grid with obstacles
    */
  def placeObstacles(): Grid =
    obstacleManager.placeObstacles()

/** Companion object for Grid providing factory methods. */
object Grid:
  /** Creates a new Grid with default parameters.
    * @param size
    *   grid size
    * @param cells
    *   initial cells
    * @param gridManager
    *   grid manager
    * @return
    *   new Grid instance
    */
  def apply(
      size: Int = 8,
      cells: Map[Coordinate, Entity] = Map.empty,
      gridManager: GridManager = HexagonalGrid(8)
  ): Grid =
    new Grid(size, cells, gridManager)
