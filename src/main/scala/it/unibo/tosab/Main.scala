package it.unibo.tosab
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.*
import it.unibo.tosab.model.entities.*
import it.unibo.tosab.model.io.MonadIO
import it.unibo.tosab.update.GameSetup
import it.unibo.tosab.view.DisplayGrid

@main def runApp(): Unit =
  val startingGrid = Grid().placeObstacles()

  println("[INIT] Inizializzazione GameState globale...")
  val initialState = GameState(GamePhase.Setup, startingGrid)

  println("\n[STATO GIOCO SPRINT 2] Setup completato con successo!\n")
  DisplayGrid.displayInitialGrid(initialState.grid)

  val setupProgram = GameSetup.runSetupLoop(startingGrid)
  val updatedGrid = setupProgram.run()
  val updatedState = initialState.copy(grid = updatedGrid)
  DisplayGrid.display(updatedState.grid)
