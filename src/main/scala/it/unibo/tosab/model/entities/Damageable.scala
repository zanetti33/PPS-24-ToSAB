package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.CombatRules.calculatedAgainst

//Type Class
trait Damageable[T]:
  extension (entity: T) def takeDamage(amount: DamageInstance): T

//given
object Damageable:
  given Damageable[Character] with
    extension (c: Character)
      def takeDamage(amount: DamageInstance): Character =
        val damageTaken = amount.calculatedAgainst(c.stats)
        c.copy(stats = c.stats.copy(currentHp = c.stats.currentHp - damageTaken))

  given Damageable[Obstacle] with
    extension (o: Obstacle)
      def takeDamage(damage: DamageInstance): Obstacle = o.hp match
        case Some(currentHp) =>
          val newHp = Math.max(0, currentHp - damage.amount)
          o.copy(hp = Some(newHp))
        case None => o
