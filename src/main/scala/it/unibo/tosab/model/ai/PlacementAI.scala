package it.unibo.tosab.model.ai

import it.unibo.tosab.model.entities
import it.unibo.tosab.model.entities.{Entity, Faction, Role, Stats}
import it.unibo.tosab.model.grid.{Grid, GridLane, Lane}
import it.unibo.tosab.update.GameSetup

object PlacementAI:
  def placeAITroops(grid: Grid, troopsNumber: Int = GameSetup.getMaxNumberOfTroops): Grid =
    val rolesToPlace = getTroopRoles(troopsNumber)

    var troopsToPlace = Seq.empty[entities.Character]
    for (role, index) <- rolesToPlace.zip(0 until GameSetup.getMaxNumberOfTroops) do
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
    val xRange = lane.from until lane.to
    var position = (scala.util.Random.shuffle(xRange).head, scala.util.Random.nextInt(grid.size))
    while grid.getEntity(position).isDefined do
      position = (scala.util.Random.shuffle(xRange).head, scala.util.Random.nextInt(grid.size))
    grid.setCell(troop, position)

  private def createTroop(troopIndex: Int, role: Role) =
    val id = s"${role.toString.toLowerCase}_$troopIndex"
    val troop = role match
      case Role.Soldier => Entity.soldier(id, Faction.AI)
      case Role.Archer  => Entity.archer(id, Faction.AI)
      case Role.Mage    => Entity.mage(id, Faction.AI)
    troop

  private def getTroopRoles(maxTroops: Int): Seq[Role] =
    val baseRoles = Seq(Role.Soldier, Role.Archer, Role.Mage)
    if maxTroops < baseRoles.size then
      Seq.fill(maxTroops)(scala.util.Random.shuffle(Seq(Role.Soldier, Role.Archer, Role.Mage)).head)
    else
      val additionalCount = scala.util.Random.nextInt(maxTroops - baseRoles.size + 1)
      val additionalRoles =
        if additionalCount > 0 then
          Seq.fill(additionalCount)(scala.util.Random.shuffle(baseRoles).head)
        else Seq.empty
      baseRoles ++ additionalRoles
