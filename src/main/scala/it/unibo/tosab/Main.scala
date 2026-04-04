package it.unibo.tosab
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.*
import it.unibo.tosab.model.entities.*
import it.unibo.tosab.model.entities.Entity.*
import it.unibo.tosab.view.DisplayGrid

@main def runApp(): Unit =
  val grid = Grid()
  val wizard = createEntity("wizard", Faction.Player, Role.Mage)
  val soldier = createEntity("soldier", Faction.AI, Role.Soldier)
  val soldier2 = createEntity("soldier", Faction.AI, Role.Soldier)
  grid.setCell(wizard, (5, 5))
  grid.setCell(soldier, (1, 4))
  grid.setCell(soldier, (8, 8))
  grid.setCell(soldier, (2, 4))
  grid.setCell(soldier2, (3, 4))
  // da controllare numerazione truppe sia se ne creo due uguali che se ne creo due diversi,
  // se è possibile creare due truppe uguali e se è possibile creare due truppe diverse con lo stesso id
  DisplayGrid.display(grid)

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
