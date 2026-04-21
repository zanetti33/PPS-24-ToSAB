package it.unibo.tosab.model.engine

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.GameAction
import it.unibo.tosab.model.{DomainEvent, GamePhase, GameState}
import it.unibo.tosab.model.engine.Engine.{
  DoesNothingEngine,
  Engine,
  ImmediatelyEndEngine,
  TurnBasedCombatEngine
}
import it.unibo.tosab.model.entities.*
import it.unibo.tosab.model.grid.Grid

class EngineTest:

  private val attackerId = "attacker"
  private val targetId = "target"
  private val aiArcherId = "archer"
  private val attackerPosition = (4, 3)
  private val targetPosition = (3, 3)
  private val aiArcherPosition = (1, 1)
  private val playerLonePosition = (4, 4)
  private val attackerCurrentHp = 40
  private val nonLethalTargetHp = 50
  private val lethalTargetHp = 10
  private val boostedAttackerAttack = 100
  private val undefendedTargetDefense = 0
  private val fastAttackerSpeed = 3
  private val slowTargetSpeed = 1
  private val expectedRemainingTargetHp = 30
  private val grid: Grid = Grid()
  private val combatState: GameState = GameState(GamePhase.Combat, grid)
  private val gameOverState: GameState = GameState(GamePhase.GameOver, grid)
  private val unitId: String = "unit1"

  private def createAttacker(
      physicalAttack: Int = Stats.baseSoldierStats.physicalAttack
  ): Character =
    Character(
      attackerId,
      Faction.Player,
      Role.Soldier,
      Stats.baseSoldierStats.copy(
        currentHp = attackerCurrentHp,
        physicalAttack = physicalAttack,
        speed = fastAttackerSpeed
      )
    )

  private def createTarget(currentHp: Int): Character =
    Character(
      targetId,
      Faction.AI,
      Role.Soldier,
      Stats.baseSoldierStats.copy(
        currentHp = currentHp,
        physicalDefense = undefendedTargetDefense,
        speed = slowTargetSpeed
      )
    )

  private def createCombatState(attacker: Character, target: Character): GameState =
    GameState(
      GamePhase.Combat,
      Grid().setCell(attacker, attackerPosition).setCell(target, targetPosition),
      Seq(attacker.id, target.id)
    )

  @Test def testDoesNothingDoesNotChangePhase(): Unit =
    val engine: Engine = DoesNothingEngine
    val result = engine.applyUnitAction(combatState, unitId, GameAction.Pass)
    assertEquals(combatState, result.nextState)

  @Test def testDoesNothingEngineDoesNotChangeGrid(): Unit =
    val archer = Entity.archer(aiArcherId, Faction.AI)
    val updatedCombatState = GameState(GamePhase.Combat, grid.setCell(archer, aiArcherPosition))
    val result = DoesNothingEngine.applyUnitAction(updatedCombatState, unitId, GameAction.Pass)
    assertEquals(Some(archer), result.nextState.grid.getEntity(aiArcherPosition))

  @Test def testImmediatelyEndEngineChangesPhaseToGameOver(): Unit =
    val engine: Engine = ImmediatelyEndEngine
    val result = engine.applyUnitAction(combatState, unitId, GameAction.Pass)
    assertEquals(gameOverState, result.nextState)

  /** Tests that the TurnBasedCombatEngine correctly applies a non-lethal attack, reducing the
    * target's HP but not removing it from the grid, and that the turn queue and game phase are
    * updated accordingly.
    */
  @Test def testTurnBasedCombatEngineAppliesNonLethalAttack(): Unit =
    val attacker = createAttacker()
    val target = createTarget(nonLethalTargetHp)
    val state = createCombatState(attacker, target)

    val outcome =
      TurnBasedCombatEngine.applyUnitAction(state, attacker.id, GameAction.Attack(target.id))
    val result = outcome.nextState
    val updatedTarget = result.getCharacterById(target.id)

    assertEquals(Some(expectedRemainingTargetHp), updatedTarget.map(_.stats.currentHp))
    assertEquals(Seq(target.id), result.turnQueue)
    assertEquals(GamePhase.Combat, result.phase)
    assertEquals(2, outcome.events.size)
    assertEquals(DomainEvent.ActionApplied(attacker.id, GameAction.Attack(target.id)), outcome.events.head)
    assertEquals(DomainEvent.DamageInflicted(attacker.id, target.id, 20), outcome.events(1))

  /** Tests that the TurnBasedCombatEngine correctly applies a lethal attack, removing the target
    * from the grid, and that the turn queue is updated to remove the defeated target and the game
    * phase transitions to GameOver
    */
  @Test def testTurnBasedCombatEngineRemovesDeadTargetAndEndsGameWhenOneFactionRemains(): Unit =
    val attacker = createAttacker(boostedAttackerAttack)
    val target = createTarget(lethalTargetHp)
    val state = createCombatState(attacker, target)

    val outcome =
      TurnBasedCombatEngine.applyUnitAction(state, attacker.id, GameAction.Attack(target.id))
    val result = outcome.nextState

    assertEquals(None, result.getCharacterById(target.id))
    assertTrue(result.turnQueue.isEmpty)
    assertEquals(GamePhase.GameOver, result.phase)
    assertEquals(3, outcome.events.size)
    assertEquals(DomainEvent.ActionApplied(attacker.id, GameAction.Attack(target.id)), outcome.events.head)
    assertEquals(DomainEvent.DamageInflicted(attacker.id, target.id, lethalTargetHp), outcome.events(1))
    assertEquals(DomainEvent.UnitDied(target.id), outcome.events(2))

  /** Tests that the TurnBasedCombatEngine ends the round and transitions to GameOver when only one
    * faction has living characters remaining, even if the turn queue is not empty
    */
  @Test def testTurnBasedCombatEngineEndsRoundWhenOnlyOneFactionIsAlive(): Unit =
    val player = Entity.soldier("player-1", Faction.Player)
    val state = GameState(GamePhase.Combat, Grid().setCell(player, playerLonePosition))

    val result = TurnBasedCombatEngine.startNewRound(state)

    assertEquals(GamePhase.GameOver, result.phase)
    assertTrue(result.turnQueue.isEmpty)
