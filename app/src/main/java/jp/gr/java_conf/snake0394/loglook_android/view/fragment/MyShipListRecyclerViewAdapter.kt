package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.graphics.drawable.GradientDrawable
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.*
import jp.gr.java_conf.snake0394.loglook_android.bean.*
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3
import kotlinx.android.synthetic.main.cardview_ship.view.*
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.util.*

/**
 * Created by snake0394 on 2016/12/08.
 */
class MyShipListRecyclerViewAdapter(private val fragmentManager: FragmentManager, sortType: String, order: String, private val listener: OnRecyclerViewItemClickListener, val toLabelMap: MutableMap<Int, List<Label>>, val labelList: MutableList<Label>) : RecyclerView.Adapter<MyShipListRecyclerViewAdapter.MyShipListRecyclerViewHolder>() {

    private val sortedList: SortedList<MyShipListItem>

    init {
        sortedList = SortedList(MyShipListItem::class.java, SortedListCallcack(this, sortType, order))
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyShipListRecyclerViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.cardview_ship, viewGroup, false)
        return MyShipListRecyclerViewHolder(itemView, fragmentManager, listener)
    }

    override fun onBindViewHolder(sampleViewHolder: MyShipListRecyclerViewHolder, i: Int) {
        val data = sortedList.get(i)
        if (data != null) {
            sampleViewHolder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    fun getItem(position: Int): MyShipListItem {
        return sortedList.get(position)
    }

    fun setItems(newItemList: List<MyShip>) {

        //newItemListにないものを削除
        for (i in sortedList.size() - 1 downTo 0) {
            val myShip = sortedList.get(i).myShip

            if (!newItemList.contains(myShip)) {
                sortedList.removeItemAt(i).myShip.id
            }
        }

        for (newItem in newItemList) {
            if (toLabelMap.containsKey(newItem.id)) {
                sortedList.add(MyShipListItem(newItem, toLabelMap[newItem.id]!!.toMutableList()))
                continue
            }
            sortedList.add(MyShipListItem(newItem))
        }
    }

    fun addLabel(position: Int, label: Label) {
        val myShipListItem = sortedList.get(position)

        if (labelList.contains(label)) {
            labelList[labelList.indexOf(label)] = label

            for ((_, value) in toLabelMap) {
                if (value.contains(label)) {
                    (value as ArrayList)[value.indexOf(label)] = label
                }
            }
            notifyDataSetChanged()
        } else {
            labelList.add(label)
        }

        if (myShipListItem.labelList.contains(label)) {
            myShipListItem.labelList[myShipListItem.labelList.indexOf(label)] = label
        } else {
            myShipListItem.labelList.add(label)
        }

        val newLabelList = ArrayList<Label>()
        for (l in labelList) {
            if (myShipListItem.labelList.contains(l)) {
                newLabelList.add(l)
            }
        }
        myShipListItem.labelList.clear()
        myShipListItem.labelList.addAll(newLabelList)

        toLabelMap.put(myShipListItem.myShip.id, myShipListItem.labelList)

        notifyItemChanged(position)
    }


    fun removeLabel(position: Int, label: Label) {
        val myShipListItem = sortedList.get(position)

        myShipListItem.labelList.remove(label)

        if (myShipListItem.labelList.isEmpty()) {
            toLabelMap.remove(myShipListItem.myShip.id)
        } else {
            toLabelMap.put(myShipListItem.myShip.id, myShipListItem.labelList)
        }

        //削除されたラベルが他のshipIdにも関連付けるられていない場合labelSetから削除する
        val existingLabelSet = HashSet<Label>()
        for (list in toLabelMap.values) {
            existingLabelSet.addAll(list)
        }
        labelList.retainAll(existingLabelSet)

        notifyItemChanged(position)
    }

    val itemList: List<MyShipListItem>
        get() {
            val list = (0..sortedList.size() - 1).map { sortedList.get(it) }
            return list
        }

    class MyShipListItem(val myShip: MyShip) {
        var labelList: MutableList<Label>

        init {
            labelList = mutableListOf()
        }

        constructor(myShip: MyShip, label: MutableList<Label>) : this(myShip) {
            this.labelList = label
        }
    }

    /**
     * Labelの同一性はnameフィールドで判定されます
     */
    class Label internal constructor(var name: String?, var color: Int) {

        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }

            if (o == null || javaClass != o.javaClass) {
                return false
            }

            val label = o as Label?

            return EqualsBuilder().append(name, label!!.name).isEquals
        }

        override fun hashCode(): Int {
            return HashCodeBuilder(17, 37).append(name).toHashCode()
        }
    }

    private class SortedListCallcack internal constructor(private val adapter: RecyclerView.Adapter<*>, private val sortType: String, private val order: String) : SortedList.Callback<MyShipListItem>() {

        override fun compare(item1: MyShipListItem, item2: MyShipListItem): Int {
            val data1 = item1.myShip
            val data2 = item2.myShip
            var result: Int
            when (sortType) {
                "ID" -> result = data1.id - data2.id
                "Lv" -> result = data1.lv - data2.lv
                "cond" -> result = data1.cond - data2.cond
                "砲撃戦火力" -> result = (ShipUtility.getShellingBasicAttackPower(data1) - ShipUtility.getShellingBasicAttackPower(data2)).toInt()
                "雷撃戦火力" -> result = (ShipUtility.getTorpedoSalvoBasicAttackPower(data1) - ShipUtility.getTorpedoSalvoBasicAttackPower(data2)).toInt()
                "夜戦火力" -> result = (ShipUtility.getNightBattleBasicAttackPower(data1) - ShipUtility.getNightBattleBasicAttackPower(data2)).toInt()
                "対潜" -> result = data1.taisen!![0].value - data2.taisen!![0].value
                else -> return 0
            }

            when (order) {
                "降順" -> result *= -1
            }
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

        override fun areContentsTheSame(oldData: MyShipListItem, newData: MyShipListItem): Boolean {
            return oldData.myShip.id == newData.myShip.id
        }

        override fun areItemsTheSame(data1: MyShipListItem, data2: MyShipListItem): Boolean {
            return data1.myShip.id == data2.myShip.id
        }
    }

    class MyShipListRecyclerViewHolder(private val rootView: View, private val fragmentManager: FragmentManager, private val listener: OnRecyclerViewItemClickListener) : RecyclerView.ViewHolder(rootView) {

        private val slotList: MutableList<ImageView>

        init {
            ButterKnife.bind(this, rootView)
            slotList = ArrayList<ImageView>()
            slotList.add(rootView.slot1)
            slotList.add(rootView.slot2)
            slotList.add(rootView.slot3)
            slotList.add(rootView.slot4)

            rootView.cardview.setOnLongClickListener { onLongClick() }
            rootView.nameText.setOnLongClickListener { onLongClick() }
            rootView.equipments.setOnLongClickListener { onLongClick() }
        }

        fun bind(myShipListItem: MyShipListRecyclerViewAdapter.MyShipListItem) {
            val myShip = myShipListItem.myShip
            Realm.getDefaultInstance().use {
                val mstShip = it.where(MstShip::class.java).equalTo("id", myShip.shipId).findFirst()
                rootView.nameText.text = mstShip.name
            }

            var deck: Deck? = null
            //Deckに入っているか
            for (i in 1..DeckManager.INSTANCE.deckNum) {
                val temp = DeckManager.INSTANCE.getDeck(i)
                if (temp.shipId.contains(myShip.id)) {
                    deck = temp
                    break
                }
            }

            if (deck != null) {
                val deckId = deck.id
                rootView.nameText.setOnClickListener {
                    val dialogFragment = ShipParamDialogFragment.newInstance(myShip.id, deckId)
                    dialogFragment.show(fragmentManager, "ShipParamDialog")
                }
            } else {
                rootView.nameText.setOnClickListener {
                    val dialogFragment = ShipParamDialogFragment.newInstance(myShip.id, 0)
                    dialogFragment.show(fragmentManager, "ShipParamDialog")
                }
            }

            rootView.lv.text = "Lv:" + myShip.lv.toString()

            if (deck != null && (deck.mission[0].toInt() == 1 || deck.mission[0].toInt() == 3)) {
                rootView.state.text = "遠征"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.expedition))
            } else if (Escape.INSTANCE.isEscaped(myShip.id)) {
                rootView.state.text = "退避"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.escort))
            } else if (DockTimer.INSTANCE.getShipId(1) == myShip.id || DockTimer.INSTANCE.getShipId(2) == myShip.id || DockTimer.INSTANCE.getShipId(3) == myShip.id || DockTimer.INSTANCE.getShipId(4) == myShip.id) {
                rootView.state.text = "入渠"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.docking))
            } else if (myShip.nowhp <= myShip.maxhp / 4) {
                rootView.state.text = "大破"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.heavy_damage))
            } else if (myShip.nowhp <= myShip.maxhp / 2) {
                rootView.state.text = "中破"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.moderate_damage))
            } else if (myShip.nowhp <= myShip.maxhp * 3 / 4) {
                rootView.state.text = "小破"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.minor_damage))
            } else if (myShip.nowhp < myShip.maxhp) {
                rootView.state.text = "健在"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.good_health))
            } else {
                rootView.state.text = "無傷"
                rootView.state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.undamaged))
            }

            Realm.getDefaultInstance().use {
                for (i in slotList.indices) {
                    val slotImage = slotList[i]
                    if (i > myShip.slotnum - 1) {
                        slotImage.setImageResource(EquipType3.NOT_AVAILABLE.imageId)
                    } else if (myShip.slot[i].value == -1) {
                        slotImage.setImageResource(EquipType3.EMPTY.imageId)
                    } else {
                        val slotItemId = myShip.slot[i].value
                        if (slotItemId > 0) {
                            val mySlotItem = it.where(MySlotItem::class.java).equalTo("id", slotItemId).findFirst()
                            val mstSlotitem = it.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()
                            slotImage.setImageResource(EquipType3.toEquipType3(mstSlotitem.type[3].value).imageId)
                        } else {
                            slotImage.setImageResource(EquipType3.UNKNOWN.imageId)
                        }
                    }
                }

                if (myShip.slotEx > 0) {
                    val mySlotItem = it.where(MySlotItem::class.java).equalTo("id", myShip.slotEx).findFirst()
                    val mstSlotitem = it.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()
                    rootView.imageview_slot_ex.setImageResource(EquipType3.toEquipType3(mstSlotitem.type[3].value).imageId)
                } else {
                    if (myShip.slotEx == 0) {
                        rootView.imageview_slot_ex.setImageResource(EquipType3.NOT_AVAILABLE.imageId)
                    } else if (myShip.slotEx == -1) {
                        rootView.imageview_slot_ex.setImageResource(EquipType3.EMPTY.imageId)
                    }
                }
            }

            rootView.equipments.setOnClickListener {
                val dialogFragment = EquipmentDialogFragment.newInstance(myShip.id)
                dialogFragment.show(fragmentManager, "fragment_dialog")
            }

            rootView.hpBar.max = myShip.maxhp
            rootView.hpBar.progress = myShip.nowhp

            rootView.hp.text = Integer.toString(myShip.nowhp) + "/" + Integer.toString(myShip.maxhp)

            rootView.cond.apply {
                text = Integer.toString(myShip.cond)
                //condの値で色分けする
                if (myShip.cond >= 50) {
                    //緑
                    setTextColor(ContextCompat.getColor(App.getInstance(), R.color.high_morale))
                } else if (myShip.cond >= 40) {
                    //グレー
                    setTextColor(ContextCompat.getColor(App.getInstance(), R.color.normal_cond))
                } else if (myShip.cond >= 30) {
                    //黄色
                    setTextColor(ContextCompat.getColor(App.getInstance(), R.color.slightly_fatigued))
                } else if (myShip.cond >= 20) {
                    //オレンジ
                    setTextColor(ContextCompat.getColor(App.getInstance(), R.color.moderately_fatigued))
                } else {
                    setTextColor(ContextCompat.getColor(App.getInstance(), R.color.seriously_fatigued))
                }
            }

            rootView.text__shelling_basic_attack_power.text = ShipUtility.getShellingBasicAttackPower(myShip).toInt().toString()

            rootView.text_torpedo_basic_attack_power.text = ShipUtility.getTorpedoSalvoBasicAttackPower(myShip).toInt().toString()

            rootView.text_night_battle_basic_attack_power.text = ShipUtility.getNightBattleBasicAttackPower(myShip).toInt().toString()

            rootView.text_ship_asw.text = myShip.taisen[0].value.toString()


            rootView.layout_label.removeAllViews()
            for (label in myShipListItem.labelList) {
                val labelView = View.inflate(rootView.layout_label.context, R.layout.view_my_ship_list_label, null) as LinearLayout
                val labelTextView = labelView.findViewById(R.id.text_label_name) as TextView
                labelTextView.text = label.name
                val bgShape = labelTextView.background as GradientDrawable
                bgShape.setColor(label.color)
                rootView.layout_label.addView(labelView)
            }
        }

        fun onLongClick(): Boolean {
            listener.onRecyclerViewItemClicked(adapterPosition)
            return false
        }
    }

    interface OnRecyclerViewItemClickListener {
        fun onRecyclerViewItemClicked(position: Int)
    }
}