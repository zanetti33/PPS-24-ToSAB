package it.unibo.tosab.model

import it.unibo.tosab.model.entities.{Character, Entity, Faction}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

enum GamePhase:
  case Setup, Combat, GameOver

case class GameState(phase: GamePhase, grid: Grid, turnQueue: Seq[String] = Seq.empty):
  def getEntityById(id: String): Option[Entity] = grid.allEntities.find(_.id == id)

  def getCharacterById(id: String): Option[Character] = grid.allEntities.find(_.id == id).flatMap {
    case c: Character => Some(c)
    case _            => None
  }

  def getPositionOf(id: String): Option[Coordinate] = grid.getPosition(id)

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
