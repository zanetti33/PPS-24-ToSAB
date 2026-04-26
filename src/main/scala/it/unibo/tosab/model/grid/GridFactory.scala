package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.Entity

object GridFactory:
  def createHexagonal(size: Int): Grid =
    val gridManager = HexagonalGrid(size)
    new Grid(size, Map.empty, gridManager)

  def createWithManager(size: Int, gridManager: GridManager): Grid =
    new Grid(size, Map.empty, gridManager)
