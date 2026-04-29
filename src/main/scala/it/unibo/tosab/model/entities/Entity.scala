package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.ObstacleType.{Bush, Tree, Wall, Rock}

/** Represents the faction of an entity in the game.
  */
enum Faction:
  case Player, AI

/** Represents the role of a character.
  */
enum Role:
  case Archer, Soldier, Mage

/** Represents different types of obstacles on the game grid.
  */
enum ObstacleType:
  case Bush, Wall, Tree, Rock

/** Base trait for all game entities.
  */
sealed trait Entity:
  /** Unique identifier for the entity. */
  def id: EntityId

  /** Cost to jump over this entity. None means not jumpable. */
  def jumpCost: Option[Int] = None

/** Represents a playable character in the game.
  *
  * @param id
  *   unique identifier
  * @param faction
  *   the controlling faction
  * @param role
  *   the character's role/class
  * @param stats
  *   combat and movement statistics
  */
case class Character(id: EntityId, faction: Faction, role: Role, stats: Stats) extends Entity

/** Represents an obstacle on the game grid.
  *
  * @param id
  *   unique identifier
  * @param obstacleType
  *   the type of obstacle
  * @param hp
  *   health points (None for indestructible)
  * @param isPassable
  *   whether entities can walk through it
  * @param blocksVision
  *   whether it blocks ranged attacks
  */
case class Obstacle(
    id: EntityId,
    obstacleType: ObstacleType,
    hp: Option[Int],
    isPassable: Boolean,
    blocksVision: Boolean
) extends Entity:
  // For simplicity, we assume that all passable obstacles have a jump cost of 1,
  // while non-passable obstacles cannot be jumped over.
  // in the future, we could have more difficult to traverse obstacles
  override def jumpCost: Option[Int] = if isPassable then Some(1) else None

object Entity:

  /** Check if a character is an enemy. */
  extension (c: Character) def isAnEnemy: Boolean = c.faction == Faction.AI

  /** Creates an archer character with base stats. */
  def archer(id: EntityId, faction: Faction): Character =
    Character(id, faction, Role.Archer, Stats.baseArcherStats)

  /** Creates a soldier character with base stats. */
  def soldier(id: EntityId, faction: Faction): Character =
    Character(id, faction, Role.Soldier, Stats.baseSoldierStats)

  /** Creates a mage character with base stats. */
  def mage(id: EntityId, faction: Faction): Character =
    Character(id, faction, Role.Mage, Stats.baseMageStats)

  /** Creates a bush obstacle. */
  def bush(id: EntityId): Obstacle =
    Obstacle(id, Bush, hp = Some(40), isPassable = true, blocksVision = false)

  /** Creates a wall obstacle. */
  def wall(id: EntityId): Obstacle =
    Obstacle(id, Wall, hp = None, isPassable = false, blocksVision = true)

  /** Creates a tree obstacle. */
  def tree(id: EntityId): Obstacle =
    Obstacle(id, Tree, hp = Some(100), isPassable = false, blocksVision = true)

  /** Creates a rock obstacle. */
  def rock(id: EntityId): Obstacle =
    Obstacle(id, Rock, hp = None, isPassable = true, blocksVision = true)
