package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameAction.{Attack, Move, Pass}
import it.unibo.tosab.model.GamePhase.*
import it.unibo.tosab.model.{DomainEvent, GameAction, GameState}
import it.unibo.tosab.model.entities.CombatRules.{canAttack, resolveAttack}

object Engine:
  case class EngineOutcome(
      nextState: GameState,
      events: Seq[DomainEvent] = Seq.empty
  )

  /** The Engine trait defines the core logic of the game, responsible for applying actions to the
    * game state. It takes the current game state, the actor id and a game action, and returns the
    * new game state
    */
  trait Engine:
    def startNewRound(state: GameState): GameState =
      TurnOrderManager.determineTurnOrder(state) match
        case Nil          => state.copy(phase = GameOver)
        case newTurnQueue => state.copy(turnQueue = newTurnQueue)
    def applyUnitAction(state: GameState, actorId: String, action: GameAction): EngineOutcome

  /** A simple implementation of the Engine trait that does not modify the game state
    */
  object DoesNothingEngine extends Engine:
    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): EngineOutcome =
      EngineOutcome(nextState = state)

  /** A simple implementation of the Engine trait that ends the game immediately by setting the
    * phase to GameOver, regardless of the action taken.
    */
  object ImmediatelyEndEngine extends Engine:
    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): EngineOutcome =
      EngineOutcome(
        nextState = state.copy(phase = GameOver),
        events = Seq(DomainEvent.ActionApplied(actorId, action))
      )

  /** Initial concrete engine for turn-based combat. Handles attack resolution, defeated-unit
    * cleanup, turn consumption and game-over detection.
    */
  object TurnBasedCombatEngine extends Engine:
    override def startNewRound(state: GameState): GameState =
      CombatStateTransitions.startRoundOrEnd(state)

    override def applyUnitAction(state: GameState, actorId: String, action: GameAction): EngineOutcome =
      if state.phase != Combat then EngineOutcome(nextState = state)
      else
        val intermediateOutcome = action match
          case Pass                 => resolvePass(state, actorId)
          case Move(targetPosition) => resolveMove(state, actorId, targetPosition)
          case Attack(targetId)     => resolveAttackAction(state, actorId, targetId)
        intermediateOutcome.copy(
          nextState = CombatStateTransitions.finalizeCombatState(intermediateOutcome.nextState)
        )

    private def resolvePass(state: GameState, actorId: String): EngineOutcome =
      EngineOutcome(
        nextState = consumeTurn(state, actorId),
        events = Seq(DomainEvent.ActionApplied(actorId, Pass))
      )

    private def resolveMove(
        state: GameState,
        actorId: String,
        targetPosition: (Int, Int)
    ): EngineOutcome =
      (state.getCharacterById(actorId), state.getPositionOf(actorId)) match
        case (Some(actor), Some(actorPosition))
            if state.grid.getDistance(actorPosition, targetPosition) <= actor.stats.movementDistance
              && state.grid.getEntity(targetPosition).isEmpty
              && state.grid.isWithinBounds(targetPosition) =>
          EngineOutcome(
            nextState = consumeTurn(state.copy(grid = state.grid.moveEntity(actorId, targetPosition)), actorId),
            events = Seq(DomainEvent.ActionApplied(actorId, Move(targetPosition)))
          )
        case _ => EngineOutcome(nextState = consumeTurn(state, actorId))

    private def resolveAttackAction(
        state: GameState,
        actorId: String,
        targetId: String
    ): EngineOutcome =
      (state.getCharacterById(actorId), state.getCharacterById(targetId)) match
        case (Some(attacker), Some(target)) if canAttack(state, attacker, target) =>
          val updatedTarget = resolveAttack(attacker, target)
          val damageAmount = Math.max(0, target.stats.currentHp - updatedTarget.stats.currentHp)
          val deathEvents =
            if updatedTarget.stats.currentHp <= 0 then Seq(DomainEvent.UnitDied(target.id))
            else Seq.empty
          EngineOutcome(
            nextState = consumeTurn(state.copy(grid = state.grid.replaceEntity(updatedTarget)), actorId),
            events = Seq(
              DomainEvent.ActionApplied(actorId, Attack(targetId)),
              DomainEvent.DamageInflicted(attacker.id, target.id, damageAmount)
            ) ++ deathEvents
          )
        case _ => EngineOutcome(nextState = consumeTurn(state, actorId))

    private def consumeTurn(state: GameState, actorId: String): GameState =
      val updatedQueue = state.turnQueue match
        case currentActor +: remainingTurns if currentActor == actorId => remainingTurns
        case _ => state.turnQueue.filterNot(_ == actorId)
      state.copy(turnQueue = updatedQueue)
