package lit.amida.umamemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import lit.amida.umamemo.databinding.ActivityEditBinding
import java.util.*

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    val realm by lazy{
        Realm.getDefaultInstance()
    }

    val blueTypeList = arrayOf("スピード", "スタミナ", "パワー", "根性", "賢さ")
    val redTypeList = arrayOf("芝", "ダート", "短距離", "マイル", "中距離", "長距離", "逃げ", "先行", "差し", "追込")
    val blueArray = Array(3){0}
    val redArray = Array(3){0}
    
    val blueButtons = mutableListOf<Button>()
    val redButtons = mutableListOf<Button>()
    val blueRatings = mutableListOf<RatingBar>()
    val redRatings = mutableListOf<RatingBar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        blueButtons.add(binding.buttonMyBlue)
        blueButtons.add(binding.buttonSireBlue)
        blueButtons.add(binding.buttonFamilyBlue)
        redButtons.add(binding.buttonMyRed)
        redButtons.add(binding.buttonSireRed)
        redButtons.add(binding.buttonFamilyRed)
        blueRatings.add(binding.ratingMyBlue)
        blueRatings.add(binding.ratingSireBlue)
        blueRatings.add(binding.ratingFamilyBlue)
        redRatings.add(binding.ratingMyRed)
        redRatings.add(binding.ratingSireRed)
        redRatings.add(binding.ratingFamilyRed)

        blueButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                        .setTitle("リスト選択ダイアログ")
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
                        .setTitle("リスト選択ダイアログ")
                        .setItems(redTypeList) { _, which ->
                            button.text = redTypeList[which]
                            redArray[index] = which+1
                        }
                        .show()
            }
        }

        binding.fab.setOnClickListener { finish() }
    }

    override fun onPause() {
        super.onPause()
        realm.executeTransaction {
            val item = it.createObject(SaveData::class.java, System.currentTimeMillis())
            item.rank = binding.editInputRank.text.toString()
            if(binding.editInputPoint.text.toString().isBlank()) item.point = 0
            else item.point = binding.editInputPoint.text.toString().toInt()
            item.name = binding.editInputName.text.toString()

            item.myBlueType = blueArray[0]
            item.myBlueValue = binding.ratingMyBlue.rating
            item.sireBlueType = blueArray[1]
            item.sireBlueValue = binding.ratingSireBlue.rating
            item.familyBlueType = blueArray[2]
            item.familyBlueValue = binding.ratingFamilyBlue.rating

            item.myRedType = redArray[0]
            item.myRedValue = binding.ratingMyRed.rating
            item.sireRedType = redArray[1]
            item.sireRedValue = binding.ratingSireRed.rating
            item.familyRedType = redArray[2]
            item.familyRedValue = binding.ratingFamilyRed.rating
            
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
    }
}