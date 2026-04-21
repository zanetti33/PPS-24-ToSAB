package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.CombatRules.isAlive
import it.unibo.tosab.model.entities.Character
import it.unibo.tosab.model.entities.EntityId
import it.unibo.tosab.model.grid.Coordinate

object TurnOrderManager:
  private def livingCharacterIds(gameState: GameState): Set[EntityId] =
    gameState.grid.allEntities.collect {
      case character: Character if character.isAlive => character.id
    }.toSet

  def synchronizeTurnQueue(gameState: GameState): Seq[EntityId] =
    val aliveIds = livingCharacterIds(gameState)
    gameState.turnQueue.filter(aliveIds)

  def determineTurnOrder(gameState: GameState): Seq[EntityId] =
    gameState.grid.allEntitiesWithPositions
      .collect { case (character: Character, pos) if character.isAlive => (character, pos) }
      .sortBy { case (character, Coordinate(row, column)) =>
        (
          -character.stats.speed,
          row,
          column
        )
      }
      .map { case (character, _) => character.id }
