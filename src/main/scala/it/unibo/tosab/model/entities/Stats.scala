package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.AttackType.{Melee, Ranged}

enum AttackType:
  case Melee, Ranged, Area

case class Stats(
    currentHp: Int,
    physicalAttack: Int,
    magicalAttack: Int,
    physicalDefense: Int,
    magicalDefence: Int,
    movementDistance: Int,
    speed: Int,
    attackRange: Int,
    attackType: AttackType
)

object Stats:

  private val archerHp = 40
  private val archerPhysicalAttack = 10
  private val archerMagicalAttack = 5
  private val archerPhysicalDefense = 5
  private val archerMagicalDefense = 10
  private val archerMovementDistance = 1
  private val archerSpeed = 3
  private val archerAttackRange = 3
  private val archerAttackType = Ranged

  private val soldierHp = 50
  private val soldierPhysicalAttack = 15
  private val soldierMagicalAttack = 0
  private val soldierPhysicalDefense = 20
  private val soldierMagicalDefense = 0
  private val soldierMovementDistance = 1
  private val soldierSpeed = 2
  private val soldierAttackRange = 1
  private val soldierAttackType = Melee

  private val mageHp = 25
  private val magePhysicalAttack = 0
  private val mageMagicalAttack = 20
  private val magePhysicalDefense = 0
  private val mageMagicalDefense = 15
  private val mageMovementDistance = 1
  private val mageSpeed = 2
  private val mageAttackRange = 4
  private val mageAttackType = Ranged

  val baseArcherStats = Stats(
    archerHp,
    archerPhysicalAttack,
    archerMagicalAttack,
    archerPhysicalDefense,
    archerMagicalDefense,
    archerMovementDistance,
    archerSpeed,
    archerAttackRange,
    archerAttackType
  )

  val baseSoldierStats = Stats(
    soldierHp,
    soldierPhysicalAttack,
    soldierMagicalAttack,
    soldierPhysicalDefense,
    soldierMagicalDefense,
    soldierMovementDistance,
    soldierSpeed,
    soldierAttackRange,
    soldierAttackType
  )

  val baseMageStats = Stats(
    mageHp,
    magePhysicalAttack,
    mageMagicalAttack,
    magePhysicalDefense,
    mageMagicalDefense,
    mageMovementDistance,
    mageSpeed,
    mageAttackRange,
    mageAttackType
  )
