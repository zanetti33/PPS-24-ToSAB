package it.unibo.tosab.view

import it.unibo.tosab.model.entities.ObstacleType.*
import it.unibo.tosab.model.entities.{Character, Entity, EntityId, Faction, Obstacle, ObstacleType}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

private def printColHeader(size: Int): Unit =
  val colHeader = (0 until size).map(c => f" $c *").mkString("* ", "", "")
  println(colHeader)
  println(f"* / \\" + " / \\" * (size - 1))

object DisplayGrid:
  private def clearScreen(): Unit =
    System.out.flush()
    print("\u001b[2J\u001b[H") // ANSI: clear screen + move cursor to home
    System.out.flush()

  def display(grid: Grid): Unit =
    clearScreen()
    val size = grid.size

    printColHeader(size)

    for row <- 0 until size do
      printRowContent(grid, row)
      printRowSeparator(row, size)

  def displayInitialGrid(grid: Grid): Unit =
    val size = grid.size
    println("        Place your troops!")
    printColHeader(size)

    for row <- 4 until size do
      val rowLabel = if row % 2 == 0 then f"$row" else f"$row  "
      val content = (0 until size)
        .map { col =>
          grid.getEntity(Coordinate(row, col)) match
            case Some(e) => formatEntity(e)
            case _       => "|   "
        }
        .mkString("", "", "|")

      println(s"$rowLabel$content")
      printRowSeparator(row, size)

    println(
      "Legend:\n|A a| = Ally, |a a| = Enemy\n|###| = Wall, |wWw| = Bush, |tTt| = Tree, |oOo| = Rock"
    )

  private def printRowContent(grid: Grid, row: Int): Unit =
    val rowLabel = if row % 2 == 0 then f"$row" else f"$row  "
    val content = (0 until grid.size)
      .map { col =>
        grid.getEntity(Coordinate(row, col)) match
          case None    => "|   "
          case Some(e) => formatEntity(e)
      }
      .mkString("", "", "|")
    println(s"$rowLabel$content")

  private def formatEntity(e: Entity): String = e match
    case c: Character =>
      val id = EntityId.value(c.id)
      if c.isAnEnemy then f"|${id.head.toLower} ${id.last}"
      else f"|${id.head.toUpper} ${id.last}"
    case o: Obstacle =>
      o.obstacleType match
        case Wall => "|###"
        case Bush => "|wWw"
        case Tree => "|tTt"
        case Rock => "|oOo"

  private def printRowSeparator(row: Int, size: Int): Unit =
    if row == size - 1 then println("*  " + " \\ /" * size + "\n")
    else if row % 2 == 0 then println("* \\" + " / \\" * size)
    else println("* /" + " \\ /" * size)
