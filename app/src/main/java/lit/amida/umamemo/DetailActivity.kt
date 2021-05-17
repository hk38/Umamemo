package lit.amida.umamemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
        
        setBlueType(item?.myBlueType, binding.textMyBlue)
        setBlueType(item?.sireBlueType, binding.textSireBlue)
        setBlueType(item?.familyBlueType, binding.textFamilyBlue)
        setRedType(item?.myRedType, binding.textMyRed)
        setRedType(item?.sireRedType, binding.textSireRed)
        setRedType(item?.familyRedType, binding.textFamilyRed)
        
        binding.ratingMyBlue.rating = item?.myBlueValue ?: 0f
        binding.ratingSireBlue.rating = item?.sireBlueValue ?: 0f
        binding.ratingFamilyBlue.rating = item?.familyBlueValue ?: 0f
        binding.ratingMyRed.rating = item?.myRedValue ?: 0f
        binding.ratingSireRed.rating = item?.sireRedValue ?: 0f
        binding.ratingFamilyRed.rating = item?.familyRedValue ?: 0f

    }
    
    fun setBlueType(type: Int?, textView: TextView){
        if(type == null) return
        
        textView.text = when(type){
            BLUE_TYPE_SPEED -> "スピード"
            BLUE_TYPE_STAMINA -> "スタミナ"
            BLUE_TYPE_POWER -> "パワー"
            BLUE_TYPE_GUTS -> "根性"
            BLUE_TYPE_INTELLIGENT -> "賢さ"
            else -> ""
        }
    }

    fun setRedType(type: Int?, textView: TextView){
        if(type == null) return

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
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}