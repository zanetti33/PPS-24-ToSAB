package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameAction.{Attack, Move, Pass}
import it.unibo.tosab.model.engine.EngineOutcome
import it.unibo.tosab.model.{DomainEvent, GameState, entities}
import it.unibo.tosab.model.entities.CombatRules.{canAttack, resolveAttack}
import it.unibo.tosab.model.entities.{Entity, EntityId, Character, Obstacle}
import it.unibo.tosab.model.grid.Coordinate

/** Helpers that resolve concrete actions into state transitions and domain events. */
object ActionResolvers:
  /** Resolves a pass action by consuming the actor turn. */
  def resolvePass(state: GameState, actorId: EntityId): EngineOutcome =
    EngineOutcome(
      nextState = consumeTurn(state, actorId),
      events = Seq(DomainEvent.ActionApplied(actorId, Pass))
    )

  /** Resolves a move action when the target is reachable within movement distance.
    *
    * @param state
    *   current game state
    * @param actorId
    *   acting character id
    * @param targetPosition
    *   requested destination
    * @return
    *   updated outcome; invalid moves consume turn without moving
    */
  def resolveMove(
      state: GameState,
      actorId: EntityId,
      targetPosition: Coordinate
  ): EngineOutcome =
    val successfulOutcome = for
      actor <- state.getCharacterById(actorId)
      actorPosition <- state.getPositionOf(actorId)
      if validMove(state, targetPosition, actor, actorPosition)
    yield EngineOutcome(
      nextState =
        consumeTurn(state.copy(grid = state.grid.moveEntity(actorId, targetPosition)), actorId),
      events = Seq(DomainEvent.ActionApplied(actorId, Move(targetPosition)))
    )
    // Fallback if any Option is None, or the `if` guard fails
    successfulOutcome.getOrElse(EngineOutcome(nextState = consumeTurn(state, actorId)))

  private def validMove(
      state: GameState,
      targetPosition: Coordinate,
      actor: entities.Character,
      actorPosition: Coordinate
  ) = Pathfinder
    .reachableCellsWithin(
      state.grid,
      actorPosition,
      actor.stats.movementDistance
    )
    .contains(targetPosition)

  private def calculateDamageAndEvents(
      target: Entity,
      updatedTarget: Entity
  ): (Int, Seq[DomainEvent]) =
    val (oldHp, newHp) = (target, updatedTarget) match
      case (targetCharacter: Character, updatedCharacter: Character) =>
        (targetCharacter.stats.currentHp, updatedCharacter.stats.currentHp)
      case (targetObstacle: Obstacle, updatedObstacle: Obstacle) =>
        (targetObstacle.hp.getOrElse(0), updatedObstacle.hp.getOrElse(0))
      case _ => (0, 0)
    val damageAmount = Math.max(0, oldHp - newHp)
    val deathEvents = if newHp <= 0 then Seq(DomainEvent.UnitDied(target.id)) else Seq.empty
    (damageAmount, deathEvents)

  /** Resolves an attack action when attacker and target satisfy combat constraints.
    *
    * @param state
    *   current game state
    * @param actorId
    *   attacker id
    * @param targetId
    *   target entity id
    * @return
    *   updated outcome with action, damage and optional death events
    */
  def resolveAttackAction(
      state: GameState,
      actorId: EntityId,
      targetId: EntityId
  ): EngineOutcome =
    val successfulAttack = for
      attacker <- state.getCharacterById(actorId)
      target <- state.getEntityById(targetId)
      if canAttack(state, attacker, target)
    yield
      val updatedTarget = resolveAttack(attacker, target)
      val (damageAmount, deathEvents) = calculateDamageAndEvents(target, updatedTarget)
      EngineOutcome(
        nextState =
          consumeTurn(state.copy(grid = state.grid.replaceEntity(updatedTarget)), actorId),
        events = Seq(
          DomainEvent.ActionApplied(actorId, Attack(targetId)),
          DomainEvent.DamageInflicted(attacker.id, target.id, damageAmount)
        ) ++ deathEvents
      )
    // Fallback for missing entities or invalid attacks
    successfulAttack.getOrElse(EngineOutcome(nextState = consumeTurn(state, actorId)))

  private def consumeTurn(state: GameState, actorId: EntityId): GameState =
    val updatedQueue = state.turnQueue match
      case currentActor +: remainingTurns if currentActor == actorId => remainingTurns
      case _ => state.turnQueue.filterNot(_ == actorId)
    state.copy(turnQueue = updatedQueue)
