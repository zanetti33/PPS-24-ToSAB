package it.unibo.tosab.update

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.entities.Role
import it.unibo.tosab.model.grid.Coordinate

class InputParserTest:

  @Test def testParseStart(): Unit =
    val commands = InputParser.parse("start")
    assertEquals(List(SetupCommand.StartGame), commands)

  @Test def testParseAddSoldier(): Unit =
    val commands = InputParser.parse("1 (0,1)")
    assertEquals(List(SetupCommand.AddTroop(Role.Soldier, Coordinate(0, 1))), commands)

  @Test def testParseAddArcher(): Unit =
    val commands = InputParser.parse("2 (2,3)")
    assertEquals(List(SetupCommand.AddTroop(Role.Archer, Coordinate(2, 3))), commands)

  @Test def testParseAddMultipleUnits(): Unit =
    val commands = InputParser.parse("3 (4,5) 1 (0,1) 2 (2,3)")
    val expected = List(
      SetupCommand.AddTroop(Role.Mage, Coordinate(4, 5)),
      SetupCommand.AddTroop(Role.Soldier, Coordinate(0, 1)),
      SetupCommand.AddTroop(Role.Archer, Coordinate(2, 3))
    )
    assertEquals(expected, commands)

  @Test def testParseInvalidTroopId(): Unit =
    val commands = InputParser.parse("4 (0,0)")
    assertEquals(List(SetupCommand.Invalid("Troop ID '4' invalid. Use 1, 2 or 3.")), commands)

  @Test def testParseInvalidFormat(): Unit =
    val commands = InputParser.parse("Hello world")
    assertEquals(List(SetupCommand.Invalid("Invalid format. Use: <id> (<x>,<y>) or write 'start'.")), commands)
