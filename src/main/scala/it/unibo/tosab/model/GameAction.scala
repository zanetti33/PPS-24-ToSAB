package it.unibo.tosab.model

import it.unibo.tosab.model.grid.Coordinate

enum GameAction:
  case Move(targetPosition: Coordinate)
  case Attack(targetId: String)
  case Pass
