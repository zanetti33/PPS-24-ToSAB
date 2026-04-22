package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GamePhase.GameOver
import it.unibo.tosab.model.entities.{Character, Obstacle}
import it.unibo.tosab.model.entities.CombatRules.{isDefeated, isDestroyed}

object EngineRules:
  val removeDefeated: EngineRule = outcome =>
    val cleanedGrid = outcome.nextState.grid.removeEntities {
      case character: Character => character.isDefeated
      case obstacle: Obstacle   => obstacle.isDestroyed
    }
    outcome.copy(nextState = outcome.nextState.copy(grid = cleanedGrid))

  val synchronizeTurnQueue: EngineRule = outcome =>
    val synchronizedTurnQueue = TurnOrderManager.synchronizeTurnQueue(outcome.nextState)
    outcome.copy(nextState = outcome.nextState.copy(turnQueue = synchronizedTurnQueue))


  val checkGameOver: EngineRule = outcome => outcome.nextState match
    case nextState if nextState.hasWinner => outcome.copy(nextState = outcome.nextState.copy(phase = GameOver, turnQueue = Seq.empty))
    case _ => outcome

  val standardCombatRules: Seq[EngineRule] =
    Seq(removeDefeated, synchronizeTurnQueue, checkGameOver)
  
