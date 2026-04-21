package it.unibo.tosab.model

import it.unibo.tosab.model.grid.Coordinate
import it.unibo.tosab.model.entities.EntityId

enum GameAction:
  case Move(targetPosition: Coordinate)
  case Attack(targetId: EntityId)
  case Pass
