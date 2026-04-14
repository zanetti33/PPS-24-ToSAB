package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameAction.{Attack, Move, Pass}
import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.{GameAction, GameState}
import it.unibo.tosab.model.entities.CombatRules.{canAttack, resolveAttack}

object Engine:
  /** The Engine trait defines the core logic of the game, responsible for applying actions to the
    * game state. It takes the current game state, the actor id and a game action, and returns the
    * new game state
    */
  trait Engine:
    def startNewRound(state: GameState): GameState =
      TurnOrderManager.determineTurnOrder(state) match
        case Nil          => state.copy(phase = GameOver)
        case newTurnQueue => state.copy(turnQueue = newTurnQueue)
    def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState

  /** A simple implementation of the Engine trait that does not modify the game state
    */
  object DoesNothingEngine extends Engine:
    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState =
      state

  /** A simple implementation of the Engine trait that ends the game immediately by setting the
   * phase to GameOver, regardless of the action taken.
   */
  object ImmediatelyEndEngine extends Engine:
    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState =
      state.copy(phase = GameOver)
  
  /** Initial concrete engine for turn-based combat. Handles attack resolution, defeated-unit cleanup,
    * turn consumption and game-over detection.
    */
  object TurnBasedCombatEngine extends Engine:
    override def startNewRound(state: GameState): GameState =
      CombatStateTransitions.startRoundOrEnd(state)

    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState =
      if state.phase != Combat then state
      else
        val nextState = action match
          case Pass                 => consumeTurn(state, actorId)
          case Move(targetPosition) => resolveMove(state, actorId, targetPosition)
          case Attack(targetId)     => resolveAttackAction(state, actorId, targetId)
        CombatStateTransitions.finalizeCombatState(nextState)

    private def resolveMove(state: GameState, actorId: String, targetPosition: (Int, Int)): GameState =
      (state.getCharacterById(actorId), state.getPositionOf(actorId)) match
        case (Some(actor), Some(actorPosition))
            if state.grid.getDistance(actorPosition, targetPosition) <= actor.stats.movementDistance
              && state.grid.getEntity(targetPosition).isEmpty
              && state.grid.isWithinBounds(targetPosition) =>
          consumeTurn(state.copy(grid = state.grid.moveEntity(actorId, targetPosition)), actorId)
        case _ => consumeTurn(state, actorId)

    private def resolveAttackAction(state: GameState, actorId: String, targetId: String): GameState =
      (state.getCharacterById(actorId), state.getCharacterById(targetId)) match
        case (Some(attacker), Some(target)) if canAttack(state, attacker, target) =>
          val updatedTarget = resolveAttack(attacker, target)
          consumeTurn(state.copy(grid = state.grid.replaceEntity(updatedTarget)), actorId)
        case _ => consumeTurn(state, actorId)

    private def consumeTurn(state: GameState, actorId: String): GameState =
      val updatedQueue = state.turnQueue match
        case currentActor +: remainingTurns if currentActor == actorId => remainingTurns
        case _                                                         => state.turnQueue.filterNot(_ == actorId)
      state.copy(turnQueue = updatedQueue)

