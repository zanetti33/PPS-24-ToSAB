package it.unibo.tosab.model.entities

import it.unibo.tosab.model.entities.AttackType.{Melee, Ranged}

/** Represents the type of attack a character can perform.
  */
enum AttackType:
  case Melee, Ranged, Area

/** Represents the combat and movement statistics of a character.
  *
  * @param currentHp
  *   current health points
  * @param physicalAttack
  *   physical attack value
  * @param magicalAttack
  *   magical attack value
  * @param physicalDefense
  *   resistance to physical damage
  * @param magicalDefence
  *   resistance to magical damage
  * @param movementDistance
  *   maximum tiles moved per turn
  * @param speed
  *   initiative value for turn order
  * @param attackRange
  *   maximum distance for attacks
  * @param attackType
  *   the type of attack performed
  */
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
  private val archerMovementDistance = 2
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

  /** Base statistics for archer characters. */
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

  /** Base statistics for soldier characters. */
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

  /** Base statistics for mage characters. */
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
