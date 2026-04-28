package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.CombatRules.isAlive
import it.unibo.tosab.model.entities.Character
import it.unibo.tosab.model.entities.EntityId
import it.unibo.tosab.model.grid.Coordinate

/** Utilities for building and maintaining the combat turn queue. */
object TurnOrderManager:
  private def livingCharacterIds(gameState: GameState): Set[EntityId] =
    gameState.grid.allEntities.collect {
      case character: Character if character.isAlive => character.id
    }.toSet

  /** Removes dead/missing actors from the current queue.
    *
    * @param gameState
    *   current game state
    * @return
    *   queue containing only alive character ids
    */
  def synchronizeTurnQueue(gameState: GameState): Seq[EntityId] =
    val aliveIds = livingCharacterIds(gameState)
    gameState.turnQueue.filter(aliveIds)

  /** Computes turn order by speed descending, then stable position tie-breakers.
    *
    * @param gameState
    *   current game state
    * @return
    *   ordered sequence of acting character ids
    */
  def determineTurnOrder(gameState: GameState): Seq[EntityId] =
    gameState.grid.allEntitiesWithPositions
      .collect { case (character: Character, pos) if character.isAlive => (character, pos) }
      .sortBy { case (character: Character, pos) =>
        (
          -character.stats.speed,
          pos.x,
          pos.y
        )
      }
      .map { case (character, _) => character.id }
