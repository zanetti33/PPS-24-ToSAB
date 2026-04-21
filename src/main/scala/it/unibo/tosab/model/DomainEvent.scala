package it.unibo.tosab.model

import it.unibo.tosab.model.grid.Grid
import it.unibo.tosab.model.entities.EntityId

/** Domain events shared across engine and game loop.
  */
enum DomainEvent:
  case ActionApplied(actorId: EntityId, action: GameAction)
  case DamageInflicted(attackerId: EntityId, targetId: EntityId, amount: Int)
  case UnitDied(unitId: EntityId)
  case GridUpdated(grid: Grid)
  case GameEnded(finalState: GameState)
