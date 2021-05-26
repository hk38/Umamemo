package lit.amida.umamemo

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class SaveData (
    @PrimaryKey open var id:Long =  System.currentTimeMillis(),
    open var rank: String = "",
    open var point: Int = 0,
    open var name: String = "",
    open var blueFactors: RealmList<FactorData> = RealmList<FactorData>(),
    open var redFactors: RealmList<FactorData> = RealmList<FactorData>(),
    open var sumOfSpeed: Int = 0,
    open var sumOfStamina: Int = 0,
    open var sumOfPower: Int = 0,
    open var sumOfGuts: Int = 0,
    open var sumOfIntelligent: Int = 0,
    open var sumOfTurf: Int = 0,
    open var sumOfDirt: Int = 0,
    open var sumOfShort: Int  = 0,
    open var sumOfMile: Int = 0,
    open var sumOfMiddle: Int = 0,
    open var sumOfLong: Int = 0,
    open var sumOfFr: Int = 0,
    open var sumOfStalker: Int = 0,
    open var sumOfSotp: Int = 0,
    open var sumOfOffer: Int = 0
): RealmObject()

open class FactorData(
    open var type: Int = 0,
    open var count: Float = 0f
): RealmObject()

const val BLUE_TYPE_SPEED = 1
const val BLUE_TYPE_STAMINA = 2
const val BLUE_TYPE_POWER = 3
const val BLUE_TYPE_GUTS = 4
const val BLUE_TYPE_INTELLIGENT = 5

const val RED_TYPE_TURF = 1
const val RED_TYPE_DIRT = 2

const val RED_TYPE_SHORT = 3
const val RED_TYPE_MILE = 4
const val RED_TYPE_MIDDLE = 5
const val RED_TYPE_LONG = 6
const val RED_TYPE_FR = 7
const val RED_TYPE_STALKER = 8
const val RED_TYPE_SOTP = 9
const val RED_TYPE_OFFER = 10