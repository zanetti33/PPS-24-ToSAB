package it.unibo.tosab.model.grid

trait GridManager:
  def size: Int
  def getDistance(p1: Coordinate, p2: Coordinate): Int
  def getNeighbors(pos: Coordinate): Set[Coordinate]
  def getDiagonal(from: Coordinate, to: Coordinate): Set[Coordinate]
  def isWithinBounds(pos: Coordinate): Boolean =
    pos.x >= 0 && pos.x < size && pos.y >= 0 && pos.y < size

class HexagonalGrid(val size: Int) extends GridManager:
  override def getDistance(p1: Coordinate, p2: Coordinate): Int =
    def toCube(pos: Coordinate) =
      val q = pos.y - (pos.x - (pos.x % 2)) / 2
      (q, pos.x, -q - pos.x)
    val (q1, r1, s1) = toCube(p1)
    val (q2, r2, s2) = toCube(p2)
    ((q1 - q2).abs + (r1 - r2).abs + (s1 - s2).abs) / 2

  override def getNeighbors(pos: Coordinate): Set[Coordinate] =
    val x = pos.x
    val y = pos.y
    val offsets =
      if x % 2 == 0 then Set((-1, -1), (-1, 0), (0, -1), (0, 1), (1, -1), (1, 0))
      else Set((-1, 0), (-1, 1), (0, -1), (0, 1), (1, 0), (1, 1))

    offsets.map((dx, dy) => Coordinate(x + dx, y + dy)).filter(isWithinBounds)

  override def getDiagonal(from: Coordinate, to: Coordinate): Set[Coordinate] =
    val down = from.x < to.x // se true scendo
    val right = from.y < to.y // se true vado verso destra
    def deltaX(x: Int): Int =
      if down then x + 1 else x - 1
    def deltaY(x: Int, y: Int): Int =
      if right then if x % 2 == 0 then y else y + 1
      else if x % 2 == 0 then y - 1
      else y
    var cellsInDiagonal = Set[Coordinate](from)
    var x = from.x
    var y = from.y
    while x != to.x do
      y = deltaY(x, y)
      x = deltaX(x)
      val cell = Coordinate(x, y)
      if isWithinBounds(cell) then cellsInDiagonal = cellsInDiagonal + cell
    cellsInDiagonal
