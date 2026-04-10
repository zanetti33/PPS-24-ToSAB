package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.ai.CharacterAI.{CharacterAI, BasicCharacterAI$, DoesNothingCharacterAI$}
import it.unibo.tosab.model.entities.{Entity, Faction}
import it.unibo.tosab.model.grid.Grid

class CharacterAITest:

  val state: GameState = GameState(GamePhase.Setup, Grid())
  val unitId: String = "unit1"
  val missingUnitId: String = "missing"
  val playerSoldier: Entity = Entity.soldier(unitId, Faction.Player)
  val aiSoldier: Entity = Entity.soldier("ai1", Faction.AI)

  @Test def testDoesNothingAIAlwaysReturnsPass(): Unit =
    val action = DoesNothingCharacterAI$.determineNextAction(state, unitId)
    assertEquals(GameAction.Pass, action)

  @Test def testDoesNothingAIIsConsistent(): Unit =
    val a1 = DoesNothingCharacterAI$.determineNextAction(state, unitId)
    val a2 = DoesNothingCharacterAI$.determineNextAction(GameState(GamePhase.Combat, Grid()), unitId)
    assertEquals(a1, a2)

  @Test def testDoesNothingAIImplementsAITrait(): Unit =
    val ai: CharacterAI = DoesNothingCharacterAI$
    val action = ai.determineNextAction(state, unitId)
    assertEquals(GameAction.Pass, action)

  @Test def testBasicAIReturnsPassWhenActorIsMissingInCombat(): Unit =
    val combatState = GameState(GamePhase.Combat, Grid())
    val action = BasicCharacterAI$.determineNextAction(combatState, missingUnitId)
    assertEquals(GameAction.Pass, action)

  @Test def testBasicAIInCombatAttacksWhenEnemyIsClose(): Unit =
    val grid = Grid()
    grid.setCell(aiSoldier, (3, 3))
    grid.setCell(playerSoldier, (4, 3))
    val combatState = GameState(GamePhase.Combat, grid)

    val action = BasicCharacterAI$.determineNextAction(combatState, aiSoldier.id)
    assertEquals(GameAction.Attack(playerSoldier.id), action)

  @Test def testBasicAIInCombatMovesWhenEnemyIsDistant(): Unit =
    val grid = Grid()
    grid.setCell(aiSoldier, (0, 0))
    grid.setCell(playerSoldier, (7, 7))
    val combatState = GameState(GamePhase.Combat, grid)

    val action = BasicCharacterAI$.determineNextAction(combatState, aiSoldier.id)
    assertTrue(action match
      case GameAction.Move(target) => Set((0, 1), (1, 0)).contains(target)
      case _                       => false
    )

  @Test def testBasicAIInCombatPassesWhenThereIsNoEnemy(): Unit =
    val grid = Grid()
    grid.setCell(aiSoldier, (3, 3))
    val combatState = GameState(GamePhase.Combat, grid)

    val action = BasicCharacterAI$.determineNextAction(combatState, aiSoldier.id)
    assertEquals(GameAction.Pass, action)

