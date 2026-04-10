package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.{GameAction, GameState}

object Engine:
  /** The Engine trait defines the core logic of the game, responsible for applying actions to the
    * game state. It takes the current game state, the and a game action, and returns the new game state
    */
  trait Engine:
    def startNewRound(state: GameState): GameState =
      val newTurnQueue = TurnOrderManager.determineTurnOrder(state)
      state.copy(turnQueue = newTurnQueue)
    def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState

  /** A simple implementation of the Engine trait that does not modify the game state
    */
  object DoesNothingEngine extends Engine:
    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState = state

  /** A simple implementation of the Engine trait that ends the game immediately by setting the
    * phase to GameOver, regardless of the action taken.
    */
  object ImmediatelyEndEngine extends Engine:
    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState =
      state.copy(phase = GameOver)
