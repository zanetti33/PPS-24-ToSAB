package it.unibo.tosab.model.engine

import it.unibo.tosab.model.grid.{Coordinate, Grid}

import scala.collection.immutable.Queue

/** Pathfinding helpers for movement and target approach decisions. */
object Pathfinder:
  private val initialDistanceFromStart = 0
  private val singleStepMovementDistance = 1
  private val movementRules: MovementRules = StandardMovementRules

  /**
    * Returns all cells reachable from `startPos` within `maxSteps` movement points.
    *
    * @param grid current grid
    * @param startPos origin position
    * @param maxSteps movement budget
    * @return reachable coordinates excluding `startPos`
    */
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

  /**
    * Picks the reachable cell that best approaches `targetPos` within `maxSteps`.
    *
    * @param grid current grid
    * @param startPos origin position
    * @param targetPos desired target
    * @param maxSteps movement budget
    * @return best reachable position, if any
    */
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

  /**
    * Greedy one-step version of `bestReachableTowardsTarget`.
    *
    * @param grid current grid
    * @param startPos origin position
    * @param targetPos desired target
    * @return best adjacent step toward target, if available
    */
  def findNextStep(
      grid: Grid,
      startPos: Coordinate,
      targetPos: Coordinate
  ): Option[Coordinate] =
    bestReachableTowardsTarget(grid, startPos, targetPos, singleStepMovementDistance)
