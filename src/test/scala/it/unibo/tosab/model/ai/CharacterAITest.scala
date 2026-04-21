package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.ai.CharacterAI.{CharacterAI, BasicCharacterAI, DoesNothingCharacterAI}
import it.unibo.tosab.model.entities.{Character, Entity, Faction, Stats}
import it.unibo.tosab.model.grid.Grid

class CharacterAITest:
  private val aiStartPositionForLongMove = (0, 0)
  private val playerTargetPositionForLongMove = (4, 0)
  private val expectedLongMoveTarget = (2, 0)
  private val movementDistanceTwo = 2
  private val state: GameState = GameState(GamePhase.Setup, Grid())
  private val unitId = "unit1"
  private val missingUnitId = "missing"
  private val playerSoldier: Entity = Entity.soldier(unitId, Faction.Player)
  private val aiSoldier: Entity = Entity.soldier("ai1", Faction.AI)
  private val aiFastSoldier: Character =
    Entity
      .soldier("ai-fast", Faction.AI)
      .copy(
        stats = Stats.baseSoldierStats.copy(movementDistance = movementDistanceTwo)
      )

  @Test def testDoesNothingAIAlwaysReturnsPass(): Unit =
    val action = DoesNothingCharacterAI.determineNextAction(state, unitId)
    assertEquals(GameAction.Pass, action)

  @Test def testDoesNothingAIIsConsistent(): Unit =
    val a1 = DoesNothingCharacterAI.determineNextAction(state, unitId)
    val a2 = DoesNothingCharacterAI.determineNextAction(GameState(GamePhase.Combat, Grid()), unitId)
    assertEquals(a1, a2)

  @Test def testDoesNothingAIImplementsAITrait(): Unit =
    val ai: CharacterAI = DoesNothingCharacterAI
    val action = ai.determineNextAction(state, unitId)
    assertEquals(GameAction.Pass, action)

  @Test def testBasicAIReturnsPassWhenActorIsMissingInCombat(): Unit =
    val combatState = GameState(GamePhase.Combat, Grid())
    val action = BasicCharacterAI.determineNextAction(combatState, missingUnitId)
    assertEquals(GameAction.Pass, action)

  @Test def testBasicAIInCombatAttacksWhenEnemyIsClose(): Unit =
    val grid = Grid()
      .setCell(aiSoldier, (3, 3))
      .setCell(playerSoldier, (4, 3))
    val combatState = GameState(GamePhase.Combat, grid)
    val action = BasicCharacterAI.determineNextAction(combatState, aiSoldier.id)
    assertEquals(GameAction.Attack(playerSoldier.id), action)

  @Test def testBasicAIInCombatMovesWhenEnemyIsDistant(): Unit =
    val grid = Grid()
      .setCell(aiSoldier, (0, 0))
      .setCell(playerSoldier, (7, 7))
    val combatState = GameState(GamePhase.Combat, grid)
    val action = BasicCharacterAI.determineNextAction(combatState, aiSoldier.id)
    assertTrue(action match
      case GameAction.Move(target) => Set((0, 1), (1, 0)).contains(target)
      case _                       => false
    )

  @Test def testBasicAIInCombatPassesWhenThereIsNoEnemy(): Unit =
    val grid = Grid()
      .setCell(aiSoldier, (3, 3))
    val combatState = GameState(GamePhase.Combat, grid)
    val action = BasicCharacterAI.determineNextAction(combatState, aiSoldier.id)
    assertEquals(GameAction.Pass, action)

  @Test def testBasicAIInCombatUsesMovementDistanceToPickBestTargetCell(): Unit =
    val grid = Grid()
      .setCell(aiFastSoldier, aiStartPositionForLongMove)
      .setCell(playerSoldier, playerTargetPositionForLongMove)
    val combatState = GameState(GamePhase.Combat, grid)

    val action = BasicCharacterAI.determineNextAction(combatState, aiFastSoldier.id)
    assertEquals(GameAction.Move(expectedLongMoveTarget), action)
