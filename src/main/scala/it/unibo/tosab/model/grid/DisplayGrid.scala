package it.unibo.tosab.model.grid
import it.unibo.tosab.model.grid.Grid

object DisplayGrid:

  def display(grid: Grid): Unit =
    val size = grid.size
    for column <- 0 until size do print(f"$column%6d")
    println()

    // 2. Stampa le righe della griglia
    for row <- 0 until size do
      val indent = if (row % 2 != 0) "   " else ""
      print(f"$indent$row%d|")

      val troup = 1
      for column <- 0 until size do
        val entity = grid.getEntity((row, column))

        // Formattazione del contenuto della cella
        val content = entity match
          case "empty" => "     "
          case entity =>
            val initial = entity.head.toUpper.toString
            f" $initial $troup "
          // qui farò un match per vedere se l'unità è mia o nemica f"*$initial$troup*"

        print(s"$content|")

      println()
