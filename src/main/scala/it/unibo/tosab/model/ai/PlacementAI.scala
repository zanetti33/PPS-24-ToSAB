package it.unibo.tosab.model.ai

import it.unibo.tosab.model.entities
import it.unibo.tosab.model.entities.{Entity, Faction, Role, Stats}
import it.unibo.tosab.model.grid.Grid
import it.unibo.tosab.update.GameSetup

object PlacementAI:
  def placeAITroops(grid: Grid): Grid =
    val rolesToPlace = getTroopRoles(GameSetup.getMaxNumberOfTroops)

    var troopsToPlace = Seq.empty[entities.Character]
    for (role, index) <- rolesToPlace.zip(0 until GameSetup.getMaxNumberOfTroops) do
      troopsToPlace = troopsToPlace ++ Seq(createTroop(index, role))
    val orderedTroops = troopsToPlace.sortBy(troop => troop.stats.currentHp).reverse

    placeTroops(orderedTroops, grid)

  private def placeTroops(troops: Seq[entities.Character], grid: Grid): Grid =
    // Group consecutive troops by role
    val groups = troops
      .foldLeft(List.empty[List[entities.Character]]) { (acc, troop) =>
        if acc.isEmpty || acc.head.head.role != troop.role then List(troop) :: acc
        else (troop :: acc.head) :: acc.tail
      }
      .reverse
      .map(_.reverse)

    var currentGrid = grid
    var currentX = grid.size / 2 - 1
    val yRange = 0 until grid.size
    for group <- groups do
      var remainingTroops = group
      while remainingTroops.nonEmpty do
        val availableY = yRange.filter(y => currentGrid.getEntity((currentX, y)).isEmpty)
        val toPlace = remainingTroops.take(availableY.size)
        val selectedY = scala.util.Random.shuffle(availableY).take(toPlace.size)
        for (troop, y) <- toPlace.zip(selectedY) do
          currentGrid = currentGrid.setCell(troop, (currentX, y))
        remainingTroops = remainingTroops.drop(toPlace.size)
        if remainingTroops.nonEmpty then
          currentX -= 1
          if currentX < 0 then currentX = scala.util.Random.nextInt(grid.size / 2)
      currentX -= 1
      if currentX < 0 then currentX = scala.util.Random.nextInt(grid.size / 2)
    currentGrid

  private def createTroop(troopIndex: Int, role: Role) =
    val id = s"${role.toString.toLowerCase}_${troopIndex}"
    val troop = role match
      case Role.Soldier => Entity.soldier(id, Faction.AI)
      case Role.Archer  => Entity.archer(id, Faction.AI)
      case Role.Mage    => Entity.mage(id, Faction.AI)
    troop

  private def getTroopRoles(maxTroops: Int): Seq[Role] =
    val baseRoles = Seq(Role.Soldier, Role.Archer, Role.Mage)
    val additionalCount = scala.util.Random.nextInt(maxTroops - baseRoles.size + 1)
    val additionalRoles =
      if additionalCount > 0 then
        Seq.fill(additionalCount)(scala.util.Random.shuffle(baseRoles).head)
      else Seq.empty
    baseRoles ++ additionalRoles
