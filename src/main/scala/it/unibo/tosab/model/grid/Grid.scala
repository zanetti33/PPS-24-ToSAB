package it.unibo.tosab.model.grid
import it.unibo.tosab.model.entities.Entity

type Coordinate = (Int, Int)

class Grid:
  val size = 8
  // Inizializziamo la griglia come una mappa o una matrice di Entity
  private var cells: Map[Coordinate, Option[Entity]] =
    (for
      x <- 0 until size
      y <- 0 until size
    yield (x, y) -> None).toMap

  def setCell(entity: Entity, position: Coordinate): Unit = position match
    case (x, y) if isPositionAvailable(position) && isRightField(entity.isAnEnemy, position) =>
      cells = cells + (position -> Some(entity))
    case _ => println(s"Cell $position is not valid.")

  def getEntity(position: Coordinate): Option[Entity] = position match
    case (x, y) if isWithinBounds(position) => cells(position)
    case _                                  => None

  def getOccupiedCells: Set[Coordinate] =
    cells.filter((_, entity) => entity.isDefined).keySet

  def getAdjacentAvailableCells(entity: Entity): Set[Coordinate] =
    val occupiedCells = getOccupiedCells
    val entityPositions = cells.filter((_, e) => e.contains(entity)).keySet
    entityPositions.flatMap(getNeighbors).diff(occupiedCells)

  def getDistance(position1: Coordinate, position2: Coordinate): Int =
    def toCube(pos: Coordinate) =
      val q = pos._2 - (pos._1 - (pos._1 % 2)) / 2
      (q, pos._1, -q - pos._1)

    val (q1, r1, s1) = toCube(position1)
    val (q2, r2, s2) = toCube(position2)
    ((q1 - q2).abs + (r1 - r2).abs + (s1 - s2).abs) / 2

  private def getNeighbors(pos: Coordinate): Set[Coordinate] =
    val (x, y) = pos
    val offsetsEven = Set(
      (-1, -1),
      (-1, 0), // Riga sopra
      (0, -1),
      (0, 1), // Stessa riga
      (1, -1),
      (1, 0) // Riga sotto
    )

    val offsetsOdd = Set(
      (-1, 0),
      (-1, 1), // Riga sopra
      (0, -1),
      (0, 1), // Stessa riga
      (1, 0),
      (1, 1) // Riga sotto
    )
    val offsets = if (x % 2 == 0) offsetsEven else offsetsOdd

    offsets.map { case (dx, dy) => (x + dx, y + dy) }.filter(isWithinBounds)

  private def isWithinBounds(pos: Coordinate): Boolean = pos match
    case (x, y) if x >= 0 && x < size && y >= 0 && y < size => true
    case _                                                  => false

  private def isPositionAvailable(pos: Coordinate): Boolean = pos match
    case (x, y) if isWithinBounds(pos) && cells(pos).isEmpty => true
    case _                                                   => false

  private def isRightField(enemy: Boolean, position: Coordinate): Boolean =
    if enemy then position._1 < 4 else position._1 >= 4
