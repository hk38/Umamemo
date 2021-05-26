package lit.amida.umamemo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lit.amida.umamemo.databinding.ItemListBinding

class MyAdapter(private val context: Context, private val listener: OnItemClickListner): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    //RecyclerViewが所持する表示データのリスト．Realmから引き出したListをここに設定する．
    val items: MutableList<SaveData> = mutableListOf()

    // RecyclerViewに表示するレイアウトを引数として受け取り，コード側で保持するためのクラス
    class MyViewHolder(val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root)

    // list_item.xmlのレイアウトファイルをコード側に持ってきてViewHolderに渡す
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    // RecyclerViewのn番目の要素に保持しているデータのn番目の要素を結びつける
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        var sumOfBlueFactor = 0f
        var sumOfRedFactor = 0f

        item.blueFactors.forEach { sumOfBlueFactor += it.count }

        item.redFactors.forEach{ sumOfRedFactor += it.count }

        holder.binding.container.setOnClickListener { listener.onItemClick(item) }
        holder.binding.textName.text = item.name
        holder.binding.textRank.text = item.rank
        holder.binding.textPoint.text = item.point.toString()
        holder.binding.textBlue.text = "青因子合計：${sumOfBlueFactor.toInt()}"
        holder.binding.textRed.text = "赤因子合計：${sumOfRedFactor.toInt()}"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // RecyclerViewの要素をタップするためのもの
    interface OnItemClickListner{
        fun onItemClick(item: SaveData)
    }

    // RecyclerViewのリスト（items）を空にして，受け取ったリスト（list）の内容に差し替える
    fun setList(list: List<SaveData>){
        items.clear()
        items.addAll(list)
        // RecyclerViewに要素が変わったことを通知し，再描画させる
        notifyDataSetChanged()
    }
}