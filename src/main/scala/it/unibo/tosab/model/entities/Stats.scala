package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.AttackType.{Melee, Ranged}

enum AttackType:
  case Melee, Ranged, Area

case class Stats(
    currentHp: Int = 100,
    physicalAttack: Int = 0,
    magicalAttack: Int = 0,
    physicalDefense: Int = 0,
    magicalDefence: Int = 0,
    movementDistance: Int = 0,
    speed: Int = 0,
    attackRange: Int = 0,
    attackType: AttackType = Melee
)

object Stats:

  private val archerHp = 40
  private val archerPhysicalAttack = 10
  private val archerMagicalAttack = 5
  private val archerPhysicalDefense = 5
  private val archerMagicalDefense = 10
  private val archerMovementDistance = 1
  private val archerSpeed = 3
  private val archerAttackRange = 2
  private val archerAttackType = Ranged

  private val soldierHp = 50
  private val soldierPhysicalAttack = 20
  private val soldierMagicalAttack = 0
  private val soldierPhysicalDefense = 15
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
  private val mageAttackRange = 3
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
