package it.unibo.tosab.update

import it.unibo.tosab.model.ai.CharacterAI
import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.{DomainEvent, GameAction, GamePhase, GameState}
import it.unibo.tosab.model.ai.CharacterAI.DoesNothingCharacterAI
import it.unibo.tosab.model.engine.Engine.{
  DoesNothingEngine,
  ImmediatelyEndEngine,
  TurnBasedCombatEngine
}
import it.unibo.tosab.model.engine.{CommandIntent, Engine, EngineOutcome}
import it.unibo.tosab.model.entities.{Entity, EntityId, Faction}
import it.unibo.tosab.model.grid.{Coordinate, Grid}

import scala.collection.mutable.ArrayBuffer

class GameLoopTest:

  private val playerId = EntityId("player-1")
  private val lonePlayerPosition = Coordinate(4, 4)

  given defaultAI: CharacterAI = DoesNothingCharacterAI
  given defaultEngine: Engine = DoesNothingEngine
  val grid: Grid = Grid()

  @Before
  def setUp(): Unit =
    GameLoop.clearSubscribers()

  @After
  def tearDown(): Unit =
    GameLoop.clearSubscribers()

  private class RecordingSubscriber extends GameLoopSubscriber:
    val receivedEvents: ArrayBuffer[DomainEvent] =
      scala.collection.mutable.ArrayBuffer.empty[DomainEvent]

    override def update(event: DomainEvent): Unit =
      receivedEvents += event

  @Test def testRunDoesntDoAnything(): Unit =
    val gameOver = GameState(GamePhase.GameOver, grid)
    val result = GameLoop.run(gameOver)
    assertEquals(GamePhase.GameOver, result.phase)

  @Test def testRunPublishesGameEndedWhenAlreadyGameOver(): Unit =
    val subscriber = new RecordingSubscriber()
    val gameOver = GameState(GamePhase.GameOver, grid)

    GameLoop.subscribe(subscriber)
    GameLoop.run(gameOver)

    assertEquals(1, subscriber.receivedEvents.size)
    subscriber.receivedEvents.head match
      case DomainEvent.GameEnded(finalState) => assertEquals(gameOver, finalState)
      case other                             => fail(s"Unexpected event received: $other")

  @Test def testRunEndsTheGame(): Unit =
    given engine: Engine = ImmediatelyEndEngine
    val gameOver = GameState(GamePhase.Combat, grid)
    val result = GameLoop.run(gameOver)
    assertEquals(GamePhase.GameOver, result.phase)

  @Test def testRunEndsWhenOnlyOneFactionRemains(): Unit =
    given engine: Engine = TurnBasedCombatEngine
    val lonePlayerGrid =
      Grid().setCell(Entity.soldier(playerId, Faction.Player), lonePlayerPosition)
    val combatState = GameState(GamePhase.Combat, lonePlayerGrid)

    val result = GameLoop.run(combatState)

    assertEquals(GamePhase.GameOver, result.phase)

  @Test def testRunPublishesActionGridAndGameOverInOrder(): Unit =
    val subscriber = new RecordingSubscriber()
    val combatGrid = Grid().setCell(Entity.soldier(playerId, Faction.Player), lonePlayerPosition)
    val combatState = GameState(GamePhase.Combat, combatGrid, turnQueue = Seq(playerId))

    given ai: CharacterAI with
      override def determineNextAction(state: GameState, currentCharacterId: EntityId): GameAction =
        GameAction.Pass

    given engine: Engine with
      override def applyUnitAction(
          state: GameState,
          intent: CommandIntent
      ): EngineOutcome =
        EngineOutcome(
          nextState = state.copy(phase = GamePhase.GameOver, turnQueue = Seq.empty),
          events = Seq(
            DomainEvent.ActionApplied(intent.actorId, intent.action),
            DomainEvent.DamageInflicted(intent.actorId, EntityId("enemy-1"), 12),
            DomainEvent.UnitDied(EntityId("enemy-1"))
          )
        )

    GameLoop.subscribe(subscriber)
    val result = GameLoop.run(combatState)

    assertEquals(GamePhase.GameOver, result.phase)
    assertEquals(4, subscriber.receivedEvents.size)

    subscriber.receivedEvents(0) match
      case DomainEvent.ActionApplied(actorId, action) =>
        assertEquals(playerId, actorId)
        assertEquals(GameAction.Pass, action)
      case other => fail(s"Unexpected first event received: $other")

    subscriber.receivedEvents(1) match
      case DomainEvent.DamageInflicted(attackerId, targetId, amount) =>
        assertEquals(playerId, attackerId)
        assertEquals(EntityId("enemy-1"), targetId)
        assertEquals(12, amount)
      case other => fail(s"Unexpected second event received: $other")

    subscriber.receivedEvents(2) match
      case DomainEvent.UnitDied(unitId) => assertEquals(EntityId("enemy-1"), unitId)
      case other                        => fail(s"Unexpected third event received: $other")

    subscriber.receivedEvents(3) match
      case DomainEvent.GameEnded(finalState) => assertEquals(GamePhase.GameOver, finalState.phase)
      case other                             => fail(s"Unexpected fourth event received: $other")
