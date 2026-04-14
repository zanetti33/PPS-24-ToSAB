package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GamePhase.GameOver
import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.CombatRules.{isDefeated, isDestroyed}
import it.unibo.tosab.model.entities.{Character, Obstacle}

object CombatStateTransitions:
  private val EmptyTurnQueue: Seq[String] = Seq.empty

  def startRoundOrEnd(state: GameState): GameState =
    val cleanedState = finalizeCombatState(state)
    cleanedState.winningFaction match
      case Some(_) => toGameOver(cleanedState)
      case None =>
        TurnOrderManager.determineTurnOrder(cleanedState) match
          case Nil          => toGameOver(cleanedState)
          case newTurnQueue => cleanedState.copy(turnQueue = newTurnQueue)

  def finalizeCombatState(state: GameState): GameState =
    val cleanedState = removeDefeatedEntities(state)
    val synchronizedState = synchronizeTurnQueue(cleanedState)

    if synchronizedState.hasWinner then toGameOver(synchronizedState)
    else synchronizedState

  private def removeDefeatedEntities(state: GameState): GameState =
    val cleanedGrid = state.grid.removeEntities {
      case character: Character => character.isDefeated
      case obstacle: Obstacle   => obstacle.isDestroyed
    }
    state.copy(grid = cleanedGrid)

  private def synchronizeTurnQueue(state: GameState): GameState =
    state.copy(turnQueue = TurnOrderManager.synchronizeTurnQueue(state))

  private def toGameOver(state: GameState): GameState =
    state.copy(phase = GameOver, turnQueue = EmptyTurnQueue)

