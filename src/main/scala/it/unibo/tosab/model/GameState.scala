package it.unibo.tosab.model

import it.unibo.tosab.model.grid.Grid

object GameState:
  enum GamePhase:
    case Setup
    case Combat
    case GameOver
  
  case class GameState(phase: GamePhase, grid: Grid)