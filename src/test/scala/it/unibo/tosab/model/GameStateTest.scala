package it.unibo.tosab.model

import it.unibo.tosab.model.entities.{Entity, EntityId, Faction}
import it.unibo.tosab.model.grid.{Coordinate, Grid}
import it.unibo.tosab.model.{GamePhase, GameState}
import org.junit.*
import org.junit.Assert.*

class GameStateTest:

  private val playerFaction = Faction.Player
  private val soldierId = EntityId("p1")
  private val soldierPosition = Coordinate(4, 0)
  private val soldier = Entity.soldier(soldierId, playerFaction)
  private val mageId = EntityId("m1")
  private val unknownId = EntityId("unknown")
  private val obstacleId = EntityId("obs-1")
  private val wallPosition = Coordinate(4, 1)
  private val wall = Entity.wall(obstacleId)
  private val magePosition = Coordinate(6, 2)
  private val mage = Entity.mage(mageId, playerFaction)
  private val enemyId = EntityId("enemy-1")
  private val enemyPosition = Coordinate(1, 1)
  private val enemy = Entity.soldier(enemyId, Faction.AI)

  @Test def testGameStateInitializationDefaultsToSetup(): Unit =
    val grid = Grid()
    val state = GameState(grid)
    assertEquals(GamePhase.Setup, state.phase)
    assertEquals(grid, state.grid)

  @Test def testGetCharacterByIdReturnsCharacterWhenPresent(): Unit =
    val grid = Grid()
      .setCell(soldier, soldierPosition)
    val state = GameState(grid)
    assertEquals(Some(soldier), state.getCharacterById(soldierId))

  @Test def testGetCharacterByIdReturnsNoneWhenMissing(): Unit =
    val grid = Grid()
      .setCell(soldier, soldierPosition)
    val state = GameState(grid)
    assertEquals(None, state.getCharacterById(unknownId))

  @Test def testGetCharacterByIdIgnoresObstacles(): Unit =
    val grid = Grid()
      .setCell(wall, wallPosition)
    val state = GameState(grid)
    assertEquals(None, state.getCharacterById(obstacleId))

  @Test def testGetPositionOfReturnsCoordinateWhenCharacterPresent(): Unit =
    val grid = Grid()
      .setCell(mage, magePosition)
    val state = GameState(grid)
    assertEquals(Some(magePosition), state.getPositionOf(mageId))

  @Test def testGetPositionOfReturnsNoneWhenMissing(): Unit =
    val grid = Grid()
    val state = GameState(grid)
    assertEquals(None, state.getPositionOf(unknownId))

  @Test def testRemainingFactionsContainsBothSidesWhenAlive(): Unit =
    val grid = Grid()
      .setCell(soldier, soldierPosition)
      .setCell(enemy, enemyPosition)
    val state = GameState(GamePhase.Combat, grid)

    assertEquals(Set(Faction.Player, Faction.AI), state.remainingFactions)
    assertEquals(None, state.winningFaction)

  @Test def testWinningFactionReturnsSingleAliveFaction(): Unit =
    val state = GameState(GamePhase.Combat, Grid().setCell(soldier, soldierPosition))

    assertEquals(Some(Faction.Player), state.winningFaction)
    assertTrue(state.hasWinner)
