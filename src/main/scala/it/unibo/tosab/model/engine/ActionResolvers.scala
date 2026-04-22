package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameAction.{Attack, Move, Pass}
import it.unibo.tosab.model.engine.EngineOutcome
import it.unibo.tosab.model.{DomainEvent, GameState, entities}
import it.unibo.tosab.model.entities.CombatRules.{canAttack, resolveAttack}
import it.unibo.tosab.model.entities.EntityId
import it.unibo.tosab.model.grid.Coordinate

object ActionResolvers:
  def resolvePass(state: GameState, actorId: EntityId): EngineOutcome =
    EngineOutcome(
      nextState = consumeTurn(state, actorId),
      events = Seq(DomainEvent.ActionApplied(actorId, Pass))
    )

  def resolveMove(
                           state: GameState,
                           actorId: EntityId,
                           targetPosition: Coordinate
                         ): EngineOutcome =
    (state.getCharacterById(actorId), state.getPositionOf(actorId)) match
      case (Some(actor), Some(actorPosition))
        if validMove(state, targetPosition, actor, actorPosition) =>
        EngineOutcome(
          nextState = consumeTurn(
            state.copy(grid = state.grid.moveEntity(actorId, targetPosition)),
            actorId
          ),
          events = Seq(DomainEvent.ActionApplied(actorId, Move(targetPosition)))
        )
      case _ => EngineOutcome(nextState = consumeTurn(state, actorId))

  private def validMove(state: GameState, targetPosition: Coordinate, actor: entities.Character, actorPosition: Coordinate) =
    state.grid.getDistance(actorPosition, targetPosition) <= actor.stats.movementDistance
      && state.grid.getEntity(targetPosition).isEmpty
      && state.grid.isWithinBounds(targetPosition)

  def resolveAttackAction(
                                   state: GameState,
                                   actorId: EntityId,
                                   targetId: EntityId
                                 ): EngineOutcome =
    (state.getCharacterById(actorId), state.getCharacterById(targetId)) match
      case (Some(attacker), Some(target)) if canAttack(state, attacker, target) =>
        val updatedTarget = resolveAttack(attacker, target)
        val damageAmount = Math.max(0, target.stats.currentHp - updatedTarget.stats.currentHp)
        val deathEvents =
          if updatedTarget.stats.currentHp <= 0 then Seq(DomainEvent.UnitDied(target.id))
          else Seq.empty
        EngineOutcome(
          nextState =
            consumeTurn(state.copy(grid = state.grid.replaceEntity(updatedTarget)), actorId),
          events = Seq(
            DomainEvent.ActionApplied(actorId, Attack(targetId)),
            DomainEvent.DamageInflicted(attacker.id, target.id, damageAmount)
          ) ++ deathEvents
        )
      case _ => EngineOutcome(nextState = consumeTurn(state, actorId))

  private def consumeTurn(state: GameState, actorId: EntityId): GameState =
    val updatedQueue = state.turnQueue match
      case currentActor +: remainingTurns if currentActor == actorId => remainingTurns
      case _ => state.turnQueue.filterNot(_ == actorId)
    state.copy(turnQueue = updatedQueue)