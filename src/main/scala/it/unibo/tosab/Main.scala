package it.unibo.tosab
import it.unibo.tosab.model.grid.*

@main def runApp(): Unit =
  val grid = Grid()
  grid.setCell("archer", (3, 5))
  grid.setCell("wizard", (6, 4))
  grid.setCell("soldier", (8, 8))
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
