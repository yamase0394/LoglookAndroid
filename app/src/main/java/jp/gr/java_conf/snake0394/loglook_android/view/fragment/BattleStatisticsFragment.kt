package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import kotlinx.android.synthetic.main.fragment_battle_statistics.*
import kotlinx.android.synthetic.main.layout_sea_area_list.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.apache.commons.io.input.ReversedLinesFileReader
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class BattleStatisticsFragment : Fragment(),
        AdapterView.OnItemClickListener,
        BattleStatisticsFilterDialog.OnFilterChangedListener {

    companion object {
        private val TAG = "BattleStatisticsFragment"

        @JvmStatic
        fun newInstance(): BattleStatisticsFragment = BattleStatisticsFragment()
    }

    private val listAdapter by lazy { ListViewAdapter(context) }
    private var allLogList: ArrayList<BattleLog>? = null
    private var presentDateFilter = BattleStatisticsFilterDialog.DATE_FILTER_THIS_MONTH
    private var fromDate: Date? = null
    private var toDate: Date? = null

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listAdapter.getItem(position)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        toolbar.inflateMenu(R.menu.menu_battle_statistics_fragment)
        toolbar.menu.findItem(R.id.menu_filter).setOnMenuItemClickListener {
            val dialogFragment = BattleStatisticsFilterDialog.newInstance(presentDateFilter, fromDate, toDate)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(fragmentManager, "filtering dialog")
            return@setOnMenuItemClickListener false
        }

        return inflater!!.inflate(R.layout.fragment_battle_statistics, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch(CommonPool) {
            val channel = Channel<BattleLog>(Channel.UNLIMITED)
            launch(CommonPool) {
                val start = System.currentTimeMillis()

                val sdcardPath = File(Environment.getExternalStorageDirectory().path + "/泥提督支援アプリ/")
                val filePath = sdcardPath.toString() + File.separator + "海戦・ドロップ報告書.csv"
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                var tempAllLogList = arrayListOf<BattleLog>()
                ReversedLinesFileReader(File(filePath), 8096*2, "SJIS").use {
                    var line = it.readLine()
                    var invalidCount = 0
                    loop@ while (line != null) {
                        val split = line.split(",")
                        try {
                            val battleLog = when (split.size) {
                                38, 62 -> BattleLog(sdf.parse(split[0]), split[1], split[2].toInt(), split[3], split[4], split[5], split[6], split[7], split[8], split[9], split[10], split[11], split[12], split[13], null, arrayListOf(split[26], split[28], split[30], split[32], split[34], split[36]).filter { it.isNotEmpty() }.toMutableList(), null, null)
                            //35は出撃がないのがまじってる
                            //35 -> BattleLog(sdf.parse(split[0]), split[1], split[2].toInt(), split[3], split[4], split[5], split[6], split[7], null, null, null, split[8], split[9], split[10], null, arrayListOf(split[23], split[24], split[25], split[26], split[27], split[28]).filter { it.isNotEmpty() }.toMutableList(), null, null)
                            //一番うしろにカンマがついててsplit.size=36のもある
                                else -> {
                                    Logger.d(TAG, "size = ${split.size}")
                                    Logger.d(TAG, "invalid record = $line")
                                    if (invalidCount++ > 5) {
                                        break@loop
                                    }
                                    line = it.readLine()
                                    continue@loop
                                }
                            }
                            channel.send(battleLog)
                            tempAllLogList.add(battleLog)
                        } catch (e: ParseException) {
                            //ヘッダ?
                        }

                        line = it.readLine()
                    }

                    channel.close()
                }
                allLogList = tempAllLogList

                val end = System.currentTimeMillis()
                Logger.d(TAG, "read csv:${end - start}ms")
            }

            val dataList = arrayListOf<ListItem>()
            launch(CommonPool) {
                val start = System.currentTimeMillis()

                val battleLogMap = hashMapOf<String, ListItem>()
                val thisMonthFirst = org.apache.commons.lang3.time.DateUtils.truncate(Date(), Calendar.MONTH)
                for (battleLog in channel) {
                    if (battleLog.date.before(thisMonthFirst)) {
                        break
                    }

                    val mapNumber = getMapNumber(battleLog)
                    val listItem = battleLogMap[battleLog.seaAreaName + mapNumber] ?: ListItem(battleLog.seaAreaName, mapNumber)

                    if (battleLog.boss.startsWith("出撃")) listItem.sortieCount++

                    if (battleLog.boss.endsWith("ボス")) {
                        listItem.bossCount++
                        listItem.bossWinRankMap[battleLog.winRank] = listItem.bossWinRankMap[battleLog.winRank]!! + 1
                    }

                    listItem.battleLogList.add(battleLog)

                    if (!battleLogMap.contains(battleLog.seaAreaName + listItem.mapNumber)) {
                        battleLogMap.put(listItem.seaAreaName + listItem.mapNumber, listItem)
                        dataList.add(listItem)
                    }
                }

                for (listItem in dataList) {
                    listItem.arrivalRate = "${((listItem.bossCount.toDouble() / listItem.sortieCount) * 100).toInt()}%"
                    val winCount = listItem.bossWinRankMap.run { get("S")!! + get("A")!! + get("B")!! }
                    val total = listItem.bossWinRankMap.values.sum()
                    listItem.bossWinRate = "${((winCount.toFloat() / total) * 100).toInt()}%"
                }

                dataList.sortWith(ListItemComparator())

                val end = System.currentTimeMillis()
                Logger.d(TAG, "init listItems:${end - start}ms")
            }.join()

            launch(UI) {
                listAdapter.setDataList(dataList)
                listview_sea_area.adapter = listAdapter

                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        //追加したメニューアイテムを消去
        val toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        toolbar.menu.removeItem(R.id.menu_filter)

        super.onDestroyView()
    }

    override fun onFilterChanged(dateFrom: Date, dateTo: Date, dateFilter: String) {
        presentDateFilter = dateFilter
        if (dateFilter == "期間を指定") {
            this.toDate = dateTo
            this.fromDate = dateFrom
        }

        launch(UI) {
            progressBar.visibility = View.VISIBLE

            launch(CommonPool) {
                //allLogListの初期化を待つ
                while (allLogList == null) {
                    delay(1000)
                    Logger.d(javaClass.simpleName, "waiting for complete allLogList initialized")
                }
            }.join()

            val dataList = arrayListOf<ListItem>()
            val battleLogMap = hashMapOf<String, ListItem>()
            val channel = Channel<BattleLog>(Channel.UNLIMITED)

            launch(CommonPool) {
                allLogList!!.forEach { battleLog ->
                    if (battleLog.date.before(dateFrom) || battleLog.date.after(dateTo)) {
                        return@forEach
                    } else {
                        channel.send(battleLog)
                    }
                }
                channel.close()
            }

            launch(CommonPool) {
                for (battleLog in channel) {
                    val mapNumber = getMapNumber(battleLog)
                    val listItem = battleLogMap[battleLog.seaAreaName + mapNumber] ?: ListItem(battleLog.seaAreaName, mapNumber)

                    if (battleLog.boss.startsWith("出撃")) listItem.sortieCount++

                    if (battleLog.boss.endsWith("ボス")) {
                        listItem.bossCount++
                        listItem.bossWinRankMap[battleLog.winRank] = listItem.bossWinRankMap[battleLog.winRank]!! + 1
                    }

                    if (!battleLogMap.contains(battleLog.seaAreaName + listItem.mapNumber)) {
                        battleLogMap.put(listItem.seaAreaName + listItem.mapNumber, listItem)
                        dataList.add(listItem)
                    }
                }
            }.join()

            for (listItem in dataList) {
                listItem.arrivalRate = "${((listItem.bossCount.toDouble() / listItem.sortieCount) * 100).toInt()}%"
                val winCount = listItem.bossWinRankMap.run { get("S")!! + get("A")!! + get("B")!! }
                val total = listItem.bossWinRankMap.values.sum()
                listItem.bossWinRate = "${((winCount.toFloat() / total) * 100).toInt()}%"
            }

            dataList.sortWith(ListItemComparator())

            listAdapter.setDataList(dataList)
            progressBar.visibility = View.GONE
        }
    }

    class ListViewAdapter(val context: Context) : BaseAdapter() {
        private lateinit var battleLogList: ArrayList<ListItem>

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = View.inflate(context, R.layout.layout_sea_area_list, null)
            }
            view!!.apply {
                layout_root.setBackgroundColor(
                        if (position % 2 == 0) {
                            ContextCompat.getColor(context, R.color.list_even_background)
                        } else {
                            Color.WHITE
                        }
                )

                val listItem = battleLogList[position]
                textview_sea_area_name.text = listItem.seaAreaName
                text_map_number.text = listItem.mapNumber
                text_sortie_count.text = listItem.sortieCount.toString()
                textview_boss_count.text = listItem.bossCount.toString()
                text_s_count.text = listItem.bossWinRankMap["S"].toString()
                text_a_count.text = listItem.bossWinRankMap["A"].toString()
                text_b_count.text = listItem.bossWinRankMap["B"].toString()
                text_c_count.text = listItem.bossWinRankMap["C"].toString()
                text_d_count.text = listItem.bossWinRankMap["D"].toString()
                text_e_count.text = listItem.bossWinRankMap["E"].toString()
                textview_arrival_rate.text = listItem.arrivalRate
                text_boss_win_rate.text = listItem.bossWinRate
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
            notifyDataSetChanged()
        }
    }

    data class BattleLog(
            val date: Date,
            val seaAreaName: String,
            val mass: Int,
            val boss: String,
            val winRank: String,
            val tactic: String,
            val formation: String,
            val enemyFormation: String,
            val airState: String?,
            val touchPlane: String?,
            val enemyTouchPlane: String?,
            val enemyFleetName: String,
            val dropShipType: String,
            val dropShipName: String,
            val nameList: MutableList<String>?,
            val enemyNameList: MutableList<String>,
            val nameCombinedList: MutableList<String>?,
            val enemyNameCombinedList: MutableList<String>?
    )

    class ListItem(
            val seaAreaName: String,
            val mapNumber: String
    ) {
        val id: String = seaAreaName + mapNumber
        var sortieCount = 0
        var bossCount = 0
        val battleLogList = arrayListOf<BattleLog>()
        val bossWinRankMap = hashMapOf(
                "S" to 0,
                "A" to 0,
                "B" to 0,
                "C" to 0,
                "D" to 0,
                "E" to 0
        )
        var arrivalRate = ""
        var bossWinRate = ""

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ListItem

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }

    private class ListItemComparator : Comparator<ListItem> {
        override fun compare(o1: ListItem, o2: ListItem): Int {
            if (o1.mapNumber == "不明") {
                if (o2.mapNumber == "不明") {
                    return 0
                }
                return -1
            } else if (o2.mapNumber == "不明") {
                return 1
            }

            //ex 1-5 16春E1
            val split1 = o1.mapNumber.split("-")
            val split2 = o2.mapNumber.split("-")

            //イベント海域は通常海域より大きい
            var result = split1.size.compareTo(split2.size)
            if (result != 0) return result * -1

            //通常海域同士の比較
            if (split1.size == 2) {
                result = split1[0].toInt().compareTo(split2[0].toInt())
                if (result != 0) return result

                return split1[1].toInt().compareTo(split2[1].toInt())
            }

            //イベント海域同士の比較
            val eventNumber1 = eventToNumber(o1.mapNumber.slice(0..2))
            val eventNumber2 = eventToNumber(o2.mapNumber.slice(0..2))
            result = eventNumber1.compareTo(eventNumber2)
            if (result != 0) return result

            //同じイベント海域の場合
            val areaNumber1 = o1.mapNumber.last().toInt()
            val areaNumber2 = o2.mapNumber.last().toInt()
            return areaNumber1.compareTo(areaNumber2)
        }

        private fun eventToNumber(yearAndSeason: String): Int {
            return when (yearAndSeason) {
                "17秋" -> 19
                "17夏" -> 18
                "17春" -> 17
                "17冬" -> 16
                "16秋" -> 15
                "16夏" -> 14
                "16春" -> 13
                "16冬" -> 12
                "15秋" -> 11
                "15夏" -> 10
                "15春" -> 9
                "15冬" -> 8
                "14秋" -> 7
                "14夏" -> 6
                "14春" -> 5
                "13冬" -> 4
                "13秋" -> 3
                "13夏" -> 2
                "13春" -> 1
                else -> 0
            }
        }
    }

    private fun getMapNumber(battleLog: BattleLog): String {
        return when (battleLog.seaAreaName) {
            "鎮守府正面海域" -> "1-1"
            "南西諸島沖" -> "1-2"
            "製油所地帯沿岸" -> "1-3"
            "南西諸島防衛線" -> "1-4"
            "鎮守府近海" -> "1-5"
            "鎮守府近海航路" -> "1-6"
            "カムラン半島" -> "2-1"
            "バシー島沖" -> "2-2"
            "東部オリョール海" -> "2-3"
            "沖ノ島海域" -> "2-4"
            "沖ノ島沖" -> "2-5"
            "モーレイ海" -> "3-1"
            "キス島沖" -> "3-2"
            "アルフォンシーノ方面" -> "3-3"
            "北方海域全域" -> "3-4"
            "北方AL海域" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                if (battleLog.date.after(sdf.parse("2014-08-29 23:59"))) return "3-5"

                //AL/MI作戦
                when (battleLog.enemyFleetName) {
                    "前衛哨戒艦隊",
                    "北方水雷戦隊",
                    "北方任務部隊",
                    "北方水上打撃部隊A群",
                    "北方水上打撃部隊B群",
                    "対潜掃討部隊" -> "14夏E1"
                    "北方方面主力艦隊" -> {
                        return if (battleLog.enemyNameList.first() == "北方棲姫") "14夏E2"
                        else "14夏E1"
                    }
                    "北方任務部隊A群",
                    "前衛警戒部隊",
                    "戦艦戦隊",
                    "AL防衛哨戒ライン",
                    "高速打撃部隊",
                    "AL最終防衛ライン" -> "14夏E2"
                    else -> "不明"
                }
            }
            "ジャム島攻略作戦" -> "4-1"
            "カレー洋制圧戦" -> "4-2"
            "リランカ島空襲" -> "4-3"
            "カスガダマ沖海戦" -> "4-4"
            "南方海域前面" -> "5-1"
            "珊瑚諸島沖" -> "5-2"
            "サブ島沖海域" -> "5-3"
            "サーモン海域" -> "5-4"
            "サーモン海域北方" -> "5-5"
            "中部海域哨戒線" -> "6-1"
            "MS諸島沖" -> "6-2"
            "グアノ環礁沖海域" -> "6-3"
            "中部北海域ピーコック島沖" -> "6-4"
            "KW環礁沖海域" -> "6-5"
            "サーモン諸島海域" -> "13秋E1"
            "ルンバ沖海域" -> "13秋E2"
            "サンタクロース諸島海域" -> "13秋E3"
            "アイアンボトムサウンド" -> {
                when (battleLog.enemyFleetName) {
                    "敵前衛艦隊",
                    "敵迎撃艦隊",
                    "敵待ち伏せ部隊",
                    "敵航空基地守備部隊",
                    "敵リコリス航空基地" -> "13秋E4"
                    "哨戒潜水艦A群",
                    "哨戒潜水艦B群",
                    "任務部隊支隊",
                    "ソロモン方面守備艦隊前衛",
                    "任務部隊前衛艦隊",
                    "ソロモン方面守備艦隊後衛",
                    "深海任務部隊主隊",
                    "任務部隊前衛艦隊",
                    "ソロモン方面守備艦隊後衛",
                    "深海任務部隊主隊",
                    "深海任務部隊支援群",
                    "深海飛行場基地" -> "15夏E4"
                    else -> {
                        //13秋E4の艦隊名が不明のため所属艦で判定
                        val eFleetGMass = listOf("装甲空母姫", "戦艦タ級elite", "軽巡ヘ級flagship", "軽巡ヘ級flagship", "駆逐ハ級flagship", "駆逐ハ級flagship")
                        battleLog.enemyNameList.withIndex().forEach {
                            if (it.value != eFleetGMass[it.index])
                                return "不明"
                        }
                        return "13秋E4"
                    }
                }
            }
            "サーモン海域最深部" -> "13秋E5"
            "観音崎沖" -> "13冬E1"
            "硫黄島周辺海域" -> "13冬E2"
            "中部太平洋海域" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                return when {
                //アルペイベ
                    battleLog.date.before(sdf.parse("2013-01-09")) -> "13冬E3"
                //索敵機、発艦始め
                    battleLog.date.before(sdf.parse("2014-05-10")) -> "14春E4"
                    else -> "不明"
                }
            }
            "南西海域サメワニ沖" -> "14春E1"
            "南西海域ズンダ海峡" -> "14春E2"
            "ポートワイン沖海域" -> "14春E3"
            "北太平洋海域" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                return if (battleLog.date.before(sdf.parse("2014-05-10"))) "14春E5"
                else "2014夏E3"
            }
            "北太平洋MI諸島近海" -> "14夏E4"
            "北太平洋MI諸島沖" -> "14夏E5"
            "本土南西諸島近海" -> "14夏E6"
            "南西方面海域" -> {
                //発動！渾作戦
                when (battleLog.enemyFleetName) {
                    "前衛巡洋艦隊",
                    "前衛水雷戦隊",
                    "任務部隊 A群",
                    "任務部隊 B群",
                    "南方前衛主力艦隊" -> "14秋E1"
                    "潜水艦警戒線",
                    "軽巡戦隊",
                    "重巡戦隊",
                    "警戒部隊",
                    "警戒任務部隊",
                    "嚮導水雷戦隊旗艦" -> "14秋E2"
                    "ピケット水雷戦隊 B群",
                    "ピケット水雷戦隊 A群",
                    "水上打撃部隊",
                    "任務部隊 D群",
                    "任務部隊 C群",
                        //wikiだと艦隊ではなく戦隊になっている
                    "南東方面主力艦隊",
                    "南東方面主力戦隊" -> "14秋E3"
                    else -> {
                        Logger.d(TAG, battleLog.toString())
                        "不明"
                    }
                }
            }
            "パラオ諸島沖" -> "14秋E4"
            "トラック泊地" -> {
                when (battleLog.enemyFleetName) {
                    "潜水艦隊 I群",
                    "潜水艦隊 II群",
                    "潜水艦隊 III群",
                    "潜水艦隊支援部隊",
                    "潜水艦隊 IV群",
                    "潜水艦隊群 旗艦艦隊" -> "15冬E1"
                    "前衛ピケット艦隊 II群",
                    "前衛ピケット艦隊 I群",
                    "任務部隊 I群",
                    "前衛水上打撃部隊",
                    "任務部隊 II群",
                    "前衛護衛空母群",
                    "敵機動部隊先鋒" -> "15冬E2"
                    else -> "不明"
                }
            }
            "トラック泊地沖" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                return when {
                    battleLog.date.before(sdf.parse("2015-02-24")) -> "15冬E3"
                    battleLog.date.before(sdf.parse("2017-03-01")) -> "17冬E3"
                    else -> "不明"
                }
            }
            "トラック諸島海域" -> {
                when (battleLog.enemyFleetName) {
                    "第1波残存艦隊",
                    "空母機動部隊 IV群",
                    "空母機動部隊 V群",
                    "機動部隊支援 補給タンカー",
                    "空母機動部隊 II群",
                    "空母機動部隊 III群",
                    "空母機動部隊 I群",
                    "空母機動部隊 主力" -> "15冬E4"
                    "主力部隊前衛艦隊",
                    "残存潰走中部隊",
                    "水上打撃部隊主力",
                    "空母機動部隊主力",
                    "任務部隊 A群",
                    "任務部隊 B群",
                    "任務部隊 C群",
                    "任務部隊 旗艦艦隊" -> "15冬E5"
                    else -> "不明"
                }
            }
            "カレー洋" -> "15春E1"
            "カレー洋リランカ島沖" -> {
                when (battleLog.enemyFleetName) {
                    "東洋方面反攻先遣隊",
                    "東洋方面威力偵察部隊",
                    "東洋艦隊新編水上打撃部隊",
                    "深海東洋方面潜水艦隊",
                    "深海東洋艦隊機動部隊",
                    "東洋方面後方兵站部隊",
                    "深海東洋方面増援艦隊",
                    "リランカ島港湾守備隊" -> "4-5"
                    "深海東洋艦隊 巡洋戦隊",
                    "深海東洋艦隊 戦艦戦隊 B部隊",
                    "深海東洋艦隊 機動部隊",
                    "深海東洋艦隊 戦艦戦隊 A部隊",
                    "深海東洋艦隊 補給輸送船団",
                    "深海東洋艦隊 機動部隊旗艦" -> "15春E2"
                    "深海棲艦 東洋艦隊 前衛部隊",
                    "深海棲艦 機動部隊 前衛",
                    "深海棲艦 東洋艦隊 A部隊",
                    "深海棲艦 東洋艦隊 B部隊",
                    "深海棲艦 リランカ島 港湾部",
                    "深海棲艦 機動部隊",
                    "深海棲艦 機動部隊 後方",
                    "リランカ島 主要港湾部" -> "15春E4"
                    "深海東方前方展開潜水艦隊 III群",
                    "深海東方前方展開潜水艦隊 II群",
                    "海峡前進配備 深海魚雷艇襲撃隊",
                    "リランカ基地防衛部隊",
                    "深海東方重巡戦隊",
                    "リランカ港湾基地", //IとHの2マスある
                    "深海東方緊急展開 前衛機動部隊",
                    "深海東方前方展開潜水艦隊 I群",
                    "深海東方緊急展開 前衛機動部隊",
                    "深海東方緊急展開 主力機動部隊",
                    "深海東方雷巡戦隊",
                    "深海精鋭駆逐隊" -> "17夏E2"
                    else -> "不明"
                }
            }
            "ベーグル湾" -> "15春E3"
            "アンズ環礁沖" -> "15春E5"
            "ステビア海" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                when {
                    battleLog.date.before(sdf.parse("2015-05-19")) -> "15春E6"
                    battleLog.date.before(sdf.parse("2017-09-13")) -> "17夏E3"
                    else -> "不明"
                }
            }
            "ショートランド沖" -> "15夏E1"
            "ソロモン海" -> "15夏E2"
            "南太平洋海域" -> "15夏E3"
            "西方海域戦線 カレー洋" -> "15夏E5"
            "ソロモン海東部海域" -> "15夏E6"
            "FS方面海域" -> "15夏E7"
            "ショートランド泊地沖" -> "15秋E1"
            "コロネハイカラ島沖" -> "15秋E2"
            "コロネハイカラ島東方沖" -> "15秋E3"
            "西方海域戦線 ステビア海" -> "15秋E4"
            "バニラ湾沖" -> "15秋E5"
            "カンパン湾沖" -> "16冬E1"
        //wikiだと"湾"が入ってるけどログにはない...
            "オートロ島マーマレード沖",
            "オートロ島マーマレード湾沖" -> "16冬E2"
            "北海道北東沖" -> "16冬E3"
            "北太平洋前線海域" -> {
                when (battleLog.enemyFleetName) {
                    "前衛水上打撃部隊 I群",
                    "前衛警戒水雷戦隊",
                    "北太平洋潜水艦隊 II群",
                    "前衛水上打撃部隊 II群",
                    "深海重雷装戦隊",
                    "北太平洋潜水艦隊 I群",
                    "深海島嶼防衛任務部隊",
                    "深海増援護衛船団",
                    "島嶼防衛艦隊旗艦" -> "16春E1"
                    "警戒哨戒線",
                    "前衛空母任務部隊",
                    "潜水艦哨戒線",
                    "上陸阻止ライン",
                    "深海魚雷艇戦隊",
                    "救援深海重巡戦隊",
                    "島嶼防衛要塞" -> "16春E2"
                    "群狼潜水艦隊C群",
                    "深海基地航空隊 第2航空隊",
                    "深海第二水雷戦隊",
                    "群狼潜水戦隊 B群",
                    "深海第一水雷戦隊",
                    "群狼潜水艦隊 A群",
                    "深海基地航空隊 第1航空隊",
                    "魚雷艇突撃戦隊",
                    "深海駆逐隊旗艦" -> "16春E3"
                    "逆襲任務部隊 III群",
                    "逆襲任務部隊 II群",
                    "逆襲任務部隊 I群",
                    "逆襲潜水艦隊 B群",
                    "逆襲空母主力任務部隊",
                    "逆襲前衛水雷戦隊",
                    "逆襲潜水艦隊 A群",
                    "逆襲水上打撃部隊前衛部隊",
                    "逆襲水上打撃部隊本隊",
                    "逆襲部隊旗艦艦隊" -> "16春E4"
                    else -> {
                        Logger.d(TAG, battleLog.toString())
                        "不明"
                    }
                }
            }
            "南方ラバウル基地戦域" -> "16春E5"
            "北太平洋深海中枢泊地沖" -> "16春E6"
            "北太平洋戦域" -> "16春E7"
            "南西海域 ブンタン沖" -> "16夏E1"
            "南西海域 エンドウ沖" -> "16夏E2"
            "南西海域 マレー沖" -> "16夏E3"
            "南西海域 マラッカ海峡沖" -> "16夏E4"
            "本土近海諸島補給線" -> "16秋E1"
            "本土沖" -> "16秋E2"
            "本土沖太平洋上" -> "16秋E3"
            "MS諸島北部" -> "16秋E4"
            "MS諸島北部 B環礁沖" -> "16秋E5"
            "日本近海/七尾北湾/舞鶴湾" -> "17冬E1"
            "小笠原諸島航路" -> "17冬E2"
            "津軽海峡/北海道沖" -> {
                when (battleLog.enemyFleetName) {
                    "通商破壊部隊 北兎支援部隊 A群",
                    "通商破壊部隊 北兎支援部隊 B群",
                    "深海北方 奇襲侵攻部隊",
                    "深海北兎潜水艦隊 II群",
                    "通商破壊部隊 偵察水雷戦隊",
                    "通商破壊部隊 侵入巡洋艦戦隊",
                    "深海前方展開 巡洋艦戦隊",
                    "深海北兎潜水艦隊 I群",
                    "深海北兎潜水艦隊 旗艦" -> "17春E1"
                    "深海北兎潜水艦隊 残存部隊",
                    "深海北方展開群 上陸部隊前衛",
                    "深海北方展開群 上陸支援部隊",
                    "深海北方展開群 奇襲上陸部隊",
                    "深海北方展開群 前方警戒部隊",
                    "深海北方展開群 前方潜水艦隊",
                    "深海北方展開群 前衛巡洋戦隊",
                    "深海北方展開群 攻撃隊",
                    "深海北方展開群 襲撃部隊部隊",
                    "深海北方展開群 攻撃隊",
                    "深海北方展開群 護衛空母部隊",
                    "深海北方展開群 泊地襲撃部隊",
                    "深海北方展開群 泊地襲撃隊旗艦" -> "17春E2"
                    else -> "不明"
                }
            }
            "千島列島沖" -> "17春E3"
            "占守島沖" -> "17春E4"
            "大ホッケ海北方" -> "17春E5"
            "リンガ泊地沖" -> "17夏E1"
            "紅海" -> "17夏E4"
            "地中海キプロス島沖" -> "17夏E5"
            "地中海マルタ島沖" -> "17夏E6"
            "北大西洋海域" -> "17夏E7"
            else -> "不明"
        }
    }
}