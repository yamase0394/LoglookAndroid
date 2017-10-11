package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import jp.gr.java_conf.snake0394.loglook_android.R
import kotlinx.android.synthetic.main.fragment_battle_statistics.*
import kotlinx.android.synthetic.main.layout_sea_area_list.view.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class BattleStatisticsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_battle_statistics, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdcardPath = File(Environment.getExternalStorageDirectory().path + "/泥提督支援アプリ/")
        val filePath = sdcardPath.toString() + File.separator + "海戦・ドロップ報告書.csv"
        val dataList = arrayListOf<ListItem>()
        val battleLogList = arrayListOf<BattleLog>()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        BufferedReader(InputStreamReader(FileInputStream(filePath), "SJIS")).use {
            var line = it.readLine()
            try {
                sdf.parse(line.split(",")[0])
            } catch (e: Exception) {
                line = it.readLine()
            }

            val battleLogMap = hashMapOf<String, ListItem>()
            while (line != null) {
                val splitted = line.split(",")
                val battleLog = BattleLog(sdf.parse(splitted[0]), splitted[1], splitted[2].toInt(), splitted[3], splitted[4], splitted[5], splitted[6], splitted[7], splitted[8], splitted[9], splitted[10], splitted[11], splitted[12], splitted[13], null, null, null, null)
                if (battleLogMap.contains(battleLog.seaArea)) {
                    val listItem = battleLogMap[battleLog.seaArea] ?: throw NullPointerException("battleLogMap not contains ${battleLog.seaArea}")
                    when (battleLog.boss) {
                        "出撃" -> listItem.sortieCount++
                        "ボス" -> listItem.bossCount++
                        "出撃&ボス" -> {
                            listItem.sortieCount++
                            listItem.bossCount++
                        }
                    }
                } else {
                    val listItem = ListItem(battleLog.seaArea, 0, 0)
                    when (battleLog.boss) {
                        "出撃" -> listItem.sortieCount++
                        "ボス" -> listItem.bossCount++
                        "出撃&ボス" -> {
                            listItem.sortieCount++
                            listItem.bossCount++
                        }
                    }
                    battleLogMap.put(battleLog.seaArea, listItem)
                    dataList.add(listItem)
                }
                line = it.readLine()
            }
        }

        val adapter = ListViewAdapter(context)
        adapter.setDataList(dataList)
        listview_sea_area.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(): BattleStatisticsFragment = BattleStatisticsFragment()
    }

    class ListViewAdapter(val context: Context) : BaseAdapter() {
        private lateinit var battleLogList: ArrayList<ListItem>

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = View.inflate(context, R.layout.layout_sea_area_list, null)
            }
            view!!.apply {
                val listItem = battleLogList[position]
                textview_sea_area_name.text = listItem.seaAreaName
                text_sortie_count.text = listItem.sortieCount.toString()
                textview_boss_count.text = listItem.bossCount.toString()
            }
                return view
        }

        override fun getItem(position: Int): ListItem {
            return battleLogList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return battleLogList.size
        }

        fun setDataList(dataList: ArrayList<ListItem>) {
            battleLogList = dataList
        }
    }

    data class BattleLog(
            val date: Date,
            val seaArea: String,
            val mass: Int,
            val boss: String,
            val winRank: String,
            val tactic: String,
            val formation: String,
            val enemyFormation: String,
            val airState: String,
            val touchPlane: String,
            val enemyTouchPlane: String,
            val enemyFleetName: String,
            val dropShipType: String,
            val dropShipName: String,
            val nameList: ArrayList<String>?,
            val enemyNameList: ArrayList<String>?,
            val nameCombinedList: ArrayList<String>?,
            val enemyNameCombinedList: ArrayList<String>?
    )

    class ListItem(
            val seaAreaName: String,
            var sortieCount: Int,
            var bossCount: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ListItem

            if (seaAreaName != other.seaAreaName) return false

            return true
        }

        override fun hashCode(): Int {
            return seaAreaName.hashCode()
        }
    }
}