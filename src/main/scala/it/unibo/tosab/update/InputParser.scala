package it.unibo.tosab.update

import it.unibo.tosab.model.entities.Role
import it.unibo.tosab.model.grid.Coordinate

object InputParser:

  // Regex detecting format: "1 (1,1)"
  private val troopPattern = """(\d)\s*\(\s*(\d+)\s*,\s*(\d+)\s*\)""".r

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
