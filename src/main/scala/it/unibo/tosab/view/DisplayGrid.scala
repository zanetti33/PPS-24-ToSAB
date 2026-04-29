package it.unibo.tosab.view

import it.unibo.tosab.model.entities.ObstacleType.*
import it.unibo.tosab.model.entities.{Character, Entity, EntityId, Obstacle, ObstacleType}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

/** Utility for displaying the grid in the console. */
object DisplayGrid:

  private val obstacleSymbols: Map[ObstacleType, String] = Map(
    Wall -> "###",
    Bush -> "wWw",
    Tree -> "tTt",
    Rock -> "oOo"
  )

  private def getObstacleSymbol(obstacleType: ObstacleType): String = obstacleSymbols(obstacleType)

  private def getObstacleLegend(obstacleType: ObstacleType): String =
    s"|${getObstacleSymbol(obstacleType)}| = ${obstacleType.toString}"

  /** Displays the entire grid.
    * @param grid
    *   the grid to display
    */
  def display(grid: Grid): Unit =
    val size = grid.size
    printColHeader(size)

    for row <- 0 until size do
      printRowContent(grid, row)
      printRowSeparator(row, size)

  /** Displays the legend for entities and obstacles. */
  private def displayLegend(): Unit =
    val characterLegend = "|A n| = Ally, |a n| = Enemy"
    val obstacleLegends = ObstacleType.values.map(getObstacleLegend)
    val obstacleLegend = obstacleLegends.mkString(", ")
    println(s"Legend:\n$characterLegend\n$obstacleLegend")

  /** Displays the initial grid for troop placement.
    * @param grid
    *   the grid to display
    */
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

    displayLegend()

  /** Prints the column header.
    * @param size
    *   the grid size
    */
  private def printColHeader(size: Int): Unit =
    val colHeader = (0 until size).map(c => f" $c *").mkString("* ", "", "")
    println(colHeader)
    println(f"* / \\" + " / \\" * (size - 1))

  /** Prints the content of a row.
    * @param grid
    *   the grid
    * @param row
    *   the row index
    */
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

  /** Formats an entity for display.
    * @param e
    *   the entity
    * @return
    *   the formatted string
    */
  private def formatEntity(e: Entity): String = e match
    case c: Character =>
      val id = EntityId.value(c.id)
      if c.isAnEnemy then f"|${id.head.toLower} ${id.last}"
      else f"|${id.head.toUpper} ${id.last}"
    case o: Obstacle =>
      s"|${getObstacleSymbol(o.obstacleType)}"

  /** Prints the row separator.
    * @param row
    *   the row index
    * @param size
    *   the grid size
    */
  private def printRowSeparator(row: Int, size: Int): Unit =
    if row == size - 1 then println("*  " + " \\ /" * size + "\n")
    else if row % 2 == 0 then println("* \\" + " / \\" * size)
    else println("* /" + " \\ /" * size)
