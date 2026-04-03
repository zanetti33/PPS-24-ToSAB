package it.unibo.tosab.model.grid
import it.unibo.tosab.model.grid.Grid

object DisplayGrid:

  def display(grid: Grid): Unit =
    val size = grid.size

    for column <- 0 until size do {
      if column == 0 then print(f"*    $column   *")
      else print(f"   $column   *")
    }
    println()

    val troup = 1
    println(f"*  /   \\" + f"   /   \\" * (size - 1))
    for row <- 0 until size do {
      if row % 2 == 0 then print(row) else print(f"$row    ")
      for column <- 0 until size do
        val entity = grid.getEntity((row, column))

        // Formattazione del contenuto della cella
        val content = entity match
          case "empty" => "|       "
          case entity =>
            val initial = entity.head.toUpper.toString
            f"|  $initial $troup  "
        // qui farò un match per vedere se l'unità è mia o nemica f"*$initial$troup*"
        if column == (size - 1) && row % 2 != 0 then {
          println(f"$content|")
          if row == (size - 1) then println(f"*   " + f"   \\   /" * size)
          else println(f"*  /" + f"   \\   /" * size)
        } else if column == (size - 1) && row % 2 == 0 then {
          println(f"$content|")
          println(f"*  \\" + f"   /   \\" * size)
        } else print(content)
    }

//    // 2. Stampa le righe della griglia
//    println(f"*  /   \\" + f"   /   \\" * (size - 1))
//    for row <- 0 until size do
//      row match
//        case row if row == (size - 1) => {
//          println(f"$row    |" + f"       |" * size)
//          println(f"*   " + f"   \\   /" * size)
//        }
//        case row if row % 2 != 0 => {
//          println(f"$row    |" + f"       |" * size)
//          println(f"*  /" + f"   \\   /" * size)
//        }
//        case _ => {
//          println(f"$row|" + f"       |" * size)
//          println(f"*  \\" + f"   /   \\" * size)
//        }

//      val troup = 1
//      for column <- 0 until size do
//        val entity = grid.getEntity((row, column))
//
//        // Formattazione del contenuto della cella
//        val content = entity match
//          case "empty" => "     "
//          case entity =>
//            val initial = entity.head.toUpper.toString
//            f" $initial $troup "
//          //qui farò un match per vedere se l'unità è mia o nemica f"*$initial$troup*"
//
//        print(s"$content|")
