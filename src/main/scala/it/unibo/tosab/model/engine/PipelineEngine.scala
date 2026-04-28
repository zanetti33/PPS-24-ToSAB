package it.unibo.tosab.model.engine

import it.unibo.tosab.model.GamePhase.Combat
import it.unibo.tosab.model.{GameAction, GameState}
import it.unibo.tosab.model.entities.EntityId

/** Post-processing rule applied to an `EngineOutcome`. */
type EngineRule = EngineOutcome => EngineOutcome

/** Engine base that resolves an action then applies a pipeline of post-action rules. Rules are
  * executed only during `Combat` phase.
  */
trait PipelineEngine(rules: Seq[EngineRule]) extends Engine:
  private val postActionPipeline: EngineRule = rules.reduceLeft(_ andThen _)

  override def applyUnitAction(
      state: GameState,
      intent: CommandIntent
  ): EngineOutcome = state match
    case GameState(Combat, _, _) =>
      postActionPipeline(resolveAction(state, intent.actorId, intent.action))
    case _ => EngineOutcome(nextState = state)

  /** Resolves the game action before post-rules are applied.
    *
    * @param state
    *   current game state
    * @param actorId
    *   acting character id
    * @param action
    *   action to resolve
    * @return
    *   raw outcome for the action
    */
  protected def resolveAction(
      state: GameState,
      actorId: EntityId,
      action: GameAction
  ): EngineOutcome
