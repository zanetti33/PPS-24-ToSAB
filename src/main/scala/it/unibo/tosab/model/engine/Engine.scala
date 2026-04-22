package it.unibo.tosab.model.engine

import it.unibo.tosab.model.DomainEvent.GameEnded
import it.unibo.tosab.model.GameAction.{Attack, Move, Pass}
import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.engine.EngineRules.standardCombatRules
import it.unibo.tosab.model.{DomainEvent, GameAction, GameState, entities}
import it.unibo.tosab.model.entities.CombatRules.{canAttack, resolveAttack}
import it.unibo.tosab.model.entities.EntityId
import it.unibo.tosab.model.grid.Coordinate

case class EngineOutcome(
    nextState: GameState,
    events: Seq[DomainEvent] = Seq.empty
)

case class CommandIntent(
    actorId: EntityId,
    action: GameAction
)

/** The Engine trait defines the core logic of the game, responsible for applying actions to the
  * game state. It takes the current game state, the actor id and a game action, and returns the new
  * game state
  */
trait Engine:
  def startNewRound(state: GameState): GameState =
    TurnOrderManager.determineTurnOrder(state) match
      case Nil          => state.copy(phase = GameOver)
      case newTurnQueue => state.copy(turnQueue = newTurnQueue)

  def applyUnitAction(state: GameState, intent: CommandIntent): EngineOutcome

object Engine:
  /** A simple implementation of the Engine trait that does not modify the game state
    */
  object DoesNothingEngine extends Engine:
    override def applyUnitAction(
        state: GameState,
        intent: CommandIntent
    ): EngineOutcome =
      EngineOutcome(nextState = state)

  /** A simple implementation of the Engine trait that ends the game immediately by setting the
    * phase to GameOver, regardless of the action taken.
    */
  object ImmediatelyEndEngine extends Engine:
    override def applyUnitAction(
        state: GameState,
        intent: CommandIntent
    ): EngineOutcome =
      val endState = state.copy(phase = GameOver)
      EngineOutcome(
        nextState = endState,
        events = Seq(GameEnded(endState))
      )

  /** Initial concrete engine for turn-based combat. Handles attack resolution, defeated-unit
    * cleanup, turn consumption and game-over detection.
    */
  object TurnBasedCombatEngine extends PipelineEngine(standardCombatRules):
    override def resolveAction(
        state: GameState,
        actorId: EntityId,
        action: GameAction
    ): EngineOutcome = action match
      case Pass                 => ActionResolvers.resolvePass(state, actorId)
      case Move(targetPosition) => ActionResolvers.resolveMove(state, actorId, targetPosition)
      case Attack(targetId)     => ActionResolvers.resolveAttackAction(state, actorId, targetId)
