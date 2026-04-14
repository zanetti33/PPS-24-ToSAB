package it.unibo.tosab.model.ai

import it.unibo.tosab.model.{GameAction, GameState}
import it.unibo.tosab.model.grid.Coordinate
import it.unibo.tosab.model.entities.Character

type Behavior = (GameState, Character, Coordinate) => Option[GameAction]
