package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, EntityId}

/** Provides methods to query entities on the grid. */
trait EntityQuery:
  /** Gets the position of the given entity. */
  def getPosition(entity: Entity): Option[Coordinate]

  /** Gets the position of the entity with the given ID. */
  def getPosition(entityId: EntityId): Option[Coordinate]

  /** Returns all entities on the grid. */
  def allEntities: List[Entity]

  /** Returns all entities with their positions. */
  def allEntitiesWithPositions: List[(Entity, Coordinate)]

  /** Filters entities based on a predicate. */
  def filterEntities(predicate: Entity => Boolean): Iterable[Entity]

  /** Collects entities using a partial function. */
  def collectEntities[T](pf: PartialFunction[Entity, T]): Iterable[T]

/** Implementation of EntityQuery using a map of coordinates to entities. */
class GridEntityQuery(cells: Map[Coordinate, Entity]) extends EntityQuery:

  override def getPosition(entity: Entity): Option[Coordinate] =
    getPosition(entity.id)

  override def getPosition(entityId: EntityId): Option[Coordinate] =
    cells.find((_, e) => e.id == entityId).map(_._1)

  override def allEntities: List[Entity] = cells.values.toList

  override def allEntitiesWithPositions: List[(Entity, Coordinate)] = cells.toList.map {
    case (pos, entity) => (entity, pos)
  }

  override def filterEntities(predicate: Entity => Boolean): Iterable[Entity] =
    allEntities.filter(predicate)

  override def collectEntities[T](pf: PartialFunction[Entity, T]): Iterable[T] =
    allEntities.collect(pf)
