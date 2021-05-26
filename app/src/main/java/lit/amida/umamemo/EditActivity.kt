package lit.amida.umamemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmList
import lit.amida.umamemo.databinding.ActivityEditBinding
import java.util.*

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    val realm by lazy{
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blueTypeList = arrayOf("スピード", "スタミナ", "パワー", "根性", "賢さ")
        val redTypeList = arrayOf("芝", "ダート", "短距離", "マイル", "中距離", "長距離", "逃げ", "先行", "差し", "追込")
        val blueArray = Array(3){0}
        val redArray = Array(3){0}
        val blueButtons = listOf(binding.buttonMyBlue, binding.buttonSireBlue, binding.buttonFamilyBlue)
        val redButtons = listOf(binding.buttonMyRed, binding.buttonSireRed, binding.buttonFamilyRed)
        val blueRatings = listOf(binding.ratingMyBlue, binding.ratingSireBlue, binding.ratingFamilyBlue)
        val redRatings = listOf(binding.ratingMyRed, binding.ratingSireRed, binding.ratingFamilyRed)

        blueButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                        .setTitle("青因子の種類を選択")
                        .setItems(blueTypeList) { _, which ->
                            button.text = blueTypeList[which]
                            blueArray[index] = which+1
                        }
                        .show()
            }
        }

        redButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                        .setTitle("赤因子の種類を選択")
                        .setItems(redTypeList) { _, which ->
                            button.text = redTypeList[which]
                            redArray[index] = which+1
                        }
                        .show()
            }
        }

        binding.fab.setOnClickListener {
            realm.executeTransaction {
                val item = it.createObject(SaveData::class.java, System.currentTimeMillis())
                item.rank = binding.editInputRank.text.toString()
                if(binding.editInputPoint.text.toString().isBlank()) item.point = 0
                else item.point = binding.editInputPoint.text.toString().toInt()
                item.name = binding.editInputName.text.toString()

                val blueFactor1 = it.createObject(FactorData::class.java)
                blueFactor1.type = blueArray[0]
                blueFactor1.count = binding.ratingMyBlue.rating
                val blueFactor2 = it.createObject(FactorData::class.java)
                blueFactor2.type = blueArray[1]
                blueFactor2.count = binding.ratingSireBlue.rating
                val blueFactor3 = it.createObject(FactorData::class.java)
                blueFactor3.type = blueArray[2]
                blueFactor3.count = binding.ratingFamilyBlue.rating
                item.blueFactors = RealmList(blueFactor1, blueFactor2, blueFactor3)

                val redFactor1 = it.createObject(FactorData::class.java)
                redFactor1.type = redArray[0]
                redFactor1.count = binding.ratingMyRed.rating
                val redFactor2 = it.createObject(FactorData::class.java)
                redFactor2.type = redArray[1]
                redFactor2.count = binding.ratingSireRed.rating
                val redFactor3 = it.createObject(FactorData::class.java)
                redFactor3.type = redArray[2]
                redFactor3.count = binding.ratingFamilyRed.rating
                item.redFactors = RealmList(redFactor1, redFactor2, redFactor3)

                blueArray.forEachIndexed { index, i ->
                    when(i){
                        BLUE_TYPE_SPEED -> item.sumOfSpeed += blueRatings[index].rating.toInt()
                        BLUE_TYPE_STAMINA -> item.sumOfStamina += blueRatings[index].rating.toInt()
                        BLUE_TYPE_POWER -> item.sumOfPower += blueRatings[index].rating.toInt()
                        BLUE_TYPE_GUTS -> item.sumOfGuts += blueRatings[index].rating.toInt()
                        BLUE_TYPE_INTELLIGENT -> item.sumOfIntelligent += blueRatings[index].rating.toInt()
                    }
                }

                redArray.forEachIndexed{ index, i ->
                    when(i){
                        RED_TYPE_TURF -> item.sumOfTurf += redRatings[index].rating.toInt()
                        RED_TYPE_DIRT -> item.sumOfDirt += redRatings[index].rating.toInt()
                        RED_TYPE_SHORT -> item.sumOfShort += redRatings[index].rating.toInt()
                        RED_TYPE_MILE -> item.sumOfMile += redRatings[index].rating.toInt()
                        RED_TYPE_MIDDLE -> item.sumOfMiddle += redRatings[index].rating.toInt()
                        RED_TYPE_LONG -> item.sumOfLong += redRatings[index].rating.toInt()
                        RED_TYPE_FR -> item.sumOfFr += redRatings[index].rating.toInt()
                        RED_TYPE_STALKER -> item.sumOfStalker += redRatings[index].rating.toInt()
                        RED_TYPE_SOTP -> item.sumOfSotp += redRatings[index].rating.toInt()
                        RED_TYPE_OFFER -> item.sumOfOffer += redRatings[index].rating.toInt()

                    }
                }
            }

            finish()
        }
    }
}