package it.unibo.tosab.model.engine

import it.unibo.tosab.model.DomainEvent.GameEnded
import it.unibo.tosab.model.GameAction.{Attack, Move, Pass}
import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.engine.EngineRules.standardCombatRules
import it.unibo.tosab.model.entities.EntityId
import it.unibo.tosab.model.{DomainEvent, GameAction, GameState, entities}

/**
  * Result of an engine step.
  *
  * @param nextState state produced after applying an action and post-rules
  * @param events domain events emitted by the transition
  */
case class EngineOutcome(
    nextState: GameState,
    events: Seq[DomainEvent] = Seq.empty
)

/**
  * Player/AI command consumed by the engine.
  *
  * @param actorId id of the acting character
  * @param action requested action
  */
case class CommandIntent(
    actorId: EntityId,
    action: GameAction
)

/** Core game engine contract for turn transitions. */
trait Engine:
  /**
    * Starts a new round by recomputing the turn queue from alive units.
    *
    * @param state current game state
    * @return state with refreshed queue, or `GameOver` when no unit can act
    */
  def startNewRound(state: GameState): GameState =
    TurnOrderManager.determineTurnOrder(state) match
      case Nil          => state.copy(phase = GameOver)
      case newTurnQueue => state.copy(turnQueue = newTurnQueue)

  /**
    * Applies one command intent to the current state.
    *
    * @param state current game state
    * @param intent actor/action intent
    * @return resulting state plus emitted domain events
    */
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
