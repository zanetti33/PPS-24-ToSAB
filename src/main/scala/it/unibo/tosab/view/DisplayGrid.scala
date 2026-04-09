package it.unibo.tosab.view

import it.unibo.tosab.model.entities.{Character, Entity, Faction}
import it.unibo.tosab.model.grid.Grid

object DisplayGrid:
  private var troop: Int = 0
  private var enemyTroop: Int = 0

  def display(grid: Grid): Unit =
    val size = grid.size

    val colHeader = (0 until size).map(c => f" $c *").mkString("* ", "", "")
    println(colHeader)
    println(f"* / \\" + " / \\" * (size - 1))

    for row <- 0 until size do
      printRowContent(grid, row)
      printRowSeparator(row, size)

  private def printRowContent(grid: Grid, row: Int): Unit =
    val rowLabel = if row % 2 == 0 then f"$row" else f"$row  "
    val content = (0 until grid.size)
      .map { col =>
        grid.getEntity((row, col)) match
          case None    => "|   "
          case Some(e) => formatEntity(e)
      }
      .mkString("", "", "|")
    println(s"$rowLabel$content")

  private def formatEntity(e: Entity): String = e match
    case c: Character =>
      if c.isAnEnemy then
        enemyTroop += 1
        f"|${c.id.head.toLower} $enemyTroop"
      else
        troop += 1
        f"|${c.id.head.toUpper} $troop"
    case _ => "| ? "

  private def printRowSeparator(row: Int, size: Int): Unit =
    if row == size - 1 then println("*  " + " \\ /" * size)
    else if row % 2 == 0 then println("* \\" + " / \\" * size)
    else println("* /" + " \\ /" * size)
