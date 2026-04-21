package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, EntityId}

type Coordinate = (Int, Int)

case class Grid(size: Int = 8, cells: Map[Coordinate, Entity] = Map.empty):
  private val hexGrid = HexagonalGrid(size)
  private val gridPlacement = PlacementManager(hexGrid)

  def setCell(entity: Entity, position: Coordinate): Grid =
    if gridPlacement.isPositionValid(entity, position, cells) then
      this.copy(cells = this.cells + (position -> entity))
    else this

  def getEntity(position: Coordinate): Option[Entity] =
    cells.get(position)

  def getPosition(entity: Entity): Option[Coordinate] =
    getPosition(entity.id)

  def getPosition(entityId: EntityId): Option[Coordinate] =
    cells.find((_, e) => e.id == entityId).map(_._1)

  def getOccupiedCells: Set[Coordinate] =
    cells.keySet

  def getDistance(p1: Coordinate, p2: Coordinate): Int =
    hexGrid.getDistance(p1, p2)

  def isWithinBounds(position: Coordinate): Boolean =
    hexGrid.isWithinBounds(position)

  def getAdjacentAvailableCells(entity: Entity): Set[Coordinate] =
    val occupiedCells = getOccupiedCells
    val entityPositions = cells.filter((_, e) => e.id == entity.id).keys.toSet
    entityPositions.flatMap(hexGrid.getNeighbors).diff(occupiedCells)

  def getAllCells: Map[Coordinate, Entity] = cells

  def allEntities: List[Entity] = cells.values.toList

  def allEntitiesWithPositions: List[(Entity, Coordinate)] = cells.toList.map {
    case (pos, entity) => (entity, pos)
  }

  def filterEntities(predicate: Entity => Boolean): Iterable[Entity] =
    allEntities.filter(predicate)

  def collectEntities[T](pf: PartialFunction[Entity, T]): Iterable[T] =
    allEntities.collect(pf)

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

  def moveEntity(entityId: EntityId, targetPosition: Coordinate): Grid =
    getPosition(entityId) match
      case Some(currentPosition)
          if isWithinBounds(targetPosition) && !cells.contains(targetPosition) =>
        cells
          .get(currentPosition)
          .map(entity => copy(cells = cells - currentPosition + (targetPosition -> entity)))
          .getOrElse(this)
      case _ => this

  def placeObstacles(): Grid =
    val random = scala.util.Random.nextInt(size)
    var grid: Grid = this
    for i <- 0 to random do
      val pos = gridPlacement.generateRandomPosition(grid)
      val obstacle = scala.util.Random.nextInt(4) match
        case 0 => Entity.wall("wall")
        case 1 => Entity.bush("bush")
        case 2 => Entity.tree("tree")
        case _ => Entity.rock("rock")
      grid = grid.setCell(obstacle, pos)
    grid
