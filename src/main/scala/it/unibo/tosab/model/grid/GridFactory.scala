package it.unibo.tosab.model.grid

/** Factory for creating Grid instances. */
object GridFactory:
  /** Creates a hexagonal grid of given size.
    * @param size
    *   the grid size
    * @return
    *   new Grid
    */
  def createHexagonal(size: Int): Grid =
    val gridManager = HexagonalGrid(size)
    new Grid(size, Map.empty, gridManager)
