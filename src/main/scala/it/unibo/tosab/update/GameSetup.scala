package it.unibo.tosab.update

import it.unibo.tosab.model.io.{InputParser, MonadIO, SetupCommand}
import it.unibo.tosab.model.grid.{Coordinate, Grid}
import it.unibo.tosab.model.entities.{Entity, Faction, Role}

object GameSetup:

  private def printMenu(): MonadIO[Unit] = MonadIO.printLine(
    """|
       |--- POSITIONING PHASE ---
       |1: Soldier | 2: Archer | 3: Mage
       |Place troops following this format: ID (X,Y) [e.g. 1 (0,1)]
       |Type 'start' to start the battle.""" // .stripMargin
  )

  def runSetupLoop(
      currentGrid: Map[Coordinate, Option[Entity]],
      entityCounter: Int = 0
  ): MonadIO[Map[Coordinate, Option[Entity]]] = for
    _ <- printMenu()
    _ <- MonadIO.printLine("> ")
    userInput <- MonadIO.readString()
    nextGrid <- InputParser.parse(userInput) match

      case SetupCommand.StartGame =>
        for _ <- MonadIO.printLine("Positioning complete! Start battle.")
        yield currentGrid

      case SetupCommand.Invalid(reason) =>
        for
          _ <- MonadIO.printLine(s"[ERROR] $reason")
          g <- runSetupLoop(currentGrid, entityCounter) // Ricorsione
        yield g

      case SetupCommand.AddTroop(role, position) =>
        val id = s"${role.toString.toLowerCase}_$entityCounter"

        val newEntity = role match
          case Role.Soldier => Entity.soldier(id, Faction.Player)
          case Role.Archer  => Entity.archer(id, Faction.Player)
          case Role.Mage    => Entity.mage(id, Faction.Player)

        // Questo deve restituire una nuova griglia immutabile
        val newGrid = currentGrid + (position -> Some(newEntity))

        for
          _ <- MonadIO.printLine(s"[OK] $role placed in (${position._1}, ${position._2}).")
          g <- runSetupLoop(newGrid, entityCounter + 1)
        yield g
  yield nextGrid
