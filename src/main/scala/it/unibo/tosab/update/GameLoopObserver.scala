package it.unibo.tosab.update

import it.unibo.tosab.model.{GameAction, GameState}
import it.unibo.tosab.model.entities.Character
import it.unibo.tosab.model.grid.Grid

/** Domain events emitted by the game loop.
  */
enum GameLoopEvent:
  case ActionChosen(actorId: String, actor: Option[Character], action: GameAction)
  case GridUpdated(grid: Grid)
  case GameEnded(finalState: GameState)

/** Observer in the classic Observer pattern.
  */
trait GameLoopSubscriber:
  def update(event: GameLoopEvent): Unit

/** Publisher that manages subscribers interested in game loop events.
  */
trait GameLoopPublisher:
  private var subscribers: Seq[GameLoopSubscriber] = Seq.empty

  def subscribe(subscriber: GameLoopSubscriber): Unit =
    if !subscribers.contains(subscriber) then
      subscribers = subscribers :+ subscriber

  def unsubscribe(subscriber: GameLoopSubscriber): Unit =
    subscribers = subscribers.filterNot(_ == subscriber)

  def clearSubscribers(): Unit =
    subscribers = Seq.empty

  protected def publish(event: GameLoopEvent): Unit =
    subscribers.foreach(_.update(event))
