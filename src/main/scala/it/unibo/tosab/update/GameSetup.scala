package it.unibo.tosab.update

import it.unibo.tosab.model.grid.{Coordinate, Grid}
import it.unibo.tosab.model.entities.{Entity, EntityId, Faction, Role}
import it.unibo.tosab.view.IO

object GameSetup:

  private final val maxNumberOfTroops = 5

  def getMaxNumberOfTroops: Int = maxNumberOfTroops

  def runSetupLoop(
      currentGrid: Grid,
      entityCounter: Int = 0
  ): IO[Grid] =
    for
      _ <- printMenu()
      _ <- IO.printLine("> ")
      userInput <- IO.readString()
      res <- processCommands(InputParser.parse(userInput), currentGrid, entityCounter)
      finalGrid <- if res._3 then runSetupLoop(res._1, res._2) else IO(() => res._1)
    yield finalGrid

  private def processCommands(
      commands: List[SetupCommand],
      grid: Grid,
      counter: Int
  ): IO[(Grid, Int, Boolean)] = counter match
    case c if c >= maxNumberOfTroops =>
      for _ <- IO.printLine("Maximum number of troops placed! Starting battle.")
      yield (grid, counter, false)
    case _ =>
      commands match
        case Nil => IO(() => (grid, counter, true))

        case SetupCommand.StartGame :: _ =>
          for _ <- IO.printLine("Positioning complete! Start battle.")
          yield (grid, counter, false)

        case SetupCommand.Invalid(reason) :: tail =>
          for
            _ <- IO.printLine(s"[ERROR] $reason")
            res <- processCommands(tail, grid, counter)
          yield res

        case SetupCommand.AddTroop(role, position) :: tail =>
          val id = EntityId(s"${role.toString.toLowerCase}_$counter")

          val newEntity = role match
            case Role.Soldier => Entity.soldier(id, Faction.Player)
            case Role.Archer  => Entity.archer(id, Faction.Player)
            case Role.Mage    => Entity.mage(id, Faction.Player)

          val newGrid = grid.setCell(newEntity, position)
          if newGrid.getEntity(position).exists(_.id == id) then
            for
              _ <- IO.printLine(
                s"[OK] $role placed in (${position.x}, ${position.y})."
              )
              res <- processCommands(tail, newGrid, counter + 1)
            yield res
          else
            for
              _ <- IO.printLine(
                s"[ERROR] Invalid position (${position.x}, ${position.y}). Try again."
              )
              res <- processCommands(tail, newGrid, counter)
            yield res

  private def printMenu(): IO[Unit] = IO.printLine(
    s"""|
         |--- POSITIONING PHASE ---
       |1: Soldier | 2: Archer | 3: Mage
       |Place up to $maxNumberOfTroops troops following this format: ID (X,Y) [e.g. 1 (0,1)]
       |Type 'start' to start the battle.""".stripMargin
  )
