package it.unibo.tosab.model.ai

import it.unibo.tosab.model.{GameAction, GameState}
import it.unibo.tosab.model.entities.Character
import it.unibo.tosab.model.grid.Coordinate

object Behaviors:

  /** Returns all enemy characters on the grid paired with their positions. */
  private def enemiesWithPositions(state: GameState, me: Character): Seq[(Character, Coordinate)] =
    state.grid.getOccupiedCells.toSeq
      .flatMap(pos => state.grid.getEntity(pos).map(e => (e, pos)))
      .collect { case (enemy: Character, pos) if enemy.faction != me.faction => (enemy, pos) }

  /** Returns the enemy closest to `myPos`, if any. */
  private def closestEnemy(state: GameState, me: Character, myPos: Coordinate): Option[(Character, Coordinate)] =
    enemiesWithPositions(state, me)
      .minByOption((_, enemyPos) => state.grid.getDistance(myPos, enemyPos))

  /** Returns true when `target` is within the attacker's attack range. */
  private def isInRange(state: GameState, from: Coordinate, to: Coordinate, range: Int): Boolean =
    state.grid.getDistance(from, to) <= range

  /** Attacks the closest enemy if it is within attack range, otherwise returns None. */
  val attackClosestEnemy: Behavior = (state, me, myPos) =>
    closestEnemy(state, me, myPos)
      .filter((_, enemyPos) => isInRange(state, myPos, enemyPos, me.stats.attackRange))
      .map((enemy, _) => GameAction.Attack(enemy.id))

  /** Moves one step towards the closest enemy if reachable, otherwise returns None. */
  val moveTowardsClosestEnemy: Behavior = (state, me, myPos) =>
    closestEnemy(state, me, myPos)
      .flatMap((_, enemyPos) => Pathfinder.findNextStep(state.grid, me, myPos, enemyPos))
      .map(GameAction.Move(_))
