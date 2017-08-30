package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.graphics.Color
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import com.google.common.collect.Multiset
import com.google.common.collect.TreeMultiset
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.SlotItemUtility
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3
import kotlinx.android.synthetic.main.layout_equipment_cardview.view.*
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * Created by snake0394 on 2016/12/08.
 */

class EquipmentAdapter(private var sortType: String, private var order: String) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {


    companion object {
        private val TAG = "EquipmentAdapter"
        private val realm = Realm.getDefaultInstance()
        /**
         * key=装備ID value=装備している艦娘ID
         * */
        private val mountedEquip: SparseIntArray = SparseIntArray()
    }

    private val sortedList = SortedList(ListItem::class.java, SortedListCallback(this, sortType, order))
    private val dataList = arrayListOf<ListItem>()

    init {
        Logger.d(TAG, "init started")
        thread {
            synchronized(mountedEquip) {
                Realm.getDefaultInstance().use { realm ->
                    for (myShip in realm.where(MyShip::class.java).findAll()) {
                        myShip.slot
                                .filter { it.value > 0 }
                                .forEach { mountedEquip.put(it.value, myShip.id) }

                        if (myShip.slotEx > 0) {
                            mountedEquip.put(myShip.slotEx, myShip.id)
                        }
                    }
                }
            }
        }
        Logger.d(TAG, "init ended")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): EquipmentViewHolder {
        //Logger.d(TAG, "onCreateViewHolder")
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_equipment_cardview, viewGroup, false)
        //Logger.d(TAG, "onCreateViewHolder ended")
        return EquipmentViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: EquipmentViewHolder, i: Int) {
        //Logger.d(TAG, "onBindViewHolder")
        val listItem = dataList.get(i)
        viewHolder.bind(listItem)
        //Logger.d(TAG, "onBindViewHolder ended")
    }

    override fun getItemCount(): Int = dataList.size

    fun setItems(newItemList: List<MySlotItem>) {
        Logger.d(TAG, "setItems started")

        val alreadyAddedMap = hashMapOf<Int, Int>()

        for(newItem in newItemList) {
            val id = newItem.id
            val mstId = newItem.mstId

            if(alreadyAddedMap.contains(mstId)){
                val idx = alreadyAddedMap[mstId]
                val exist = dataList[idx!!]
                if(newItem.level == exist.level && newItem.alv == exist.alv) {
                    exist.count++
                    val shipId = mountedEquip.get(id, -1)
                    if (shipId != -1) {
                        exist.putShipId(shipId)
                    }
                    continue
                }
            }

            val listItem = ListItem(id)
            val shipId = mountedEquip.get(id, -1)
            if (shipId != -1) {
                listItem.putShipId(shipId)
            }
            alreadyAddedMap.put(listItem.mstId, dataList.size)
            dataList.add(listItem)
        }

        sortData(sortType)

        Logger.d(TAG, "setItems ended")
    }

    fun sortData (sortType: String){
        this.sortType = sortType
        Collections.sort(dataList, kotlin.Comparator { item1: ListItem, item2: ListItem ->
            var result = when (sortType) {
                "名前" -> item1.name.compareTo(item2.name).toFloat()
                "改修度" -> (item1.level - item2.level).toFloat()
                "ID" -> (item1.mstId - item2.mstId).toFloat()
                "火力" -> item1.houg + item1.hougImproved - item2.houg - item2.hougImproved
                "雷装" -> item1.raig + item1.raigImproved - item2.raig - item2.raigImproved
                "爆装" -> item1.baku + item1.bakuImproved - item2.baku - item2.bakuImproved
                "対空" -> item1.tyku + item1.tykuImproved - item2.tyku - item2.tykuImproved
                "対潜" -> item1.tais + item1.taisImproved - item2.tais - item2.taisImproved
                "索敵" -> item1.saku + item1.sakuImproved - item2.saku - item2.sakuImproved
                "命中" -> item1.houm + item1.houmImproved - item2.houm - item2.houmImproved
                "回避" -> item1.houk + item1.houkImproved - item2.houk - item2.houkImproved
                "装甲" -> item1.souk + item1.soukImproved - item2.souk - item2.soukImproved
                "加重対空" -> item1.adjustedAA + item1.adjustedAAImproved - item2.adjustedAA - item2.adjustedAAImproved
                "艦隊対空" -> item1.adjustedFleetAA + item1.adjustedFleetAAImproved - item2.adjustedFleetAA - item2.adjustedFleetAAImproved
                else -> throw IllegalArgumentException("${this.sortType} does not exist")
            }

            when (order) {
                "降順" -> result *= -1f
            }

            /*
            //同じだった場合はマスターID→熟練度の順番でソートする
            if (result == 0f) {
                if (item1.mstId - item2.mstId == 0) {
                    result = (item2.level - item1.level).toFloat()
                    if (result == 0f) {
                        result = (item2.alv - item1.alv).toFloat()
                    }
                }
            }
            */

            //resultをintに変換し返す
            return@Comparator when {
                result > 0 -> 1
                result == 0f -> 0
                result < 0 -> -1
                else -> throw IllegalArgumentException("result=$result")
            }
        })
        notifyDataSetChanged()
    }

    class ListItem internal constructor(internal var mySlotItemId: Int) {
        var count: Int = 0
        var equipShipSet: Multiset<Int>? = null

        val mySlotItem: MySlotItem
        val mstSlotItem: MstSlotitem

        val slotType3: Int
        val mstId: Int
        val level: Int
        val alv: Int
        val name: String
        val houg: Int
        val hougImproved: Float
        val raig: Int
        val raigImproved: Float
        val baku: Int
        val bakuImproved: Float
        val tyku: Int
        val tykuImproved: Float
        val tais: Int
        val taisImproved: Float
        val saku: Int
        val sakuImproved: Float
        val houm: Int
        val houmImproved: Float
        val houk: Int
        val houkImproved: Float
        val leng: Int
        val souk: Int
        val soukImproved: Float
        val adjustedAA: Float
        val adjustedAAImproved: Float
        val adjustedFleetAA: Float
        val adjustedFleetAAImproved: Float

        init {
            this.count = 1
            val realm = Realm.getDefaultInstance()
            mySlotItem = realm.where(MySlotItem::class.java).equalTo("id", mySlotItemId).findFirst()
            mstSlotItem = realm.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()

            slotType3 = mstSlotItem.type[3].value
            mstId = mySlotItem.mstId
            level = mySlotItem.level
            alv = mySlotItem.alv
            name = mstSlotItem.name
            houg = mstSlotItem.houg
            hougImproved = SlotItemUtility.getShellingImprovementFirepower(mstSlotItem, mySlotItem.level)
            raig = mstSlotItem.raig
            raigImproved = SlotItemUtility.getTorpedoSalvoImprovementPower(mstSlotItem, mySlotItem.level)
            baku = mstSlotItem.baku
            bakuImproved = SlotItemUtility.getImprovementDivebomb(mstSlotItem, mySlotItem.level)
            tyku = mstSlotItem.tyku
            tykuImproved = SlotItemUtility.getImprovementAA(mstSlotItem, mySlotItem.level)
            tais = mstSlotItem.tais
            taisImproved = SlotItemUtility.getImprovementASW(mstSlotItem, mySlotItem.level)
            saku = mstSlotItem.saku
            sakuImproved = SlotItemUtility.getImprovementLOS(mstSlotItem, mySlotItem.level)
            houm = mstSlotItem.houm
            houmImproved = SlotItemUtility.getImprovementAccuracy(mstSlotItem, mySlotItem.level)
            houk = mstSlotItem.houk
            houkImproved = SlotItemUtility.getImprovementEvation(mstSlotItem, mySlotItem.level)
            leng = mstSlotItem.leng
            souk = mstSlotItem.souk
            soukImproved = SlotItemUtility.getImprovementArmor(mstSlotItem, mySlotItem.level)
            adjustedAA = SlotItemUtility.getAdjustedAA(mstSlotItem)
            adjustedAAImproved = SlotItemUtility.getImprovementAdjustedAA(mstSlotItem, mySlotItem.level)
            adjustedFleetAA = SlotItemUtility.getAdjustedFleetAA(mstSlotItem)
            adjustedFleetAAImproved = SlotItemUtility.getImprovementAdjustedFleetAA(mstSlotItem, mySlotItem.level)
            realm.close()
        }

        internal fun putShipId(myShipId: Int) {
            if (this.equipShipSet == null) {
                this.equipShipSet = TreeMultiset.create { data1, data2 ->
                    val realm = Realm.getDefaultInstance()
                    val results = realm.where(MyShip::class.java)
                            .`in`("id", arrayOf(data1, data2))
                            .findAll()
                    val myShip1 = results.where()
                            .equalTo("id", data1)
                            .findFirst()
                    val myShip2 = results.where()
                            .equalTo("id", data2)
                            .findFirst()
                    //レベルの降順
                    var result = myShip2.lv - myShip1.lv
                    if (result == 0) {
                        //マスターIDの降順
                        result = myShip2.shipId - myShip1.shipId
                        if (result == 0) {
                            //艦娘IDの昇順
                            result = myShip1.id - myShip2.id
                        }
                    }
                    realm.close()
                    result
                }
            }
            this.equipShipSet!!.add(myShipId)
        }
    }

    private class SortedListCallback constructor(private val adapter: RecyclerView.Adapter<*>, private val sortType: String, private val order: String) : SortedList.Callback<ListItem>() {

        val executor = Executors.newSingleThreadExecutor()

        override fun compare(item1: ListItem, item2: ListItem): Int {
            val future = executor.submit<Int> {
                var result = when (this.sortType) {
                    "名前" -> item1.name.compareTo(item2.name).toFloat()
                    "改修度" -> (item1.level - item2.level).toFloat()
                    "ID" -> (item1.mstId - item2.mstId).toFloat()
                    "火力" -> item1.houg + item1.hougImproved - item2.houg - item2.hougImproved
                    "雷装" -> item1.raig + item1.raigImproved - item2.raig - item2.raigImproved
                    "爆装" -> item1.baku + item1.bakuImproved - item2.baku - item2.bakuImproved
                    "対空" -> item1.tyku + item1.tykuImproved - item2.tyku - item2.tykuImproved
                    "対潜" -> item1.tais + item1.taisImproved - item2.tais - item2.taisImproved
                    "索敵" -> item1.saku + item1.sakuImproved - item2.saku - item2.sakuImproved
                    "命中" -> item1.houm + item1.houmImproved - item2.houm - item2.houmImproved
                    "回避" -> item1.houk + item1.houkImproved - item2.houk - item2.houkImproved
                    "装甲" -> item1.souk + item1.soukImproved - item2.souk - item2.soukImproved
                    "加重対空" -> item1.adjustedAA + item1.adjustedAAImproved - item2.adjustedAA - item2.adjustedAAImproved
                    "艦隊対空" -> item1.adjustedFleetAA + item1.adjustedFleetAAImproved - item2.adjustedFleetAA - item2.adjustedFleetAAImproved
                    else -> throw IllegalArgumentException("${this.sortType} does not exist")
                }

                when (order) {
                    "降順" -> result *= -1f
                }

                //同じだった場合はマスターID→熟練度の順番でソートする
                if (result == 0f) {
                    if (item1.mstId - item2.mstId == 0) {
                        result = (item2.level - item1.level).toFloat()
                        if (result == 0f) {
                            result = (item2.alv - item1.alv).toFloat()
                        }
                    }
                }

                //resultをintに変換し返す
                return@submit when {
                    result > 0 -> 1
                    result == 0f -> 0
                    result < 0 -> -1
                    else -> throw IllegalArgumentException("result=$result")
                }
            }
            val result = future.get()
            return result
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun areContentsTheSame(oldData: ListItem, newData: ListItem): Boolean {
            return oldData.equipShipSet?.size == newData.equipShipSet?.size
        }

        override fun areItemsTheSame(item1: ListItem, item2: ListItem): Boolean {
            val result = item1.mstId == item2.mstId
                    && item1.level == item2.level
                    && item1.alv == item2.alv
            if (result) {
                item2.count = item1.count++
                val shipId = mountedEquip.get(item2.mySlotItemId, -1)
                if (shipId != -1) {
                    item2.putShipId(shipId)
                    item1.equipShipSet?.forEach { item2.putShipId(it) }
                }
            }
            return result
        }
    }

    class EquipmentViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(listItem: ListItem) {
            rootView.apply {
                equipment.text = listItem.name

                equipIcon.setImageResource(EquipType3.toEquipType3(listItem.slotType3).imageId)

                alv.apply {
                    visibility = if (listItem.alv == 0) View.GONE else View.VISIBLE
                    when (listItem.alv) {
                        0 -> text = ""
                        1 -> {
                            text = "|"
                            setTextColor(Color.rgb(67, 135, 233))
                        }
                        2 -> {
                            text = "||"
                            setTextColor(Color.rgb(67, 135, 233))
                        }
                        3 -> {
                            text = "|||"
                            setTextColor(Color.rgb(67, 135, 233))
                        }
                        4 -> {
                            text = "\\"
                            setTextColor(Color.rgb(243, 213, 26))
                        }
                        5 -> {
                            text = "\\\\"
                            setTextColor(Color.rgb(243, 213, 26))
                        }
                        6 -> {
                            text = "\\\\\\"
                            setTextColor(Color.rgb(243, 213, 26))
                        }
                        7 -> {
                            text = ">>"
                            setTextColor(Color.rgb(243, 213, 26))
                        }
                    }
                }

                improvement.apply {
                    if (listItem.level == 0) {
                        visibility = View.GONE
                    } else {
                        visibility = View.VISIBLE
                        text = "★${listItem.level}"
                    }
                }

                textview_num.apply {
                    text = if (listItem.equipShipSet == null) {
                        "${listItem.count}/${listItem.count}"
                    } else {
                        "${listItem.count - listItem.equipShipSet!!.size}/${listItem.count}"
                    }
                }

                initStatusView(layout_firepower, text_firepower, text_firepower_improve, listItem.houg, listItem.hougImproved)
                initStatusView(layout_torpedo, text_torpedo, text_torpedo_improve, listItem.raig, listItem.raigImproved)
                initStatusView(layout_divebomb, text_divebomb, text_divebomb_improve, listItem.baku, listItem.bakuImproved)
                initStatusView(layout_aa, text_aa, text_aa_improve, listItem.tyku, listItem.tykuImproved)
                initStatusView(layout_asw, text_asw, text_asw_improve, listItem.tais, listItem.taisImproved)
                initStatusView(layout_los, text_los, text_los_improve, listItem.saku, listItem.sakuImproved)
                initStatusView(layout_accuracy, text_accuracy, text_accuracy_improve, listItem.houm, listItem.houmImproved)
                initStatusView(layout_evation, text_evation, text_evation_improve, listItem.houk, listItem.houkImproved)

                layout_range.visibility = View.VISIBLE
                when (listItem.leng) {
                    0 -> layout_range.visibility = View.GONE
                    1 -> text_range.text = "短"
                    2 -> text_range.text = "中"
                    3 -> text_range.text = "長"
                    4 -> text_range.text = "超長"
                }
                text_range_improve.visibility = View.GONE

                initStatusView(layout_armor, text_armor, text_armor_improve, listItem.souk, listItem.soukImproved)
                initStatusView(layout_adjusted_aa, text_adjusted_aa, text_adjusted_aa_improve, listItem.adjustedAA, listItem.adjustedAAImproved)
                initStatusView(layout_adjusted_fleet_aa, text_adjusted_fleet_aa, text_adjusted_fleet_aa_improve, listItem.adjustedFleetAA, listItem.adjustedFleetAAImproved)

                layout_ships.apply {
                    removeAllViews()
                    if (listItem.equipShipSet != null) {
                        visibility = View.VISIBLE

                        for (entry in listItem.equipShipSet!!.entrySet()) {
                            val myShip = realm.where(MyShip::class.java)
                                    .equalTo("id", entry.element)
                                    .findFirst()
                            val equippedShipLayout = View.inflate(context, R.layout.view_equipped_ship, null) as LinearLayout

                            val nameTextView = equippedShipLayout.findViewById(R.id.name) as TextView
                            val mstShip = realm.where(MstShip::class.java)
                                    .equalTo("id", myShip.shipId)
                                    .findFirst()
                            nameTextView.text = mstShip.name

                            val lv = ButterKnife.findById<TextView>(equippedShipLayout, R.id.lv)
                            lv.text = "Lv${myShip.lv}"

                            val shipNum = ButterKnife.findById<TextView>(equippedShipLayout, R.id.ship_num)
                            if (entry.count > 1) {
                                shipNum.text = "×${entry.count}"
                            } else {
                                shipNum.visibility = View.GONE
                            }

                            addView(equippedShipLayout)
                        }
                    } else {
                        visibility = View.GONE
                    }
                }
            }
        }

        private fun initStatusView(layout: View, baseAbilityTextView: TextView, improvementTextView: TextView, baseAbility: Int, improvement: Float) {
            layout.apply {
                if (baseAbility == 0 && improvement == 0f) {
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE

                    baseAbilityTextView.apply {
                        if (baseAbility == 0) {
                            visibility = View.GONE
                        } else {
                            visibility = View.VISIBLE
                            text = if (baseAbility > 0) "+$baseAbility" else baseAbility.toString()
                        }
                    }

                    improvementTextView.apply {
                        if (improvement == 0f) {
                            visibility = View.GONE
                        } else {
                            visibility = View.VISIBLE
                            val roundDown = BigDecimal(improvement.toDouble()).setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros()
                            text = "+$roundDown"
                        }
                    }
                }
            }
        }

        private fun initStatusView(layout: View, baseAbilityTextView: TextView, improvementTextView: TextView, baseAbility: Float, improvement: Float) {
            layout.apply {
                if (baseAbility == 0f && improvement == 0f) {
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE

                    baseAbilityTextView.apply {
                        if (baseAbility == 0f) {
                            visibility = View.GONE
                        } else {
                            visibility = View.VISIBLE
                            val rounded = BigDecimal(baseAbility.toDouble()).setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros()
                            text = if (rounded > BigDecimal.ZERO) "+${rounded.toPlainString()}" else rounded.toPlainString()
                        }
                    }

                    improvementTextView.apply {
                        if (improvement == 0f) {
                            visibility = View.GONE
                        } else {
                            visibility = View.VISIBLE
                            val roundDown = BigDecimal(improvement.toDouble()).setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros()
                            text = "+$roundDown"
                        }
                    }
                }
            }
        }
    }
}