package it.unibo.tosab.model.ai

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.ai.CharacterAI.{
  BasicCharacterAI,
  CleanerCharacterAI,
  DoesNothingCharacterAI
}
import it.unibo.tosab.model.entities.{Character, Entity, EntityId, Faction, Stats}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

class CharacterAITest:
  private val aiStartPositionForLongMove = Coordinate(0, 0)
  private val playerTargetPositionForLongMove = Coordinate(4, 0)
  private val expectedLongMoveTarget = Coordinate(2, 0)
  private val movementDistanceTwo = 2
  private val state: GameState = GameState(GamePhase.Setup, Grid())
  private val unitId = EntityId("unit1")
  private val missingUnitId = EntityId("missing")
  private val playerSoldier: Entity = Entity.soldier(unitId, Faction.Player)
  private val aiSoldier: Entity = Entity.soldier(EntityId("ai1"), Faction.AI)
  private val aiFastSoldier: Character =
    Entity
      .soldier(EntityId("ai-fast"), Faction.AI)
      .copy(
        stats = Stats.baseSoldierStats.copy(movementDistance = movementDistanceTwo)
      )
  private val aiImmobileSoldier: Character =
    Entity
      .soldier(EntityId("ai-immobile"), Faction.AI)
      .copy(stats = Stats.baseSoldierStats.copy(movementDistance = 0))
  private val aiArcher: Character = Entity.archer(EntityId("ai-archer"), Faction.AI)

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
      .setCell(aiSoldier, Coordinate(3, 3))
      .setCell(playerSoldier, Coordinate(4, 3))
    val combatState = GameState(GamePhase.Combat, grid)
    val action = BasicCharacterAI.determineNextAction(combatState, aiSoldier.id)
    assertEquals(GameAction.Attack(playerSoldier.id), action)

  @Test def testBasicAIInCombatMovesWhenEnemyIsDistant(): Unit =
    val grid = Grid()
      .setCell(aiSoldier, Coordinate(0, 0))
      .setCell(playerSoldier, Coordinate(7, 7))
    val combatState = GameState(GamePhase.Combat, grid)
    val action = BasicCharacterAI.determineNextAction(combatState, aiSoldier.id)
    assertTrue(action match
      case GameAction.Move(target) => Set(Coordinate(0, 1), Coordinate(1, 0)).contains(target)
      case _                       => false
    )

  @Test def testBasicAIInCombatPassesWhenThereIsNoEnemy(): Unit =
    val grid = Grid()
      .setCell(aiSoldier, Coordinate(3, 3))
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

  @Test def testCleanerAIPrefersAttackingNearbyBushOverMovingToDistantEnemy(): Unit =
    val bush = Entity.bush(EntityId("bush1"))
    val grid = Grid()
      .setCell(aiSoldier, aiStartPositionForLongMove)
      .setCell(bush, Coordinate(3, 0))
      .setCell(playerSoldier, playerTargetPositionForLongMove)
    val combatState = GameState(GamePhase.Combat, grid)
    val action = CleanerCharacterAI.determineNextAction(combatState, playerSoldier.id)
    assertEquals(GameAction.Attack(bush.id), action)

  @Test def testBasicAIWithZeroMovementPassesWhenEnemyIsOutOfRange(): Unit =
    val grid = Grid()
      .setCell(aiImmobileSoldier, Coordinate(0, 0))
      .setCell(playerSoldier, Coordinate(4, 0))
    val combatState = GameState(GamePhase.Combat, grid)

    val action = BasicCharacterAI.determineNextAction(combatState, aiImmobileSoldier.id)
    assertEquals(GameAction.Pass, action)

  @Test def testBasicAIUsesJumpWhenPassableObstacleBlocksDirectPath(): Unit =
    val bush = Entity.bush(EntityId("bush-jump"))
    val grid = Grid()
      .setCell(aiArcher, Coordinate(1, 1))
      .setCell(bush, Coordinate(2, 1))
      .setCell(playerSoldier, Coordinate(4, 1))
    val combatState = GameState(GamePhase.Combat, grid)

    val action = BasicCharacterAI.determineNextAction(combatState, aiArcher.id)
    assertEquals(GameAction.Move(Coordinate(3, 1)), action)

  @Test def testCleanerAIIgnoresNonAttackableRockAndMoves(): Unit =
    val rock = Entity.rock(EntityId("rock1"))
    val grid = Grid()
      .setCell(aiSoldier, Coordinate(0, 0))
      .setCell(rock, Coordinate(0, 1))
      .setCell(playerSoldier, Coordinate(4, 0))
    val combatState = GameState(GamePhase.Combat, grid)

    val action = CleanerCharacterAI.determineNextAction(combatState, aiSoldier.id)
    assertTrue(action match
      case GameAction.Move(_) => true
      case _                  => false
    )

