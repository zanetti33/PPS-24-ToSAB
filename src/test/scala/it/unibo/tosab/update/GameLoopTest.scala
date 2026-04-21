package it.unibo.tosab.update

import org.junit.*
import org.junit.Assert.*
import it.unibo.tosab.model.{GameAction, GamePhase, GameState}
import it.unibo.tosab.model.ai.CharacterAI.{CharacterAI, DoesNothingCharacterAI}
import it.unibo.tosab.model.engine.Engine.{
  DoesNothingEngine,
  Engine,
  ImmediatelyEndEngine,
  TurnBasedCombatEngine
}
import it.unibo.tosab.model.entities.{Entity, Faction}
import it.unibo.tosab.model.grid.Grid

class GameLoopTest:

  private val playerId = "player-1"
  private val lonePlayerPosition = (4, 4)

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
    val receivedEvents = scala.collection.mutable.ArrayBuffer.empty[GameLoopEvent]

    override def update(event: GameLoopEvent): Unit =
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
      case GameLoopEvent.GameEnded(finalState) => assertEquals(gameOver, finalState)
      case other => fail(s"Unexpected event received: $other")

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
      override def determineNextAction(state: GameState, currentCharacterId: String): GameAction =
        GameAction.Pass

    given engine: Engine with
      override def applyUnitAction(state: GameState, actorId: String, action: GameAction): GameState =
        state.copy(phase = GamePhase.GameOver, turnQueue = Seq.empty)

    GameLoop.subscribe(subscriber)
    val result = GameLoop.run(combatState)

    assertEquals(GamePhase.GameOver, result.phase)
    assertEquals(3, subscriber.receivedEvents.size)

    subscriber.receivedEvents(0) match
      case GameLoopEvent.ActionChosen(actorId, actor, action) =>
        assertEquals(playerId, actorId)
        assertTrue(actor.exists(_.id == playerId))
        assertEquals(GameAction.Pass, action)
      case other => fail(s"Unexpected first event received: $other")

    subscriber.receivedEvents(1) match
      case GameLoopEvent.GridUpdated(updatedGrid) => assertEquals(combatGrid, updatedGrid)
      case other                                  => fail(s"Unexpected second event received: $other")

    subscriber.receivedEvents(2) match
      case GameLoopEvent.GameEnded(finalState) => assertEquals(GamePhase.GameOver, finalState.phase)
      case other                               => fail(s"Unexpected third event received: $other")

