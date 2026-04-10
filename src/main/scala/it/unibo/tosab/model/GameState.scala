package it.unibo.tosab.model

import it.unibo.tosab.model.entities.Entity
import it.unibo.tosab.model.grid.{Coordinate, Grid}

enum GamePhase:
  case Setup, Combat, GameOver

case class GameState(phase: GamePhase, grid: Grid, turnQueue: Seq[String] = Seq.empty)

object GameState:
  def apply(grid: Grid): GameState = GameState(GamePhase.Setup, grid)
