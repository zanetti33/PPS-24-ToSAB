package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameState
import it.unibo.tosab.model.entities.Character

object TurnOrderManager:
  def determineTurnOrder(gameState: GameState): Seq[String] =
    gameState.grid.allEntitiesWithPositions
      .collect { case (character: Character, pos) => (character, pos)}
      .sortBy { case (character, (row, column)) =>
        (
          -character.stats.speed,
          row,
          column
        )
      }
      .map { case (character, _) => character.id }
