package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, EntityId}

trait EntityQuery:
  def getPosition(entity: Entity): Option[Coordinate]
  def getPosition(entityId: EntityId): Option[Coordinate]
  def allEntities: List[Entity]
  def allEntitiesWithPositions: List[(Entity, Coordinate)]
  def filterEntities(predicate: Entity => Boolean): Iterable[Entity]
  def collectEntities[T](pf: PartialFunction[Entity, T]): Iterable[T]

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
