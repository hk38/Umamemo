package lit.amida.umamemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import io.realm.Realm
import lit.amida.umamemo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val realm by lazy{
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = realm.where(SaveData::class.java).equalTo("id", intent.getLongExtra("id", 0L)).findFirst()

        if(item == null) finish()

        binding.textRank.text = item?.rank
        binding.textPoint.text = item?.point.toString()
        binding.textName.text = item?.name
        
        val textBlueList = listOf(binding.textMyBlue, binding.textSireBlue, binding.textFamilyBlue)
        val ratingBlueList = listOf(binding.ratingMyBlue, binding.ratingSireBlue, binding.ratingFamilyBlue)
        val textRedList = listOf(binding.textMyRed, binding.textSireRed, binding.textFamilyRed)
        val ratingRedList = listOf(binding.ratingMyRed, binding.ratingSireRed, binding.ratingFamilyRed)

        item?.blueFactors?.forEachIndexed { i, factorData -> setBlue(factorData.type, textBlueList[i], factorData.value, ratingBlueList[i]) }
        item?.redFactors?.forEachIndexed { i, factorData -> setRed(factorData.type, textRedList[i], factorData.value, ratingRedList[i]) }
    }
    
    fun setBlue(type: Int, textView: TextView, value: Float, ratingBar: RatingBar){
        textView.text = when(type){
            BLUE_TYPE_SPEED -> "スピード"
            BLUE_TYPE_STAMINA -> "スタミナ"
            BLUE_TYPE_POWER -> "パワー"
            BLUE_TYPE_GUTS -> "根性"
            BLUE_TYPE_INTELLIGENT -> "賢さ"
            else -> ""
        }

        ratingBar.rating = value
    }

    fun setRed(type: Int, textView: TextView, value: Float, ratingBar: RatingBar){
        textView.text = when(type){
            RED_TYPE_TURF -> "芝"
            RED_TYPE_DIRT -> "ダート"
            RED_TYPE_SHORT -> "短距離"
            RED_TYPE_MILE -> "マイル"
            RED_TYPE_MIDDLE -> "中距離"
            RED_TYPE_LONG -> "長距離"
            RED_TYPE_FR -> "逃げ"
            RED_TYPE_STALKER -> "先行"
            RED_TYPE_SOTP -> "差し"
            RED_TYPE_OFFER -> "追込"
            else -> ""
        }

        ratingBar.rating = value
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}