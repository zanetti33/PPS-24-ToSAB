package it.unibo.tosab.model

import it.unibo.tosab.model.entities.{Entity, Faction}
import it.unibo.tosab.model.grid.Grid
import it.unibo.tosab.model.{GamePhase, GameState}
import org.junit.*
import org.junit.Assert.*

class GameStateTest:

  private val playerFaction = Faction.Player
  private val soldierId = "p1"
  private val soldierPosition = (0, 0)
  private val soldier = Entity.soldier(soldierId, playerFaction)
  private val mageId = "m1"
  private val unknownId = "unknown"
  private val obstacleId = "obs-1"
  private val wallPosition = (1, 1)
  private val wall = Entity.wall(obstacleId)
  private val magePosition = (2, 2)
  private val mage = Entity.mage(mageId, playerFaction)

  @Test def testGameStateInitializationDefaultsToSetup(): Unit =
    val grid = Grid()
    val state = GameState(grid)
    assertEquals(GamePhase.Setup, state.phase)
    assertEquals(grid, state.grid)

  @Test def testGetCharacterByIdReturnsCharacterWhenPresent(): Unit =
    val grid = Grid()
    grid.setCell(soldier, soldierPosition)
    val state = GameState(grid)
    assertEquals(Some(soldier), state.getCharacterById(soldierId))

  @Test def testGetCharacterByIdReturnsNoneWhenMissing(): Unit =
    val grid = Grid()
    grid.setCell(soldier, soldierPosition)
    val state = GameState(grid)
    assertEquals(None, state.getCharacterById(unknownId))

  @Test def testGetCharacterByIdIgnoresObstacles(): Unit =
    val grid = Grid()
    grid.setCell(wall, wallPosition)
    val state = GameState(grid)
    assertEquals(None, state.getCharacterById(obstacleId))

  @Test def testGetPositionOfReturnsCoordinateWhenCharacterPresent(): Unit =
    val grid = Grid()
    grid.setCell(mage, magePosition)
    val state = GameState(grid)
    assertEquals(Some(magePosition), state.getPositionOf(mageId))

  @Test def testGetPositionOfReturnsNoneWhenMissing(): Unit =
    val grid = Grid()
    val state = GameState(grid)
    assertEquals(None, state.getPositionOf(unknownId))
