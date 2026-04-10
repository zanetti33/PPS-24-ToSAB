package it.unibo.tosab
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.*
import it.unibo.tosab.model.entities.*
import it.unibo.tosab.model.io.MonadIO
import it.unibo.tosab.update.GameSetup
import it.unibo.tosab.view.DisplayGrid

@main def runApp(): Unit =
  val initialGrid = Grid().placeObstacles()

  println("[INIT] Inizializzazione GameState globale...")
  val initialState = GameState(GamePhase.Setup, initialGrid)

  println("\n[STATO GIOCO SPRINT 1] Setup completato con successo!\n")
  DisplayGrid.displayInitialGrid(initialState.grid)

  val setupProgram = GameSetup.runSetupLoop(initialGrid)
  val startingGrid = setupProgram.run()
  val updatedState = initialState.copy(grid = startingGrid)
  DisplayGrid.display(updatedState.grid)
