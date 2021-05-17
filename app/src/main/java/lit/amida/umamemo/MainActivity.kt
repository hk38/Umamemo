package lit.amida.umamemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
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

    }

    override fun onResume() {
        super.onResume()
        adapter?.setList(realm.where(SaveData::class.java).findAll().sort("id"))
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}