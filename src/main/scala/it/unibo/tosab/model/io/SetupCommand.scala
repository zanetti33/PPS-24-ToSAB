package it.unibo.tosab.model.io

import it.unibo.tosab.model.entities.Role
import it.unibo.tosab.model.grid.Coordinate

enum SetupCommand:
  case AddTroop(roleId: Role, position: Coordinate)
  case StartGame
  case Invalid(error: String)
