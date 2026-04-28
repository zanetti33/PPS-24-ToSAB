package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GamePhase.GameOver
import it.unibo.tosab.model.entities.{Character, Obstacle}
import it.unibo.tosab.model.entities.CombatRules.{isDefeated, isDestroyed}

/** Reusable post-action rules for engine pipelines. */
object EngineRules:
  /** Removes defeated characters and destroyed obstacles from the grid. */
  val removeDefeated: EngineRule = outcome =>
    val cleanedGrid = outcome.nextState.grid.removeEntities {
      case character: Character => character.isDefeated
      case obstacle: Obstacle   => obstacle.isDestroyed
    }
    outcome.copy(nextState = outcome.nextState.copy(grid = cleanedGrid))

  /** Drops turn-queue ids that no longer map to living characters. */
  val synchronizeTurnQueue: EngineRule = outcome =>
    val synchronizedTurnQueue = TurnOrderManager.synchronizeTurnQueue(outcome.nextState)
    outcome.copy(nextState = outcome.nextState.copy(turnQueue = synchronizedTurnQueue))

  /** Switches to `GameOver` when a winner is present. */
  val checkGameOver: EngineRule = outcome =>
    outcome.nextState match
      case nextState if nextState.hasWinner =>
        outcome.copy(nextState = outcome.nextState.copy(phase = GameOver, turnQueue = Seq.empty))
      case _ => outcome

  /** Standard rule chain for combat turns. */
  val standardCombatRules: Seq[EngineRule] =
    Seq(removeDefeated, synchronizeTurnQueue, checkGameOver)
