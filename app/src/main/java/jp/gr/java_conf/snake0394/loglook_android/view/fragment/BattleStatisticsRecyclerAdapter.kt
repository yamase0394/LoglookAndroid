package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem

/**
 * Created by snake0394 on 2016/12/08.
 */

class BattleStatisticsRecyclerAdapter : RecyclerView.Adapter<BattleStatisticsRecyclerAdapter.BattleStatisticsRecyclerViewHolder>() {

    private val TAG = "BattleStatisticsRecyclerAdapter"

    private val dataList = arrayListOf<ListItem>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BattleStatisticsRecyclerViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_equipment_cardview, viewGroup, false)
        return BattleStatisticsRecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: BattleStatisticsRecyclerViewHolder, i: Int) {
        val listItem = dataList.get(i)
        viewHolder.bind(listItem)
    }

    override fun getItemCount(): Int = dataList.size

    fun setItems(newItemList: List<MySlotItem>, sortType: String, order: String) {

    }

    class ListItem constructor(mySlotItemId: Int) {

    }

    class BattleStatisticsRecyclerViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(listItem: ListItem) {

        }
    }
}