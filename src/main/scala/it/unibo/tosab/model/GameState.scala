package it.unibo.tosab.model

import it.unibo.tosab.model.entities.{Character, Entity, Faction, EntityId}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

/** Represents the current phase of the game. */
enum GamePhase:
  case Setup, Combat, GameOver

/** Represents the current state of the game including the grid, phase, and turn queue.
  *
  * @param phase
  *   the current game phase
  * @param grid
  *   the game grid containing all entities
  * @param turnQueue
  *   the sequence of entity IDs representing turn order
  */
case class GameState(phase: GamePhase, grid: Grid, turnQueue: Seq[EntityId] = Seq.empty):
  /** Retrieves an entity from the grid by its ID. */
  def getEntityById(id: EntityId): Option[Entity] = grid.allEntities.find(_.id == id)

  /** Retrieves a character from the grid by its ID. */
  def getCharacterById(id: EntityId): Option[Character] =
    grid.allEntities.find(_.id == id).flatMap {
      case c: Character => Some(c)
      case _            => None
    }

  /** Retrieves the position of an entity on the grid. */
  def getPositionOf(id: EntityId): Option[Coordinate] = grid.getPosition(id)

  private def livingCharacters: Seq[Character] =
    grid.collectEntities { case c: Character if c.stats.currentHp > 0 => c }.toSeq

  /** Returns the set of factions that still have living characters. */
  def remainingFactions: Set[Faction] = livingCharacters.map(_.faction).toSet

  /** Determines if one faction has won the game. */
  def winningFaction: Option[Faction] =
    remainingFactions.toSeq match
      case Seq(faction) => Some(faction)
      case _            => None

  /** Checks if the game has a winner (only one faction remains). */
  def hasWinner: Boolean = winningFaction.nonEmpty

  /** Checks if the grid has changed between two game states. */
  def gridChanged(other: GameState): Boolean =
    this.grid.getOccupiedCells != other.grid.getOccupiedCells

object GameState:
  /** Creates a new GameState in the Setup phase with the given grid. */
  def apply(grid: Grid): GameState = GameState(GamePhase.Setup, grid)
