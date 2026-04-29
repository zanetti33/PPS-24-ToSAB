package it.unibo.tosab.model.entities

/** Represents the type of damage dealt.
  */
enum DamageType:
  case Physical, Magical

/** Represents a damage instance with amount and type.
  *
  * @param amount
  *   the damage value
  * @param damageType
  *   the type of damage (Physical or Magical)
  */
case class DamageInstance(amount: Int, damageType: DamageType)
