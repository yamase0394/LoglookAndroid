package jp.gr.java_conf.snake0394.loglook_android.storage

import android.content.Context
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.mapPreference
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListRecyclerViewAdapter
import jp.takuji31.koreference.KoreferenceModel
import jp.takuji31.koreference.gson.gsonPreference
import jp.takuji31.koreference.stringPreference

/**
 * [jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListFragment]
 */
class MyShipListFragmentPrefs(context: Context) : KoreferenceModel(context = context, name = "pref_my_ship_list_fragment") {

    var labelFilter by stringPreference("すべて")

    var shipTypeFilter by stringPreference("すべて")

    var sortType by stringPreference("Lv")

    var order by stringPreference("降順")

    var toLabelMap: Map<Int, List<MyShipListRecyclerViewAdapter.Label>> by mapPreference(HashMap<Int, List<MyShipListRecyclerViewAdapter.Label>>())

    var labelList: List<MyShipListRecyclerViewAdapter.Label> by gsonPreference(default =  ArrayList<MyShipListRecyclerViewAdapter.Label>())
}