package it.unibo.tosab.model.io

import it.unibo.tosab.model.entities.Role
import it.unibo.tosab.model.grid.Coordinate

object InputParser:

  // Regex detecting format: "1 (1,1)"
  private val troopPattern = """^(\d)\s*\(\s*(\d+)\s*,\s*(\d+)\s*\)$""".r

  def parse(input: String): SetupCommand = input.trim.toLowerCase match
    case "start" => SetupCommand.StartGame

    case troopPattern(roleId, xStr, yStr) =>
      val position: Coordinate = (xStr.toInt, yStr.toInt)
      roleId match
        case "1" => SetupCommand.AddTroop(Role.Soldier, position)
        case "2" => SetupCommand.AddTroop(Role.Archer, position)
        case "3" => SetupCommand.AddTroop(Role.Mage, position)
        case _   => SetupCommand.Invalid(s"Troop ID '$roleId' invalid. Use 1, 2 or 3.")

    case _ => SetupCommand.Invalid("Invalid format. Use: <id> (<x>,<y>) or write 'start'")
