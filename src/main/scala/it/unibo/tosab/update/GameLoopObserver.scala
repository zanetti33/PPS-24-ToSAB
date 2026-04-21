package it.unibo.tosab.update

import it.unibo.tosab.model.DomainEvent

/** Observer in the classic Observer pattern.
  */
trait GameLoopSubscriber:
  def update(event: DomainEvent): Unit

/** Publisher that manages subscribers interested in game loop events.
  */
trait GameLoopPublisher:
  private var subscribers: Seq[GameLoopSubscriber] = Seq.empty

  def subscribe(subscriber: GameLoopSubscriber): Unit =
    if !subscribers.contains(subscriber) then subscribers = subscribers :+ subscriber

  def unsubscribe(subscriber: GameLoopSubscriber): Unit =
    subscribers = subscribers.filterNot(_ == subscriber)

  def clearSubscribers(): Unit =
    subscribers = Seq.empty

  protected def publish(event: DomainEvent): Unit =
    subscribers.foreach(_.update(event))
