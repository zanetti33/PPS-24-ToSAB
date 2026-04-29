package it.unibo.tosab.model.entities

/** Unique identifier for game entities.
  */
opaque type EntityId = String

object EntityId:
  /** Creates an EntityId from a String. */
  def apply(s: String): EntityId = s

  /** Extracts the String value from an EntityId. */
  def value(id: EntityId): String = id
