package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import butterknife.ButterKnife
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.ShipType
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.storage.MyShipListFragmentPrefs
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListRecyclerViewAdapter.Label
import kotlinx.android.synthetic.main.fragment_my_ship_list.*
import java.util.*

/**
 * 艦娘一覧画面
 */
class MyShipListFragment : Fragment(), MyShipListRecyclerViewAdapter.OnRecyclerViewItemClickListener, MyShipListAddLabelDialog.OnFinishedListener {

    private lateinit var searchView: SearchView
    private var dataList: MutableList<MyShip>? = null
    private val prefs by lazy { MyShipListFragmentPrefs(context) }
    private val realm by lazy { Realm.getDefaultInstance() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_my_ship_list, container, false)

        //ツールバーに検索ボックスを追加する
        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        val toolbar = ButterKnife.findById<Toolbar>(activity, R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_my_ship_list_fragment)
        this.searchView = toolbar.menu.findItem(R.id.menu_search).actionView as SearchView
        this.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchDataList(newText)
                recycler_view.scrollToPosition(0)
                return true
            }
        })
        val searchPlate = ButterKnife.findById<EditText>(this.searchView, R.id.search_src_text)
        searchPlate.setOnEditorActionListener { _, _, _ ->
            searchView.clearFocus()
            false
        }
        searchPlate.imeOptions = EditorInfo.IME_FLAG_NO_FULLSCREEN or EditorInfo.IME_ACTION_NONE

        /*
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_call) {
                    android.support.v4.app.DialogFragment dialogFragment = MyShipListSortDialogFragment.newInstance();
                    dialogFragment.setTargetFragment(MyShipListFragment.this, 0);
                    dialogFragment.show(getFragmentManager(), "doalog");
                    return true;
                }
                return false;
            }
        });
        */

        var spinner = rootView.findViewById(R.id.sortSpinner) as Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val type = parent as Spinner
                // 初回起動時の動作
                if (type.isFocusable == false) {
                    type.isFocusable = true
                    return
                }

                prefs.sortType = type.selectedItem as String

                val adapter = MyShipListRecyclerViewAdapter(fragmentManager, prefs.sortType, prefs.order, this@MyShipListFragment, prefs.toLabelMap.toMutableMap(), prefs.labelList.toMutableList())
                recycler_view.swapAdapter(adapter, false)
                initDataList()
                recycler_view.scrollToPosition(0)

                searchView.clearFocus()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
        arrayAdapter.addAll(*SORT_TYPE_LIST.toTypedArray() as Array<String>)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.isFocusable = false
        spinner.setSelection(SORT_TYPE_LIST.indexOf(prefs.sortType))

        val orderButton = rootView.findViewById(R.id.button_order) as Button
        orderButton.setOnClickListener { v ->
            val orderButton = v as Button
            when (orderButton.text.toString()) {
                "降順" -> orderButton.text = "昇順"
                "昇順" -> orderButton.text = "降順"
            }

            prefs.order = orderButton.text.toString()

            val adapter = MyShipListRecyclerViewAdapter(fragmentManager, prefs.sortType, prefs.order, this@MyShipListFragment, prefs.toLabelMap.toMutableMap(), prefs.labelList.toMutableList())
            recycler_view.swapAdapter(adapter, false)
            initDataList()
            recycler_view.scrollToPosition(0)

            searchView.clearFocus()
        }
        orderButton.text = prefs.order

        spinner = rootView.findViewById(R.id.shipTypeFilterSpinner) as Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val shipTypeFilterSpinner = parent as Spinner
                if (shipTypeFilterSpinner.isFocusable == false) {
                    shipTypeFilterSpinner.isFocusable = true
                    return
                }

                prefs.shipTypeFilter = shipTypeFilterSpinner.selectedItem as String

                filterDataList()
                recycler_view.scrollToPosition(0)

                searchView.clearFocus()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
        arrayAdapter.add("すべて")
        ShipType.values().forEach { arrayAdapter.add(it.toString()) }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.isFocusable = false
        if (dataList == null) {
            try {
                val displayShipType = ShipType.valueOf(prefs.shipTypeFilter)
                //"すべて"の分ずらす
                spinner.setSelection(displayShipType.ordinal + 1)
            } catch (e: IllegalArgumentException) {
                spinner.setSelection(0)
            }
        }

        return rootView
    }

    override fun onViewCreated(rootView: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        if (recycler_view.adapter == null) {
            val adapter = MyShipListRecyclerViewAdapter(fragmentManager, prefs.sortType, prefs.order, this, prefs.toLabelMap.toMutableMap(), prefs.labelList.toMutableList())
            recycler_view.adapter = adapter
        }

        spinner_filter_label.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (!spinner_filter_label.isFocusable) {
                    spinner_filter_label.isFocusable = true
                    return
                }

                prefs.labelFilter = spinner_filter_label.selectedItem as String

                filterDataList()
                recycler_view.scrollToPosition(0)

                searchView.clearFocus()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        this.initLabelFilterSpinnerAdapter()

        spinner_filter_label.setOnTouchListener(this::clearSearchViewFocus)
        recycler_view.setOnTouchListener(this::clearSearchViewFocus)
        sortAppBar.setOnTouchListener(this::clearSearchViewFocus)
        layout_base.setOnTouchListener(this::clearSearchViewFocus)
        shipTypeFilterSpinner.setOnTouchListener(this::clearSearchViewFocus)
        sortSpinner.setOnTouchListener(this::clearSearchViewFocus)

        filterDataList()
        recycler_view.scrollToPosition(0)
    }

    private fun clearSearchViewFocus(view: View?, motionEvent: MotionEvent): Boolean {
        this.searchView.clearFocus()
        return false
    }

    override fun onPause() {
        super.onPause()

        val recyclerViewAdapter = recycler_view.adapter as MyShipListRecyclerViewAdapter
        prefs.toLabelMap = recyclerViewAdapter.toLabelMap
        prefs.labelList = recyclerViewAdapter.labelList
    }

    private fun initDataList() {
        if (searchView.query.isNotEmpty()) {
            searchDataList(searchView.query.toString())
            return
        }

        (recycler_view.adapter as MyShipListRecyclerViewAdapter).setItems(dataList!!)
    }

    private fun searchDataList(searchWord: String) {
        val lowerCaseSearchWord = searchWord.toLowerCase()

        val filteredList = ArrayList<MyShip>()
        for (myShip in dataList!!) {
            val mstShip = realm.where(MstShip::class.java).equalTo("id", myShip.shipId).findFirst()
            if (mstShip.name.toLowerCase().contains(lowerCaseSearchWord)) {
                filteredList.add(myShip)
                continue
            }

            if (mstShip.yomi.toLowerCase().contains(lowerCaseSearchWord)) {
                filteredList.add(myShip)
            }
        }

        (recycler_view.adapter as MyShipListRecyclerViewAdapter).setItems(filteredList)
    }

    private fun filterDataList() {
        if (prefs.shipTypeFilter == "すべて") {
            dataList = ArrayList(realm.where(MyShip::class.java).findAll())
        } else {
            dataList = arrayListOf()
            val shipType = ShipType.valueOf(prefs.shipTypeFilter)
            for (myShip in realm.where(MyShip::class.java).findAll()) {
                val mstShip = realm.where(MstShip::class.java).equalTo("id", myShip.shipId).findFirst()
                if (shipType == ShipType.toShipType(mstShip.stype)) {
                    dataList!!.add(myShip)
                }
            }
        }

        initDataList()

        if ((recycler_view.adapter as MyShipListRecyclerViewAdapter).labelList.contains(Label(prefs.labelFilter, 0))) {
            dataList = ArrayList<MyShip>()
            (recycler_view.adapter as MyShipListRecyclerViewAdapter).itemList
                    .filter { it.labelList.contains(Label(prefs.labelFilter, 0)) }
                    .forEach { dataList!!.add(it.myShip) }
        } else {
            return
        }

        initDataList()
    }

    override fun onDestroyView() {
        //検索ボックスを削除
        val toolbar = ButterKnife.findById<Toolbar>(activity, R.id.toolbar)
        toolbar.menu.removeGroup(R.id.search_group)
        realm.close()

        super.onDestroyView()
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        if (searchView.hasFocus()) {
            searchView.clearFocus()
            return
        }

        val builder = AlertDialog.Builder(context)
        val items: Array<String>
        val myShipListItem = (recycler_view.adapter as MyShipListRecyclerViewAdapter).getItem(position)
        if (myShipListItem.labelList.isNotEmpty()) {
            items = arrayOf("ラベルを追加", "ラベルを削除")
        } else {
            items = arrayOf("ラベルを追加")
        }
        builder.setItems(items) { dialog, which ->
            when (which) {
                0 -> {
                    val adapter = recycler_view.adapter as MyShipListRecyclerViewAdapter
                    val dialogFragment = MyShipListAddLabelDialog.newInstance(position, adapter.labelList)
                    dialogFragment.setTargetFragment(this@MyShipListFragment, 0)
                    dialogFragment.show(fragmentManager, "addLabelDialog")
                }
                1 -> {
                    val builder = AlertDialog.Builder(context)
                    val items = arrayOfNulls<String>(myShipListItem.labelList.size)
                    for (i in 0..myShipListItem.labelList.size - 1) {
                        items[i] = myShipListItem.labelList[i].name
                    }
                    builder.setTitle("ラベルを削除")
                            .setItems(items) { _, which ->
                                val adapter = recycler_view.adapter as MyShipListRecyclerViewAdapter
                                adapter.removeLabel(position, myShipListItem.labelList[which])
                                initLabelFilterSpinnerAdapter()
                                filterDataList()
                            }
                            .setNegativeButton("キャンセル", null)
                    builder.show()
                }
            }
        }
        builder.show()
    }

    private fun initLabelFilterSpinnerAdapter() {
        val recyclerViewAdapter = recycler_view.adapter as MyShipListRecyclerViewAdapter

        val arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
        arrayAdapter.add("すべて")
        val labelList = recyclerViewAdapter.labelList
        labelList.forEach { arrayAdapter.add(it.name) }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_filter_label.adapter = arrayAdapter
        spinner_filter_label.isFocusable = false

        val comparisionLabel = Label(prefs.labelFilter, 0)
        if (labelList.contains(comparisionLabel)) {
            spinner_filter_label.setSelection(labelList.indexOf(comparisionLabel) + 1)
        } else {
            spinner_filter_label.setSelection(0)
        }
    }

    override fun onAddLabelFinished(isCanceled: Boolean, position: Int, addedLabel: Label?) {
        if (isCanceled) {
            return
        }

        val recyclerViewAdapter = recycler_view.adapter as MyShipListRecyclerViewAdapter
        recyclerViewAdapter.addLabel(position, addedLabel!!)

        this.initLabelFilterSpinnerAdapter()

        filterDataList()
    }

    companion object {

        private val SORT_TYPE_LIST = Arrays.asList("ID", "Lv", "cond", "砲撃戦火力", "雷撃戦火力", "夜戦火力", "対潜")

        fun newInstance(): MyShipListFragment {
            val fragment = MyShipListFragment()
            return fragment
        }
    }
}
