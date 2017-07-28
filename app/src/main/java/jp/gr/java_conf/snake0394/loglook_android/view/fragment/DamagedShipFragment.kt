package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.storage.DamagedShipFragmentPrefs
import kotlinx.android.synthetic.main.fragment_damaged_ship.*

class DamagedShipFragment : Fragment() {

    private val prefs by lazy { DamagedShipFragmentPrefs(context) }
    private val realm by lazy { Realm.getDefaultInstance() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_damaged_ship, container, false)

        val spinner = rootView.findViewById(R.id.sortSpinner) as Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val type = parent as Spinner
                // 初回起動時の動作
                if (type.isFocusable == false) {
                    type.isFocusable = true
                    return
                }
                prefs.sortType = type.selectedItem as String
                val adapter = DamagedShipAdapter(fragmentManager, prefs.sortType, prefs.order)
                recycler_view.swapAdapter(adapter, false)
                initDataList()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
        adapter.add("修復時間")
        adapter.add("損傷度")
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.isFocusable = false
        spinner.setSelection(adapter.getPosition(prefs.sortType))


        val orderButton = rootView.findViewById(R.id.button_order) as Button
        orderButton.setOnClickListener { v ->
            val orderButton = v as Button
            val buttonText = orderButton.text.toString()
            when (buttonText) {
                "降順" -> prefs.order = "昇順"
                "昇順" -> prefs.order = "降順"
            }
            orderButton.text = prefs.order
            val adapter = DamagedShipAdapter(fragmentManager, prefs.sortType, prefs.order)
            recycler_view.swapAdapter(adapter, false)
            initDataList()
        }
        orderButton.text = this.prefs.order

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        if (recycler_view.adapter == null) {
            val adapter = DamagedShipAdapter(fragmentManager, prefs.sortType, prefs.order)
            recycler_view.adapter = adapter
            initDataList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        realm.close()
    }

    private fun initDataList() {
        (recycler_view.adapter as DamagedShipAdapter).clearData()
        val initialDataList = realm.where(MyShip::class.java).findAll()
                .filter { it.ndockTime > 0 }
        (recycler_view.adapter as DamagedShipAdapter).addDataOf(initialDataList)
        recycler_view.scrollToPosition(0)
    }

    companion object {

        fun newInstance(): DamagedShipFragment {
            return DamagedShipFragment()
        }
    }
}
