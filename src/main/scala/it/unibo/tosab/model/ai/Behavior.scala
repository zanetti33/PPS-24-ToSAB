package it.unibo.tosab.model.ai

import it.unibo.tosab.model.{GameAction, GameState}
import it.unibo.tosab.model.grid.Coordinate
import it.unibo.tosab.model.entities.Character

/**
  * Decision function used by configurable AI.
  *
  * @param state current game state
  * @param actor controlled character
  * @param actorPosition current position of the controlled character
  * @return the chosen action, or `None` when this behavior cannot decide
  */
type Behavior = (GameState, Character, Coordinate) => Option[GameAction]
