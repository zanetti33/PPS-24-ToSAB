package it.unibo.tosab.model

import it.unibo.tosab.model.grid.Grid

/** Domain events shared across engine and game loop.
  */
enum DomainEvent:
  case ActionApplied(actorId: String, action: GameAction)
  case DamageInflicted(attackerId: String, targetId: String, amount: Int)
  case UnitDied(unitId: String)
  case GridUpdated(grid: Grid)
  case GameEnded(finalState: GameState)

