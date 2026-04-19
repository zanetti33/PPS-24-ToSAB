package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.CombatRules.calculatedAgainst

//Type Class
trait Damageable[T]:
  extension (entity: T) def takeDamage(amount: DamageInstance): T

//given
object Damageable:
  private final val MinimumRemainingHp = 0

  private def clampHp(value: Int): Int = Math.max(MinimumRemainingHp, value)

  given Damageable[Character] with
    extension (c: Character)
      def takeDamage(amount: DamageInstance): Character =
        val damageTaken = amount.calculatedAgainst(c.stats)
        val newHp = clampHp(c.stats.currentHp - damageTaken)
        c.copy(stats = c.stats.copy(currentHp = newHp))

  given Damageable[Obstacle] with
    extension (o: Obstacle)
      def takeDamage(damage: DamageInstance): Obstacle = o.hp match
        case Some(currentHp) =>
          val newHp = clampHp(currentHp - damage.amount)
          o.copy(hp = Some(newHp))
        case None => o
