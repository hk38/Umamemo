package lit.amida.umamemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.RatingBar
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import lit.amida.umamemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val realm by lazy{
        Realm.getDefaultInstance()
    }
    private var adapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MyAdapter(this, object: MyAdapter.OnItemClickListner{
            override fun onItemClick(item: SaveData) {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("id", item.id)
                startActivity(intent)
            }
        })

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            val intent = Intent(applicationContext, EditActivity::class.java)
            startActivity(intent)
        }

        val blueRatingList = listOf(binding.ratingBlue1, binding.ratingBlue2, binding.ratingBlue3)
        val blueRadioList = listOf(binding.blueRadioGroup1, binding.blueRadioGroup2, binding.blueRadioGroup3)
        val redRatingList = listOf(binding.ratingRed1, binding.ratingRed2, binding.ratingRed3)
        val redRadioList = listOf(binding.redRadioGroup1, binding.redRadioGroup2, binding.redRadioGroup3)

        blueRatingList.forEach { it.setOnRatingBarChangeListener { _, _, _ ->  setList(blueRatingList, blueRadioList, redRatingList, redRadioList)} }
        blueRadioList.forEach { it.setOnCheckedChangeListener { _, _ -> setList(blueRatingList, blueRadioList, redRatingList, redRadioList) } }
        redRatingList.forEach { it.setOnRatingBarChangeListener { _, _, _ ->  setList(blueRatingList, blueRadioList, redRatingList, redRadioList)} }
        redRadioList.forEach { it.setOnCheckedChangeListener { _, _ -> setList(blueRatingList, blueRadioList, redRatingList, redRadioList) } }

        binding.buttonReset.setOnClickListener {
            blueRatingList.forEach { it.rating = 1f }
            blueRadioList.forEach { it.clearCheck() }
            redRatingList.forEach { it.rating = 1f }
            redRadioList.forEach { it.clearCheck() }
            adapter?.setList(realm.where(SaveData::class.java).findAll())
        }

        binding.bottomSheet.setOnClickListener {  }

    }

    override fun onResume() {
        super.onResume()
        adapter?.setList(realm.where(SaveData::class.java).findAll().sort("id"))
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val list = listOf(binding.blueScrollView1, binding.blueScrollView2, binding.blueScrollView3, binding.redScrollView1, binding.redScrollView2, binding.redScrollView3)

        list.forEach {
            val params = it.layoutParams
            params.width = (binding.bottomSheet.width / 2.5).toInt()
            it.layoutParams = params
        }

        Log.d("width", "bottomSheet:${binding.bottomSheet.width}, screen:${binding.blueScrollView1.layoutParams.width}")
    }

    fun setList(blueRatingList: List<RatingBar>, blueRadioList: List<RadioGroup>, redRatingList: List<RatingBar>, redRadioList: List<RadioGroup>){
        val blueArray = Array(5){0}
        val redArray = Array(10){0}

        for(i in blueRadioList.indices){
            for(j in blueArray.indices){
                if(blueRadioList[i].checkedRadioButtonId == blueRadioList[i][j].id) blueArray[j] += blueRatingList[i].rating.toInt()
            }
        }

        for(i in redRadioList.indices){
            for(j in redArray.indices){
                if(redRadioList[i].checkedRadioButtonId == redRadioList[i][j].id) redArray[j] += redRatingList[i].rating.toInt()
            }
        }

        adapter?.setList(realm.where(SaveData::class.java)
            .greaterThanOrEqualTo("sumOfSpeed", blueArray[0])
            .greaterThanOrEqualTo("sumOfStamina", blueArray[1])
            .greaterThanOrEqualTo("sumOfPower", blueArray[2])
            .greaterThanOrEqualTo("sumOfGuts", blueArray[3])
            .greaterThanOrEqualTo("sumOfIntelligent", blueArray[4])
            .greaterThanOrEqualTo("sumOfTurf", redArray[0])
            .greaterThanOrEqualTo("sumOfDirt", redArray[1])
            .greaterThanOrEqualTo("sumOfShort", redArray[2])
            .greaterThanOrEqualTo("sumOfMile", redArray[3])
            .greaterThanOrEqualTo("sumOfMiddle", redArray[4])
            .greaterThanOrEqualTo("sumOfLong", redArray[5])
            .greaterThanOrEqualTo("sumOfFr", redArray[6])
            .greaterThanOrEqualTo("sumOfStalker", redArray[7])
            .greaterThanOrEqualTo("sumOfSotp", redArray[8])
            .greaterThanOrEqualTo("sumOfOffer", redArray[9])
            .findAll()
        )
    }
}