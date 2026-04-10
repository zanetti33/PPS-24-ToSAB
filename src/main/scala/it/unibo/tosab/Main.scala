package it.unibo.tosab
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.*
import it.unibo.tosab.model.entities.*
import it.unibo.tosab.model.io.MonadIO
import it.unibo.tosab.update.GameSetup
import it.unibo.tosab.view.DisplayGrid

@main def runApp(): Unit =
  val initialGrid = Grid().placeObstacles()

  val setupProgram: MonadIO[Map[Coordinate, Option[Entity]]] = GameSetup.runSetupLoop(grid.getCells)

  val initialGrid = setupProgram.run()

  println("[INIT] Inizializzazione GameState globale...")
  val initialState = GameState(GamePhase.Setup, initialGrid)

  println("\n[STATO GIOCO SPRINT 1] Setup completato con successo!\n")
  DisplayGrid.displayInitialGrid(initialState.grid)

  val wizard = Entity.mage("wizard", Faction.Player)
  val soldier = Entity.soldier("soldier", Faction.AI)
  val soldier2 = Entity.soldier("soldier", Faction.AI)
  val updatedGrid = initialGrid
    .setCell(wizard, (5, 5))
    .setCell(soldier, (1, 4))
    .setCell(soldier, (2, 4))
    .setCell(soldier2, (3, 4))
  val updatedState = initialState.copy(grid = updatedGrid)
  DisplayGrid.display(updatedState.grid)
