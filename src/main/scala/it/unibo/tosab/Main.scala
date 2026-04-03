package it.unibo.tosab
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

  /*
  def main(args: Array[String]): Unit =
    println("==============================")
    println(" TOWN OF SAVIOM: AUTO-BATTLER ")
    println("==============================")

    println("[INIT] Generazione Griglia 10x10...")
    val initialGrid = Grid(10, 10)

    println("[INIT] Creazione Unità in corso...")
    // Creiamo alcune unità di prova usando le strutture immutabili
    val u1 = Archer()
    val u2 = Soldier()
    val u3 = Mage()

    println("[INIT] Inizializzazione GameState globale...")
    val initialState = GameState(initialGrid)

    initialState.setUnit(u1, Coordinate(0, 2))
    initialState.setUnit(u2, Coordinate(3, 2))
    initialState.setUnit(u3, Coordinate(4, 5))

    println("\n[STATO GIOCO SPRINT 1] Setup completato con successo!\n")

    renderTerminalView(initialState)

  private def renderTerminalView(state: GameState): Unit =
    println(s"--- FASE ATTUALE: ${} ---")
    println(s"Mappa: ${}x${}")
    println("Unità in campo:")

    // TODO access units and simple print

    println("=========================================\n")
   */
