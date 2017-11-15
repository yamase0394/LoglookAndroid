package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.EquipType2
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem
import jp.gr.java_conf.snake0394.loglook_android.storage.EquipmentFragmentPrefs
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3
import kotlinx.android.synthetic.main.fragment_equipment.*
import java.util.*

class EquipmentFragment : Fragment(), EquipmentDrawerRecyclerAdapter.OnItemClickListener {

    private val equipTypeFilterList = Arrays.asList("全装備", "小口径主砲", "中口径主砲", "大口径主砲", "副砲", "魚雷", "艦戦/夜戦", "艦爆/艦攻/噴式/夜攻", "艦偵", "水上機/飛行艇", "電探", "機銃/高射装置", "高角砲", "対潜", "陸上機", "その他")

    private val prefs by lazy { EquipmentFragmentPrefs(context) }
    private val realm by lazy { Realm.getDefaultInstance() }
    private val equipmentAdapter by lazy { EquipmentAdapter() }

    private var dataList = arrayListOf<MySlotItem>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_equipment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview_right_drawer.layoutManager = LinearLayoutManager(context)
        val drawerRecyclerAdapter = EquipmentDrawerRecyclerAdapter(this)
        drawerRecyclerAdapter.setItems(equipTypeFilterList)
        recyclerview_right_drawer.adapter = drawerRecyclerAdapter
        recyclerview_right_drawer.addItemDecoration(EquipmentDrawerRecyclerAdapter.MyItemDecoration(context))

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = equipmentAdapter

        equipTypeFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 初回起動時の動作
                if (!equipTypeFilterSpinner.isFocusable) {
                    equipTypeFilterSpinner.isFocusable = true
                    return
                }
                /*
                EquipmentAdapter adapter = new EquipmentAdapter(prefs.getSortType(), prefs.getOrder());
                recyclerView.swapAdapter(adapter, false);
                */
                filterDataList()
                setDataList()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        var adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, equipTypeFilterList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        equipTypeFilterSpinner.adapter = adapter
        equipTypeFilterSpinner.isFocusable = false
        equipTypeFilterSpinner.setSelection(0)

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 初回起動時の動作
                if (!sortSpinner.isFocusable) {
                    sortSpinner.isFocusable = true
                    return
                }
                prefs.sortType = sortSpinner.selectedItem as String
                /*
                EquipmentAdapter recyclerAdapter = new EquipmentAdapter(prefs.getSortType(), prefs.getOrder());
                recyclerView.swapAdapter(recyclerAdapter, false);
                setDataList();
                */
                equipmentAdapter.sortData(prefs.sortType, prefs.order)
                recycler_view.scrollToPosition(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item)
        adapter.addAll("名前", "改修度", "ID", "火力", "雷装", "爆装", "対空", "対潜", "索敵", "命中", "回避", "装甲", "加重対空", "艦隊対空")
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = adapter
        sortSpinner.isFocusable = false
        sortSpinner.setSelection(adapter.getPosition(prefs.sortType))

        button_order.setOnClickListener {
            val buttonText = button_order.text.toString()
            when (buttonText) {
                "降順" -> prefs.order = "昇順"
                "昇順" -> prefs.order = "降順"
            }
            button_order.text = prefs.order

            /*
                EquipmentAdapter recyclerAdapter = new EquipmentAdapter(prefs.getSortType(), prefs.getOrder());
                recyclerView.swapAdapter(recyclerAdapter, false);
                setDataList();
                */
            equipmentAdapter.sortData(prefs.sortType, prefs.order)
            recycler_view.scrollToPosition(0)
        }
        button_order.text = prefs.order

        filterDataList()
        setDataList()
    }

    /**
     * [.dataList]を[.equipTypeFilterSpinner]の選択項目でフィルターします
     */
    private fun filterDataList() {
        val newDataList = ArrayList<MySlotItem>()
        val showEquipType = equipTypeFilterSpinner.selectedItem.toString()

        for (mySlotItem in realm.where(MySlotItem::class.java).findAll()) {
            val mstSlotitem = realm.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()

            val type3 = EquipType3.toEquipType3(mstSlotitem.type[3].value)
            if (type3.toString() == showEquipType) {
                newDataList.add(mySlotItem)
                continue
            }

            when (showEquipType) {
                "小口径主砲" ->
                    if (type3 == EquipType3.高角砲 && EquipType2.toEquipType2(mstSlotitem.type[2].value) == EquipType2.小口径主砲) {
                        newDataList.add(mySlotItem)
                    }
                "副砲" ->
                    if (type3 == EquipType3.高角砲 && EquipType2.toEquipType2(mstSlotitem.type[2].value) == EquipType2.副砲) {
                        newDataList.add(mySlotItem)
                    }
                "全装備" -> newDataList.add(mySlotItem)
                "艦戦/夜戦" ->
                    when (type3) {
                        EquipType3.艦上戦闘機,
                        EquipType3.夜間戦闘機-> newDataList.add(mySlotItem)
                    }
                "艦爆/艦攻/噴式/夜攻" ->
                    when (type3) {
                        EquipType3.艦上爆撃機,
                        EquipType3.艦上攻撃機,
                        EquipType3.噴式戦闘爆撃機_噴式景雲改,
                        EquipType3.噴式戦闘爆撃機_橘花改,
                        EquipType3.夜間攻撃機-> newDataList.add(mySlotItem)
                    }
                "艦偵" ->
                    when (type3) {
                        EquipType3.艦上偵察機 -> newDataList.add(mySlotItem)
                    }
                "水上機/飛行艇" ->
                    when (type3) {
                        EquipType3.水上機, EquipType3.大型飛行艇, EquipType3.水上戦闘機 -> newDataList.add(mySlotItem)
                    }
                "機銃/高射装置" ->
                    when (type3) {
                        EquipType3.対空機銃, EquipType3.高射装置 -> newDataList.add(mySlotItem)
                    }
                "対潜" ->
                    when (type3) {
                        EquipType3.ソナー, EquipType3.爆雷, EquipType3.対潜哨戒機, EquipType3.オートジャイロ -> newDataList.add(mySlotItem)
                    }
                "陸上機" ->
                    when (type3) {
                        EquipType3.局地戦闘機, EquipType3.陸上攻撃機, EquipType3.陸軍戦闘機 -> newDataList.add(mySlotItem)
                    }
                "その他" ->
                    when (type3) {
                        EquipType3.特型内火艇,
                        EquipType3.補給物資,
                        EquipType3.戦闘糧食,
                        EquipType3.水上艦要員,
                        EquipType3.対地装備,
                        EquipType3.航空要員,
                        EquipType3.司令部施設,
                        EquipType3.照明弾,
                        EquipType3.艦艇修理施設,
                        EquipType3.簡易輸送部材,
                        EquipType3.探照灯,
                        EquipType3.追加装甲,
                        EquipType3.上陸用舟艇,
                        EquipType3.機関部強化,
                        EquipType3.応急修理要員,
                        EquipType3.対艦強化弾,
                        EquipType3.対空強化弾,
                        EquipType3.輸送機材,
                        EquipType3.潜水艦装備,
                        EquipType3.UNKNOWN -> newDataList.add(mySlotItem)
                    }
            }
        }

        (recyclerview_right_drawer.adapter as EquipmentDrawerRecyclerAdapter).setHighlight(equipTypeFilterList.indexOf(showEquipType))

        dataList = newDataList
    }

    /**
     * [.recyclerView]のアダプタに[.dataList]を渡します
     */
    private fun setDataList() {
        equipmentAdapter.setItems(dataList, prefs.sortType, prefs.order)
        recycler_view.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        this.realm.close()
    }

    override fun onItemClick(itemName: String) {
        equipTypeFilterSpinner.setSelection(equipTypeFilterList.indexOf(itemName))
        layout_drawer.closeDrawers()
    }

    companion object {
        fun newInstance(): EquipmentFragment = EquipmentFragment()
    }
}
