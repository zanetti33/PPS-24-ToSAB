package it.unibo.tosab.model

import it.unibo.tosab.model.entities.{Character, Entity, Faction, EntityId}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

enum GamePhase:
  case Setup, Combat, GameOver

case class GameState(phase: GamePhase, grid: Grid, turnQueue: Seq[EntityId] = Seq.empty):
  def getEntityById(id: EntityId): Option[Entity] = grid.allEntities.find(_.id == id)

  def getCharacterById(id: EntityId): Option[Character] =
    grid.allEntities.find(_.id == id).flatMap {
      case c: Character => Some(c)
      case _            => None
    }

  def getPositionOf(id: EntityId): Option[Coordinate] = grid.getPosition(id)

  private def livingCharacters: Seq[Character] =
    grid.collectEntities { case c: Character if c.stats.currentHp > 0 => c }.toSeq

  def remainingFactions: Set[Faction] = livingCharacters.map(_.faction).toSet

  def winningFaction: Option[Faction] =
    remainingFactions.toSeq match
      case Seq(faction) => Some(faction)
      case _            => None

  def hasWinner: Boolean = winningFaction.nonEmpty

object GameState:
  def apply(grid: Grid): GameState = GameState(GamePhase.Setup, grid)
