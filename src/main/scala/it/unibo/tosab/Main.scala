package it.unibo.tosab
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.*
import it.unibo.tosab.model.entities.*
import it.unibo.tosab.view.DisplayGrid

@main def runApp(): Unit =
  val grid = Grid()
  val wizard = Entity.mage("wizard", Faction.Player)
  val soldier = Entity.soldier("soldier", Faction.AI)
  val soldier2 = Entity.soldier("soldier", Faction.AI)
  grid.setCell(wizard, (5, 5))
  grid.setCell(soldier, (1, 4))
  grid.setCell(soldier, (2, 4))
  grid.setCell(soldier2, (3, 4))
  // da controllare numerazione truppe sia se ne creo due uguali che se ne creo due diversi,
  // se è possibile creare due truppe uguali e se è possibile creare due truppe diverse con lo stesso id

  println("[INIT] Inizializzazione GameState globale...")
  val initialState = GameState(GamePhase.Setup, grid)

  println("\n[STATO GIOCO SPRINT 1] Setup completato con successo!\n")
  DisplayGrid.display(initialState.grid)
