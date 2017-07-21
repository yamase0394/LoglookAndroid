package jp.gr.java_conf.snake0394.loglook_android.logger

import android.os.Environment
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import jp.gr.java_conf.snake0394.loglook_android.BattleUtility
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation
import jp.gr.java_conf.snake0394.loglook_android.api.API
import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.*
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*


/**
 * 戦闘をCSVファイルに記録します。
 */
@API("/kcsapi/api_req_map/start", "/kcsapi/api_req_map/next", "/kcsapi/api_req_sortie/battleresult", "/kcsapi/api_req_combined_battle/battleresult")
class BattleLogger : APIListenerSpi {

    private var isFirstBattle = false
    private var cell: Int = 0
    private var isBoss = false

    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {
        val data = json.getAsJsonObject("api_data")
        when (req.requestURI) {
            "/kcsapi/api_req_map/start" -> {
                isFirstBattle = true
                this.cell = data.get("api_no").asInt
                this.isBoss = data.get("api_event_id").asInt == 5
            }
            "/kcsapi/api_req_map/next" -> {
                this.cell = data.get("api_no").asInt
                this.isBoss = data.get("api_event_id").asInt == 5
            }
            "/kcsapi/api_req_sortie/battleresult", "/kcsapi/api_req_combined_battle/battleresult" -> {
                val result = GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                        .fromJson(data, SortieBattleresult::class.java)
                writeLog(result)
            }
        }
    }

    private fun writeLog(result: SortieBattleresult) {
        val sdcardPath = File(Environment.getExternalStorageDirectory().path + "/泥提督支援アプリ/")
        val filePath = sdcardPath.toString() + File.separator + "海戦・ドロップ報告書.csv"

        //フォルダがなければ作成
        if (!sdcardPath.exists()) {
            sdcardPath.mkdir()
        }

        try {
            val battle = TacticalSituation.battle
            val sb = StringBuffer()
            val file = File(filePath)
            if (!file.exists()) {
                sb.append("日付,海域,マス,ボス,ランク,交戦形態,味方陣形,敵陣形,制空状態,味方触接機,敵触接機,敵艦隊名,ドロップ艦種,ドロップ艦娘,味方艦1,味方艦1HP,味方艦2,味方艦2HP,味方艦3,味方艦3HP,味方艦4,味方艦4HP,味方艦5,味方艦5HP,味方艦6,味方艦6HP,敵艦1,敵艦1HP,敵艦2,敵艦2HP,敵艦3,敵艦3HP,敵艦4,敵艦4HP,敵艦5,敵艦5HP,敵艦6,敵艦6HP\r\n")
            }

            val pw = BufferedWriter(OutputStreamWriter(FileOutputStream(filePath, true), "SJIS"))
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sb.append("${sdf.format(Calendar.getInstance().time)},${result.apiQuestName},${cell},")
            sb.append("${when {
                isFirstBattle && isBoss -> "出撃&ボス"
                isFirstBattle -> "出撃"
                isBoss -> "ボス"
                else -> ""
            }},${result.apiWinRank},")
            (battle as IFormation).run {
                sb.append("${BattleUtility.getTactic(apiFormation[2])},${BattleUtility.getFormation(apiFormation[0])},${BattleUtility.getFormation(apiFormation[1])},")
            }
            when (battle) {
                is IKouku -> {
                    (battle as IKouku).apiKouku.apiStage1!!.run {
                        sb.append("${BattleUtility.getDispSeiku(apiDispSeiku)},")
                        sb.append("${MstSlotitemManager.INSTANCE.getMstSlotitem(apiTouchPlane[0])?.name ?: ""},${MstSlotitemManager.INSTANCE.getMstSlotitem(apiTouchPlane[1])?.name ?: ""},")
                    }
                }
                is IMidnightBattle -> {
                    (battle as IMidnightBattle).run {
                        sb.append(",${MstSlotitemManager.INSTANCE.getMstSlotitem(apiTouchPlane[0])?.name ?: ""},${MstSlotitemManager.INSTANCE.getMstSlotitem(apiTouchPlane[1])?.name ?: ""},")
                    }
                }
                else -> Logger.d("BattleLogger", "illegal battle:${battle.javaClass.name}")
            }
            sb.append("${result.apiEnemyInfo.apiDeckName},${result.apiGetShip?.apiShipType ?: ""},${result.apiGetShip?.apiShipName ?: ""},")

            val deck = DeckManager.INSTANCE.getDeck(battle.apiDeckId)
            val shipId = deck.shipId
            val nowhps = battle.apiNowhps
            val maxhps = battle.apiMaxhps
            for (i in 1..6) {
                if (shipId[i - 1] == -1) {
                    sb.append(",,")
                    continue
                }
                val myShip = MyShipManager.INSTANCE.getMyShip(shipId[i - 1])
                val mstShip = MstShipManager.INSTANCE.getMstShip(myShip.shipId)
                sb.append("${mstShip.name}(Lv${myShip.lv}),${nowhps[i]}/${maxhps[i]},")
            }

            val eship = battle.apiShipKe
            for (i in 1..6) {
                if (eship[i] == -1) {
                    sb.append(",")
                } else {
                    sb.append(MstShipManager.INSTANCE.getMstShip(eship[i]).name)
                    val yomi = MstShipManager.INSTANCE.getMstShip(eship[i]).yomi
                    if (yomi != "" && yomi != "-") {
                        sb.append("($yomi)")
                    }
                    sb.append(",${nowhps[i + 6]}/${maxhps[i + 6]}")
                }

                if (i == 6) {
                    break
                }

                sb.append(",")
            }

            Log.d("battlelog", sb.toString())
            sb.append("\r\n")
            pw.write(sb.toString())
            pw.flush()
            pw.close()
        } catch (e: Exception) {
            e.printStackTrace()
            ErrorLogger.writeLog(e)
        } finally {
            isFirstBattle = false
        }

    }
}
