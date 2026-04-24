package it.unibo.tosab.model.engine

import it.unibo.tosab.model.grid.{Coordinate, Grid}

import scala.collection.immutable.Queue

object Pathfinder:
  private val initialDistanceFromStart = 0
  private val singleStepMovementDistance = 1
  private val movementRules: MovementRules = StandardMovementRules

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
          visited: Set[Coordinate],
          costs: Map[Coordinate, Int]
      ): Set[Coordinate] =
        toVisit.dequeueOption match
          case None => visited - startPos
          case Some(((_, costFromStart), remainingQueue)) if costFromStart > maxSteps =>
            bfs(remainingQueue, visited, costs)
          case Some(((position, costFromStart), remainingQueue)) =>
            val neighbors = movementRules.availableMoves(grid, position)
            val unseenNeighbors = neighbors.filter((c, _) => !visited.contains(c))

            val (newQueue, newCosts, added) =
              unseenNeighbors.foldLeft((remainingQueue, costs, Set.empty[Coordinate])) {
                case ((queue, costMap, addedSet), (neighbor, neighborCost)) =>
                  val newCost = costFromStart + neighborCost

                  if newCost <= maxSteps && newCost < costMap.getOrElse(neighbor, Int.MaxValue) then
                    (
                      queue.enqueue((neighbor, newCost)),
                      costMap + (neighbor -> newCost),
                      addedSet + neighbor
                    )
                  else (queue, costMap, addedSet)
              }

            bfs(newQueue, visited ++ added, newCosts)

      val initialQueue = Queue((startPos, initialDistanceFromStart))
      val initialVisited = Set(startPos)
      val initialCosts = Map(startPos -> initialDistanceFromStart)
      bfs(initialQueue, initialVisited, initialCosts)

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
          position.x,
          position.y
        )
      )

  /** Evaluates adjacent cells and picks the one closest to the target. This is a "Greedy" approach
    */
  def findNextStep(
      grid: Grid,
      startPos: Coordinate,
      targetPos: Coordinate
  ): Option[Coordinate] =
    bestReachableTowardsTarget(grid, startPos, targetPos, singleStepMovementDistance)
