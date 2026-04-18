package it.unibo.tosab.model.ai

import scala.collection.immutable.Queue
import it.unibo.tosab.model.entities.Entity
import it.unibo.tosab.model.grid.{Coordinate, Grid}
import it.unibo.tosab.model.grid.HexagonalGrid

object Pathfinder:
  private val initialDistanceFromStart = 0
  private val singleStepMovementDistance = 1

  private def traversableNeighbors(grid: Grid, position: Coordinate): Set[Coordinate] =
    val hexGrid = HexagonalGrid(grid.size)
    hexGrid
      .getNeighbors(position)
      .filter(nextPosition => grid.getEntity(nextPosition).isEmpty)

  /** Returns all cells reachable from `startPos` within `maxSteps` steps. */
  def reachableCellsWithin(
      grid: Grid,
      startPos: Coordinate,
      maxSteps: Int
  ): Set[Coordinate] =
    if maxSteps <= initialDistanceFromStart then Set.empty
    else
      @annotation.tailrec
      def bfs(
          toVisit: Queue[(Coordinate, Int)],
          visited: Set[Coordinate]
      ): Set[Coordinate] =
        toVisit.dequeueOption match
          case None => visited - startPos
          case Some(((position, stepsFromStart), remainingQueue)) if stepsFromStart >= maxSteps =>
            bfs(remainingQueue, visited)
          case Some(((position, stepsFromStart), remainingQueue)) =>
            val unseenNeighbors = traversableNeighbors(grid, position).diff(visited)
            val nextDistance = stepsFromStart + singleStepMovementDistance
            val queueWithNeighbors = unseenNeighbors.foldLeft(remainingQueue)((queue, neighbor) =>
              queue.enqueue((neighbor, nextDistance))
            )
            bfs(queueWithNeighbors, visited ++ unseenNeighbors)

      val initialQueue = Queue((startPos, initialDistanceFromStart))
      val initialVisited = Set(startPos)
      bfs(initialQueue, initialVisited)

  /** Picks the reachable cell within `maxSteps` that minimizes distance to `targetPos`. */
  def bestReachableTowardsTarget(
      grid: Grid,
      startPos: Coordinate,
      targetPos: Coordinate,
      maxSteps: Int
  ): Option[Coordinate] =
    reachableCellsWithin(grid, startPos, maxSteps)
      .minByOption(position =>
        (
          grid.getDistance(position, targetPos),
          grid.getDistance(startPos, position),
          position._1,
          position._2
        )
      )

  /** Evaluates adjacent cells and picks the one closest to the target. This is a "Greedy" approach
    */
  def findNextStep(
      grid: Grid,
      entity: Entity,
      startPos: Coordinate,
      targetPos: Coordinate
  ): Option[Coordinate] =
    bestReachableTowardsTarget(grid, startPos, targetPos, singleStepMovementDistance)
