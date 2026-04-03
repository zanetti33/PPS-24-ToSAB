package it.unibo.tosab
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.*

@main def runApp(): Unit =
  println("==============================")
  println(" TOWN OF SAVIOM: AUTO-BATTLER ")
  println("==============================")

  println("[INIT] Generazione Griglia 8x8...")
  val initialGrid = Grid()

  println("[INIT] Creazione Unità in corso...")
  initialGrid.setCell("archer", (0, 2))
  initialGrid.setCell("soldier", (3, 2))
  initialGrid.setCell("wizard", (4, 5))

  println("[INIT] Inizializzazione GameState globale...")
  val initialState = GameState(GamePhase.Setup, initialGrid)

  println("\n[STATO GIOCO SPRINT 1] Setup completato con successo!\n")

  DisplayGrid.display(initialState.grid)
