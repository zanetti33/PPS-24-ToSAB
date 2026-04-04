package it.unibo.tosab.model.entities

enum DamageType:
  case Physical, Magical  
  
case class DamageInstance(amount: Int, damageType: DamageType)