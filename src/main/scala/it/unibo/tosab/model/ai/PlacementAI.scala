package it.unibo.tosab.model.ai

import it.unibo.tosab.model.entities
import it.unibo.tosab.model.entities.{Entity, Faction, Role}
import it.unibo.tosab.model.grid.{Coordinate, Grid, GridLane, Lane}
import it.unibo.tosab.update.GameSetup

object PlacementAI:
  private val emptyLaneStart = 0
  private val minTroops = 0

  def placeAITroops(grid: Grid, troopsNumber: Int = GameSetup.getMaxNumberOfTroops): Grid =
    val rolesToPlace = getTroopRoles(troopsNumber)

    var troopsToPlace = Seq.empty[entities.Character]
    for (role, index) <- rolesToPlace.zipWithIndex do
      troopsToPlace = troopsToPlace ++ Seq(createTroop(index, role))
    val orderedTroops = troopsToPlace.sortBy(troop => troop.stats.currentHp).reverse

    divideTroopsIntoLanes(orderedTroops, grid)

  private def divideTroopsIntoLanes(troops: Seq[entities.Character], grid: Grid): Grid =
    val (backLane, middleLane, frontLane) = GridLane.calculateLanes(grid.size / 2)
    var currentGrid = grid
    for troop <- troops do
      currentGrid = troop.role match
        case Role.Soldier => placeTroopInLane(troop, frontLane, currentGrid)
        case Role.Archer  => placeTroopInLane(troop, middleLane, currentGrid)
        case Role.Mage    => placeTroopInLane(troop, backLane, currentGrid)
    currentGrid

  private def placeTroopInLane(troop: entities.Character, lane: Lane, grid: Grid): Grid =
    availableLanePositions(grid, lane)
      .headOption
      .map(position => grid.setCell(troop, position))
      .getOrElse(grid)

  private def availableLanePositions(grid: Grid, lane: Lane): Seq[Coordinate] =
    val xRange = lane.from until lane.to
    if xRange.isEmpty then Seq.empty
    else
      val allLanePositions =
        for
          x <- xRange
          y <- emptyLaneStart until grid.size
        yield (x, y)
      scala.util.Random.shuffle(allLanePositions)
        .filter(position => grid.getEntity(position).isEmpty)

  private def createTroop(troopIndex: Int, role: Role) =
    val id = s"${role.toString.toLowerCase}_$troopIndex"
    val troop = role match
      case Role.Soldier => Entity.soldier(id, Faction.AI)
      case Role.Archer  => Entity.archer(id, Faction.AI)
      case Role.Mage    => Entity.mage(id, Faction.AI)
    troop

  private def getTroopRoles(maxTroops: Int): Seq[Role] =
    val baseRoles = Seq(Role.Soldier, Role.Archer, Role.Mage)
    if maxTroops <= minTroops then Seq.empty
    else if maxTroops <= baseRoles.size then
      scala.util.Random.shuffle(baseRoles).take(maxTroops)
    else
      val additionalCount = scala.util.Random.nextInt(maxTroops - baseRoles.size + 1)
      val additionalRoles =
        if additionalCount > 0 then
          Seq.fill(additionalCount)(scala.util.Random.shuffle(baseRoles).head)
        else Seq.empty
      baseRoles ++ additionalRoles
