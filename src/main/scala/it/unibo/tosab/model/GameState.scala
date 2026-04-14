package it.unibo.tosab.model

import it.unibo.tosab.model.entities.Character
import it.unibo.tosab.model.grid.{Coordinate, Grid}

enum GamePhase:
  case Setup, Combat, GameOver

case class GameState(phase: GamePhase, grid: Grid, turnQueue: Seq[String] = Seq.empty):
  def getCharacterById(id: String): Option[Character] = grid.allEntities.find(_.id == id).flatMap {
    case c: Character => Some(c)
    case _            => None
  }

  def getPositionOf(id: String): Option[Coordinate] = grid.getPosition(id)

object GameState:
  def apply(grid: Grid): GameState = GameState(GamePhase.Setup, grid)
