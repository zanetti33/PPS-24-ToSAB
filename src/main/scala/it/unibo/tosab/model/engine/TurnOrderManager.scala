package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.Entity

object TurnOrderManager:
  def determineTurnOrder(gameState: GameState): Seq[String] = gameState.turnQueue