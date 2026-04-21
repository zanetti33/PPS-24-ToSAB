package it.unibo.tosab.update

import it.unibo.tosab.model.io.{InputParser, MonadIO, SetupCommand}
import it.unibo.tosab.model.grid.{Coordinate, Grid}
import it.unibo.tosab.model.entities.{Entity, EntityId, Faction, Role}

object GameSetup:

  private final val maxNumberOfTroops = 5

  def getMaxNumberOfTroops: Int = maxNumberOfTroops

  def runSetupLoop(
      currentGrid: Grid,
      entityCounter: Int = 0
  ): MonadIO[Grid] =
    for
      _ <- printMenu()
      _ <- MonadIO.printLine("> ")
      userInput <- MonadIO.readString()
      res <- processCommands(InputParser.parse(userInput), currentGrid, entityCounter)
      finalGrid <- if res._3 then runSetupLoop(res._1, res._2) else MonadIO(() => res._1)
    yield finalGrid

  private def processCommands(
      commands: List[SetupCommand],
      grid: Grid,
      counter: Int
  ): MonadIO[(Grid, Int, Boolean)] = counter match
    case c if c >= maxNumberOfTroops =>
      for _ <- MonadIO.printLine("Maximum number of troops placed! Starting battle.")
      yield (grid, counter, false)
    case _ =>
      commands match
        case Nil => MonadIO(() => (grid, counter, true))

        case SetupCommand.StartGame :: _ =>
          for _ <- MonadIO.printLine("Positioning complete! Start battle.")
          yield (grid, counter, false)

        case SetupCommand.Invalid(reason) :: tail =>
          for
            _ <- MonadIO.printLine(s"[ERROR] $reason")
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
              _ <- MonadIO.printLine(
                s"[OK] $role placed in (${Coordinate.x(position)}, ${Coordinate.y(position)})."
              )
              res <- processCommands(tail, newGrid, counter + 1)
            yield res
          else
            for
              _ <- MonadIO.printLine(
                s"[ERROR] Invalid position (${Coordinate.x(position)}, ${Coordinate.y(position)}). Try again."
              )
              res <- processCommands(tail, newGrid, counter)
            yield res

  private def printMenu(): MonadIO[Unit] = MonadIO.printLine(
    s"""|
         |--- POSITIONING PHASE ---
       |1: Soldier | 2: Archer | 3: Mage
       |Place up to $maxNumberOfTroops troops following this format: ID (X,Y) [e.g. 1 (0,1)]
       |Type 'start' to start the battle.""".stripMargin
  )
