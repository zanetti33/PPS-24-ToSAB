package it.unibo.tosab.model

import it.unibo.tosab.model.grid.Grid
import it.unibo.tosab.model.{GamePhase, GameState}
import org.junit.*
import org.junit.Assert.*

class GameStateTest:

  @Test def gameStateInitializationDefaultsToSetup(): Unit =
    val grid = Grid()
    val state = GameState(grid)
    assertEquals(GamePhase.Setup, state.phase)
    assertEquals(grid, state.grid)
