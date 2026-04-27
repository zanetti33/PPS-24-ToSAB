package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.*
import org.junit.*
import org.junit.Assert.*

class ObstacleManagerTest:

  val grid: Grid = GridFactory.createHexagonal(8)

  @Test def testPlaceObstacles(): Unit =
    val updatedGrid = grid.placeObstacles()
    val occupiedCells = updatedGrid.getOccupiedCells
    assertTrue(occupiedCells.nonEmpty)
    for cell <- occupiedCells do
      val entity = updatedGrid.getEntity(cell)
      assertTrue(entity.exists(_.isInstanceOf[Obstacle]))

  @Test def testPlaceObstaclesRandomCount(): Unit =
    val updatedGrid = grid.placeObstacles()
    assertTrue(updatedGrid.getOccupiedCells.nonEmpty)
    assertTrue(updatedGrid.getOccupiedCells.size <= 8)
