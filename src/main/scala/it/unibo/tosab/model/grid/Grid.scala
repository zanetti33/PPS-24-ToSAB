package it.unibo.tosab.model.grid

type Coordinate = (Int, Int)

class Grid:
  val size = 8
  // Inizializziamo la griglia come una mappa o una matrice di Entity
  private var cells: Map[Coordinate, String] =
    (for
        x <- 0 until size
        y <- 0 until size
    yield (x, y) -> "empty").toMap

  def setCell(entity: String, cell: Coordinate): Unit = 
    val position = (cell._1 - 1, cell._2 - 1) 
    position match
    case (x, y) if isWithinBounds(position) && cells(position) == "empty" => cells = cells + (position -> entity)
    case _ =>  println(s"Cell $cell is not valid.")

  def getEntity(position: Coordinate): String = position match
    case (x, y) if isWithinBounds(position) => cells.getOrElse(position, "empty")
    case _ => "invalid position"
  
  def getOccupiedCells: Set[Coordinate] =
    cells.filter((_, entity) => entity != "empty").keySet

  def getAdjacentAvailableCells(entity: String): Set[Coordinate] =
    val occupiedCells = getOccupiedCells
    val entityPositions = cells.filter((_, e) => e == entity).keySet
    entityPositions.flatMap(getNeighbors).diff(occupiedCells)

  private def getNeighbors(pos: Coordinate): Set[Coordinate] =
    val (x, y) = pos
    val offsetsEven = Set(
      (-1, -1), (-1, 0),   // Riga sopra
      (0, -1), (0, 1),    // Stessa riga
      (1, -1), (1, 0)   // Riga sotto
    )

    val offsetsOdd = Set(
      (-1, 0), (-1, 1),   // Riga sopra
      (0, -1), (0, 1),    // Stessa riga
      (1, 0), (1, 1)      // Riga sotto
    )
    val offsets = if (x % 2 == 0) offsetsEven else offsetsOdd

    offsets.map { case (dx, dy) => (x + dx, y + dy) }.filter(isWithinBounds)

  private def isWithinBounds(pos: Coordinate): Boolean = pos match
    case (x, y) if x >= 0 && x < size && y >= 0 && y < size => true
    case _ => false