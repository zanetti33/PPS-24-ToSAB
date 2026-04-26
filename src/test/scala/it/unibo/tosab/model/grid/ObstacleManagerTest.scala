package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class ObstacleManagerTest:

  @Test def testPlaceObstacles(): Unit =
    val grid = GridFactory.createHexagonal(8).placeObstacles()
    val occupiedCells = grid.getOccupiedCells
    assertTrue(occupiedCells.nonEmpty)
    for cell <- occupiedCells do
      val entity = grid.getEntity(cell)
      assertTrue(entity.exists(_.isInstanceOf[Obstacle]))

  @Test def testPlaceObstaclesRandomCount(): Unit =
    val grid = GridFactory.createHexagonal(8).placeObstacles()
    assertTrue(grid.getOccupiedCells.nonEmpty)
    assertTrue(grid.getOccupiedCells.size <= 8)
