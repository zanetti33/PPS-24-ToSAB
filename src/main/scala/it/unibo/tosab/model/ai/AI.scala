package it.unibo.tosab.model.ai

import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.GameState.GameState

object AI:
  /**
   * This trait represents the AI component of the game.
   * It defines a method to determine the next action based on the current game state.
   */
  trait AI:
    def determineNextAction(state: GameState): GameAction
  /**
   * A simple implementation of the AI trait that always returns GameAction.Pass, effectively doing nothing.
   */
  object DummyAI extends AI:
    override def determineNextAction(state: GameState): GameAction = GameAction.Pass
