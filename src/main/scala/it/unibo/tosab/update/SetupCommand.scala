package it.unibo.tosab.update

import it.unibo.tosab.model.entities.Role
import it.unibo.tosab.model.grid.Coordinate

/** Represents commands parsed from user input during the game setup phase.
  */
enum SetupCommand:
  /** Places a troop of the specified role at the given position. */
  case AddTroop(roleId: Role, position: Coordinate)

  /** Signals the end of the setup phase and starts the game. */
  case StartGame

  /** Represents an invalid or unparseable command. */
  case Invalid(error: String)
