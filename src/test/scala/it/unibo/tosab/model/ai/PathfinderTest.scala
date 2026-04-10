package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.entities.{Entity, Faction}
import it.unibo.tosab.model.grid.Grid

class PathfinderTest:
  private val unitId: String = "unit1"
  private val soldier = Entity.soldier(unitId, Faction.Player)
  private def wall(id: String): Entity = Entity.wall(id)

  @Test def testFindNextStepWithoutObstacles(): Unit =
    val startPos = (4, 0)
    val targetPos = (4, 2)
    val grid = Grid().setCell(soldier, startPos)

    val nextStep = Pathfinder.findNextStep(grid, soldier, startPos, targetPos)
    assertTrue(nextStep.isDefined)
    val step = nextStep.get
    // Should move closer to target (2,0)
    assertEquals((4, 1), step)

  @Test def testFindNextStepWithObstacleInDirectPath(): Unit =
    val obstacle = wall(id = "wall-1")
    val startPos = (4, 0)
    val blockingPos = (4, 1)
    val targetPos = (4, 2)
    val grid = Grid()
      .setCell(soldier, startPos)
      .setCell(obstacle, blockingPos)

    val nextStep = Pathfinder.findNextStep(grid, soldier, startPos, targetPos)
    assertTrue(nextStep.isDefined)
    val step = nextStep.get
    // Should avoid the obstacle
    assertNotEquals(blockingPos, step)
    // Should pass from (5, 0) => (5, 1) => (4, 2) or (3, 0) => (3, 1) => (4, 2)
    val validSteps = Set((5, 0), (3, 0))
    assertTrue(validSteps.contains(step))

  @Test def testFindNextStepToAdjacentTarget(): Unit =
    val startPos = (4, 3)
    val targetPos = (5, 3)
    val grid = Grid().setCell(soldier, startPos)

    val nextStep = Pathfinder.findNextStep(grid, soldier, startPos, targetPos)
    assertEquals(Some(targetPos), nextStep)

  @Test def testFindNextStepWithAllSidesSurroundedByObstacles(): Unit =
    val startPos = (4, 4)
    val targetPos = (6, 6)
    val grid = Grid().setCell(soldier, startPos)
      // Place obstacles around the soldier to block all adjacent cells
      .setCell(wall(id = "w1"), (3, 3))
      .setCell(wall(id = "w2"), (3, 4))
      .setCell(wall(id = "w3"), (4, 3))
      .setCell(wall(id = "w4"), (4, 5))
      .setCell(wall(id = "w5"), (5, 3))
      .setCell(wall(id = "w6"), (5, 4))

    val nextStep = Pathfinder.findNextStep(grid, soldier, startPos, targetPos)
    assertEquals(None, nextStep)

  @Test def testFindNextStepPrefersCellCloserToTarget(): Unit =
    val startPos = (4, 0)
    val targetPos = (5, 5)
    val grid = Grid().setCell(soldier, startPos)

    val nextStep = Pathfinder.findNextStep(grid, soldier, startPos, targetPos)
    assertTrue(nextStep.isDefined)
    val step = nextStep.get
    val distanceFromStep = grid.getDistance(step, targetPos)
    val distanceFromStart = grid.getDistance(startPos, targetPos)
    // The next step should be closer to target than start
    assertTrue(distanceFromStep < distanceFromStart)

