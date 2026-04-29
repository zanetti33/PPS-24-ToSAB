package it.unibo.tosab.update

import it.unibo.tosab.model.entities.Role
import it.unibo.tosab.model.grid.Coordinate

/** Utility object for parsing user input during the game setup phase.
  */
object InputParser:

  // Regex pattern for parsing troop placement commands in format: "1 (1,1)"
  private val troopPattern = """(\d)\s*\(\s*(\d+)\s*,\s*(\d+)\s*\)""".r

  /** Parses user input string into a list of setup commands. Supports two formats:
    *   - "start" - signals the end of setup phase
    *   - "id (x,y)" - places a troop at coordinates (e.g., "1 (0,1)")
    *
    * @param input
    *   the raw user input string
    * @return
    *   list of SetupCommand objects representing the parsed commands
    */
  def parse(input: String): List[SetupCommand] = input.trim.toLowerCase match
    case "start" => List(SetupCommand.StartGame)
    case other =>
      val matches = troopPattern.findAllMatchIn(other).toList
      if matches.isEmpty then
        List(SetupCommand.Invalid("Invalid format. Use: <id> (<x>,<y>) or write 'start'."))
      else
        matches.map { m =>
          val roleId = m.group(1)
          val position = Coordinate(m.group(2).toInt, m.group(3).toInt)
          roleId match
            case "1" => SetupCommand.AddTroop(Role.Soldier, position)
            case "2" => SetupCommand.AddTroop(Role.Archer, position)
            case "3" => SetupCommand.AddTroop(Role.Mage, position)
            case _   => SetupCommand.Invalid(s"Troop ID '$roleId' invalid. Use 1, 2 or 3.")
        }
