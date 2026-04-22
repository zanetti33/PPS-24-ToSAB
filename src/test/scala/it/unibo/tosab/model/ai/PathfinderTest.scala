package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.entities.{Entity, EntityId, Faction}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

class PathfinderTest:
  private val unitId = EntityId("unit1")
  private val soldier = Entity.soldier(unitId, Faction.Player)
  private def wall(id: EntityId): Entity = Entity.wall(id)

  @Test def testFindNextStepWithoutObstacles(): Unit =
    val startPos = Coordinate(4, 0)
    val targetPos = Coordinate(4, 2)
    val grid = Grid().setCell(soldier, startPos)

    val nextStep = Pathfinder.findNextStep(grid, startPos, targetPos)
    assertTrue(nextStep.isDefined)
    val step = nextStep.get
    // Should move closer to target (2,0)
    assertEquals(Coordinate(4, 1), step)

  @Test def testFindNextStepWithObstacleInDirectPath(): Unit =
    val obstacle = wall(id = EntityId("wall-1"))
    val startPos = Coordinate(4, 0)
    val blockingPos = Coordinate(4, 1)
    val targetPos = Coordinate(4, 2)
    val grid = Grid()
      .setCell(soldier, startPos)
      .setCell(obstacle, blockingPos)

    val nextStep = Pathfinder.findNextStep(grid, startPos, targetPos)
    assertTrue(nextStep.isDefined)
    val step = nextStep.get
    // Should avoid the obstacle
    assertNotEquals(blockingPos, step)
    // Should pass from (5, 0) => (5, 1) => (4, 2) or (3, 0) => (3, 1) => (4, 2)
    val validSteps = Set(Coordinate(5, 0), Coordinate(3, 0))
    assertTrue(validSteps.contains(step))

  @Test def testFindNextStepToAdjacentTarget(): Unit =
    val startPos = Coordinate(4, 3)
    val targetPos = Coordinate(5, 3)
    val grid = Grid().setCell(soldier, startPos)

    val nextStep = Pathfinder.findNextStep(grid, startPos, targetPos)
    assertEquals(Some(targetPos), nextStep)

  @Test def testFindNextStepWithAllSidesSurroundedByObstacles(): Unit =
    val startPos = Coordinate(4, 4)
    val targetPos = Coordinate(6, 6)
    val grid = Grid()
      .setCell(soldier, startPos)
      // Place obstacles around the soldier to block all adjacent cells
      .setCell(wall(id = EntityId("w1")), Coordinate(3, 3))
      .setCell(wall(id = EntityId("w2")), Coordinate(3, 4))
      .setCell(wall(id = EntityId("w3")), Coordinate(4, 3))
      .setCell(wall(id = EntityId("w4")), Coordinate(4, 5))
      .setCell(wall(id = EntityId("w5")), Coordinate(5, 3))
      .setCell(wall(id = EntityId("w6")), Coordinate(5, 4))

    val nextStep = Pathfinder.findNextStep(grid, startPos, targetPos)
    assertEquals(None, nextStep)

  @Test def testFindNextStepPrefersCellCloserToTarget(): Unit =
    val startPos = Coordinate(4, 0)
    val targetPos = Coordinate(5, 5)
    val grid = Grid().setCell(soldier, startPos)

    val nextStep = Pathfinder.findNextStep(grid, startPos, targetPos)
    assertTrue(nextStep.isDefined)
    val step = nextStep.get
    val distanceFromStep = grid.getDistance(step, targetPos)
    val distanceFromStart = grid.getDistance(startPos, targetPos)
    // The next step should be closer to target than start
    assertTrue(distanceFromStep < distanceFromStart)

  @Test def testBestReachableTowardsTargetUsesMovementDistance(): Unit =
    val startPos = Coordinate(0, 0)
    val targetPos = Coordinate(0, 4)
    val maxSteps = 2
    val expectedBestPosition = Coordinate(0, 2)
    val grid = Grid().setCell(soldier, startPos)

    val bestPosition = Pathfinder.bestReachableTowardsTarget(grid, startPos, targetPos, maxSteps)
    assertEquals(Some(expectedBestPosition), bestPosition)

  @Test def testReachableCellsWithinReturnsEmptyWhenStepsAreNonPositive(): Unit =
    val startPos = Coordinate(2, 2)
    val noSteps = 0
    val grid = Grid().setCell(soldier, startPos)

    val reachable = Pathfinder.reachableCellsWithin(grid, startPos, noSteps)
    assertTrue(reachable.isEmpty)
