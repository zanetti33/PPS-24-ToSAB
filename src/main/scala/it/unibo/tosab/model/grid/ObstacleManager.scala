package it.unibo.tosab.model.grid

import it.unibo.tosab.model.entities.{Entity, EntityId}

/** Manages obstacle placement on the grid. */
class ObstacleManager(grid: Grid, gridPlacement: PlacementManager, size: Int):

  /** Places random obstacles on the grid.
    * @return
    *   the updated grid with obstacles
    */
  def placeObstacles(): Grid =
    val random = scala.util.Random.nextInt(size)
    var currentGrid = grid
    for i <- 0 to random do
      val pos = gridPlacement.generateRandomPosition(currentGrid)
      val obstacle = scala.util.Random.nextInt(4) match
        case 0 => Entity.wall(EntityId(s"wall_$i"))
        case 1 => Entity.bush(EntityId(s"bush_$i"))
        case 2 => Entity.tree(EntityId(s"tree_$i"))
        case _ => Entity.rock(EntityId(s"rock_$i"))
      currentGrid = currentGrid.setCell(obstacle, pos)
    currentGrid
