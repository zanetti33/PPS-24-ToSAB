package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.CombatRules.calculatedAgainst

/** Trait defining the ability to receive and process damage.
  *
  * @tparam T
  *   the type of entity that can take damage
  */
trait Damageable[T]:
  extension (entity: T) def takeDamage(amount: DamageInstance): T

object Damageable:
  private final val MinimumRemainingHp = 0
  private def clampHp(value: Int): Int = Math.max(MinimumRemainingHp, value)

  given Damageable[Character] with
    extension (c: Character)
      /** Applies damage to a character considering its stats.
        *
        * @param amount
        *   the damage instance to apply
        * @return
        *   a new character with updated health after damage
        */
      def takeDamage(amount: DamageInstance): Character =
        val damageTaken = amount.calculatedAgainst(c.stats)
        val newHp = clampHp(c.stats.currentHp - damageTaken)
        c.copy(stats = c.stats.copy(currentHp = newHp))

  given Damageable[Obstacle] with
    extension (o: Obstacle)
      /** Applies direct damage to an obstacle if it has hp.
        *
        * @param damage
        *   the damage instance to apply
        * @return
        *   a new obstacle with updated health after damage
        */
      def takeDamage(damage: DamageInstance): Obstacle = o.hp match
        case Some(currentHp) =>
          val newHp = clampHp(currentHp - damage.amount)
          o.copy(hp = Some(newHp))
        case None => o
