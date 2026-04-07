package it.unibo.tosab.view

import it.unibo.tosab.model.entities.{Character, Faction}
import it.unibo.tosab.model.grid.Grid

object DisplayGrid:

  def display(grid: Grid): Unit =
    val size = grid.size
    val troup = 1

    def getContent(row: Int, column: Int): String =
      grid.getEntity((row, column)) match
        case None => "|   "
        case Some(entity) =>
          val initial = entity.id.head
          val char = entity match
            case c: Character if c.isAnEnemy => initial.toLower
            case _                           => initial.toUpper
          f"|$char $troup"

    // Stampa indici colonne
    for column <- 0 until size do
      if column == 0 then print(f"*  $column *") else print(f" $column *")
    println()

    // Stampa prima riga
    println(f"* / \\" + f" / \\" * (size - 1))

    for row <- 0 until size do
      // Stampa indici righe
      if row % 2 == 0 then print(row) else print(f"$row  ")
      // Stampa contenuto righe
      for column <- 0 until size do
        val cellContent = getContent(row, column)
        // Stampa ultima colonna e riga dispari
        if column == (size - 1) && row % 2 != 0 then
          println(f"$cellContent|")
          // Stampa ultima riga
          if row == (size - 1) then println(f"*  " + f" \\ /" * size)
          else println(f"* /" + f" \\ /" * size)
        // Stampa righe pari
        else if column == (size - 1) && row % 2 == 0 then
          println(f"$cellContent|")
          println(f"* \\" + f" / \\" * size)
        else print(cellContent)
