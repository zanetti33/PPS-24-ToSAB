package it.unibo.tosab.model

import it.unibo.tosab.model.entities.Character
import it.unibo.tosab.model.grid.{Coordinate, Grid}

enum GamePhase:
  case Setup, Combat, GameOver

case class GameState(phase: GamePhase, grid: Grid, turnQueue: Seq[String] = Seq.empty):
  def getCharacterById(id: String): Option[Character] = ???

  def getPositionOf(id: String): Option[Coordinate] = ???

object GameState:
  def apply(grid: Grid): GameState = GameState(GamePhase.Setup, grid)
