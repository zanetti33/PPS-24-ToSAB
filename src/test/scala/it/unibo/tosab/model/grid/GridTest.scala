package it.unibo.tosab.model.grid
import org.junit.*
import org.junit.Assert.*

class GridTest:
    import it.unibo.tosab.model.grid.Grid

    val grid = Grid()

    @Test def testUnitPosition(): Unit =
        grid.setCell("soldier", (3, 3))
        val occupiedCells = grid.getOccupiedCells
        assertTrue(occupiedCells.contains((3, 3)))
        assertFalse(occupiedCells.contains((2, 2)))

    @Test def testGetOccupiedCells(): Unit =
        grid.setCell("soldier", (1, 1))
        grid.setCell("soldier", (2, 2))
        val occupiedCells = grid.getOccupiedCells
        assertTrue(occupiedCells.contains((1, 1)))
        assertTrue(occupiedCells.contains((2, 2)))
        assertEquals(2, occupiedCells.size)

    @Test def testGetAdjacentCells(): Unit =
      // Inseriamo i soldati
      grid.setCell("soldier1", (2, 1))
      grid.setCell("soldier2", (5, 3))

      val availableCellsSoldier1 = grid.getAdjacentAvailableCells("soldier1")
      val availableCellsSoldier2 = grid.getAdjacentAvailableCells("soldier2")

      // Definiamo i set attesi (assicurati che le coordinate siano corrette per il tuo gioco)
      val expected1 = Set((1, 0), (1, 1), (2, 0), (2, 2), (3, 0), (3, 1))
      val expected2 = Set((4, 3), (4, 4), (5, 2), (5, 4), (6, 3), (6, 4))

      // Usiamo assertEquals tra Set (l'ordine degli elementi non conterà)
      assertEquals(expected1, availableCellsSoldier1)
      assertEquals(expected2, availableCellsSoldier2)