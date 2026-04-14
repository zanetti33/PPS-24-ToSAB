package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.CombatRules.isAlive
import it.unibo.tosab.model.entities.Character

object TurnOrderManager:
  private def livingCharacterIds(gameState: GameState): Set[String] =
    gameState.grid.allEntities
      .collect { case character: Character if character.isAlive => character.id }
      .toSet

  def synchronizeTurnQueue(gameState: GameState): Seq[String] =
    val aliveIds = livingCharacterIds(gameState)
    gameState.turnQueue.filter(aliveIds)

  def determineTurnOrder(gameState: GameState): Seq[String] =
    gameState.grid.allEntitiesWithPositions
      .collect { case (character: Character, pos) if character.isAlive => (character, pos) }
      .sortBy { case (character, (row, column)) =>
        (
          -character.stats.speed,
          row,
          column
        )
      }
      .map { case (character, _) => character.id }
