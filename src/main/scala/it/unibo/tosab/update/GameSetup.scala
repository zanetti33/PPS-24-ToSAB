package it.unibo.tosab.update

import it.unibo.tosab.model.grid.{Coordinate, Grid}
import it.unibo.tosab.model.entities.{Entity, EntityId, Faction, Role}
import it.unibo.tosab.view.IO

/** GameSetup object manages the initial positioning phase of the game.
  *
  * This singleton object provides functionality to set up the game board by allowing players to
  * position their troops on the grid before the battle starts.
  */
object GameSetup:

  private final val maxNumberOfTroops = 5

  /** @return
    *   the maximum number of troops
    */
  def getMaxNumberOfTroops: Int = maxNumberOfTroops

  /** Executes the main setup loop that handles the troop positioning phase.
    *
    * This method displays the positioning menu, reads user input, processes placement commands, and
    * updates the grid accordingly. The loop continues until either the maximum number of troops is
    * placed or the player chooses to start the battle.
    *
    * @param currentGrid
    *   the current state of the game grid
    * @param entityCounter
    *   the current count of placed entities (default: 0)
    * @return
    *   an IO action that yields the final grid configuration after setup completion
    */
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

  /** Processes a list of parsed setup commands and updates the game state accordingly.
    *
    * @param commands
    *   the list of SetupCommand to process
    * @param grid
    *   the current grid state
    * @param entityCounter
    *   the current number of placed entities
    * @return
    *   an IO action yielding a tuple containing:
    *   - the updated grid,
    *   - the updated entity counter,
    *   - a boolean indicating whether to continue the setup loop
    */
  private def processCommands(
      commands: List[SetupCommand],
      grid: Grid,
      entityCounter: Int
  ): IO[(Grid, Int, Boolean)] = entityCounter match
    case c if c >= maxNumberOfTroops =>
      for _ <- IO.printLine("Maximum number of troops placed! Starting battle.")
      yield (grid, entityCounter, false)
    case _ =>
      commands match
        case Nil => IO(() => (grid, entityCounter, true))

        case SetupCommand.StartGame :: _ =>
          for _ <- IO.printLine("Positioning complete! Start battle.")
          yield (grid, entityCounter, false)

        case SetupCommand.Invalid(reason) :: tail =>
          for
            _ <- IO.printLine(s"[ERROR] $reason")
            res <- processCommands(tail, grid, entityCounter)
          yield res

        case SetupCommand.AddTroop(role, position) :: tail =>
          val id = EntityId(s"${role.toString.toLowerCase}_$entityCounter")

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
              res <- processCommands(tail, newGrid, entityCounter + 1)
            yield res
          else
            for
              _ <- IO.printLine(
                s"[ERROR] Invalid position (${position.x}, ${position.y}). Try again."
              )
              res <- processCommands(tail, newGrid, entityCounter)
            yield res

  private def printMenu(): IO[Unit] = IO.printLine(
    s"""|
         |--- POSITIONING PHASE ---
       |1: Soldier | 2: Archer | 3: Mage
       |Place up to $maxNumberOfTroops troops following this format: ID (X,Y) [e.g. 1 (0,1)]
       |Type 'start' to start the battle.""".stripMargin
  )
