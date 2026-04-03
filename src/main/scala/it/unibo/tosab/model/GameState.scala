package it.unibo.tosab.model

import it.unibo.tosab.model.grid.Grid

enum GamePhase:
  case Setup, Combat, GameOver

case class GameState(phase: GamePhase, grid: Grid)

object GameState:
  def apply(grid: Grid): GameState = GameState(GamePhase.Setup, grid)
