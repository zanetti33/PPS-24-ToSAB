package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GameAction.{Attack, Move, Pass}
import it.unibo.tosab.model.GamePhase.Combat
import it.unibo.tosab.model.{DomainEvent, GameAction, GameState, entities}
import it.unibo.tosab.model.entities.{CombatRules, EntityId}
import it.unibo.tosab.model.entities.CombatRules.{canAttack, resolveAttack}

type EngineRule = EngineOutcome => EngineOutcome

trait PipelineEngine(rules: Seq[EngineRule]) extends Engine:
  private val postActionPipeline: EngineRule = rules.reduceLeft(_ andThen _)

  override def applyUnitAction(
      state: GameState,
      intent: CommandIntent
  ): EngineOutcome = state match
    case GameState(Combat, _, _) => postActionPipeline(resolveAction(state, intent.actorId, intent.action))
    case _ => EngineOutcome(nextState = state)

  protected def resolveAction(
      state: GameState,
      actorId: EntityId,
      action: GameAction
  ): EngineOutcome
