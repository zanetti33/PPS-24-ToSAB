package it.unibo.tosab.model.entities

opaque type EntityId = String

object EntityId:
  def apply(s: String): EntityId = s
  def value(id: EntityId): String = id
