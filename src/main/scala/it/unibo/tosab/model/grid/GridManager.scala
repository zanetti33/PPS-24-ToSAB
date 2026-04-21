package it.unibo.tosab.model.grid

trait GridManager:
  def size: Int
  def getDistance(p1: Coordinate, p2: Coordinate): Int
  def getNeighbors(pos: Coordinate): Set[Coordinate]
  def isWithinBounds(pos: Coordinate): Boolean =
    Coordinate.x(pos) >= 0 && Coordinate.x(pos) < size && Coordinate.y(pos) >= 0 && Coordinate.y(
      pos
    ) < size

class HexagonalGrid(val size: Int) extends GridManager:
  override def getDistance(p1: Coordinate, p2: Coordinate): Int =
    def toCube(pos: Coordinate) =
      val q = Coordinate.y(pos) - (Coordinate.x(pos) - (Coordinate.x(pos) % 2)) / 2
      (q, Coordinate.x(pos), -q - Coordinate.x(pos))
    val (q1, r1, s1) = toCube(p1)
    val (q2, r2, s2) = toCube(p2)
    ((q1 - q2).abs + (r1 - r2).abs + (s1 - s2).abs) / 2

  override def getNeighbors(pos: Coordinate): Set[Coordinate] =
    val x = Coordinate.x(pos)
    val y = Coordinate.y(pos)
    val offsets =
      if x % 2 == 0 then Set((-1, -1), (-1, 0), (0, -1), (0, 1), (1, -1), (1, 0))
      else Set((-1, 0), (-1, 1), (0, -1), (0, 1), (1, 0), (1, 1))

    offsets.map((dx, dy) => Coordinate(x + dx, y + dy)).filter(isWithinBounds)
