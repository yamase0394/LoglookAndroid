package jp.gr.java_conf.snake0394.loglook_android.api

import com.google.gson.JsonObject
import jp.gr.java_conf.snake0394.loglook_android.Escape
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_combined_battle/battleresult")
class ApiReqCombinedBattleBattleresult : APIListenerSpi {
    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {

        val data = json.getAsJsonObject("api_data")

        if (!data.get("api_escape").isJsonNull) {
            val apiEscape = data.getAsJsonObject("api_escape")
            var damaged = apiEscape.getAsJsonArray("api_escape_idx").get(0).asInt
            var towing = apiEscape.getAsJsonArray("api_tow_idx").get(0).asInt
            if (damaged > 6) {
                damaged = DeckManager.INSTANCE.getDeck(2).shipId[damaged - 7]
            } else {
                damaged = DeckManager.INSTANCE.getDeck(1).shipId[damaged - 1]
            }
            if (towing > 6) {
                towing = DeckManager.INSTANCE.getDeck(2).shipId[towing - 7]
            } else {
                towing = DeckManager.INSTANCE.getDeck(1).shipId[towing - 1]
            }
            Escape.INSTANCE.ready(damaged, towing)
        }

        /*
        //勝利ランクオーバーレイを消す
        App.getInstance().stopService(Intent(App.getInstance().applicationContext, WinRankOverlayService::class.java))

        val prefs = GeneralPrefs(App.getInstance().applicationContext)
        if (!prefs.showsHeavilyDamagedOverlay) {
            return
        }

        //大破警告オーバーレイ
        val phaseStates = TacticalSituation.phaseList
        val battle = TacticalSituation.battle
        for (i in 0..phaseStates[0].fHp.size - 1) {
            val lastHp = phaseStates[phaseStates.size - 1].fHp[i]
            val maxHp = battle.apiMaxhps[i + 1]
            if (lastHp <= maxHp / 4) {
                App.getInstance().startService(Intent(App.getInstance().applicationContext, HeavilyDamagedWarningService::class.java))
                return
            }
        }

        if(phaseStates[0].fHpCombined != null) {
            for (i in 0..phaseStates[0].fHpCombined!!.size - 1) {
                val lastHp = phaseStates[phaseStates.size - 1].fHpCombined!![i]
                if (battle is ICombinedBattle) {
                    val maxHp = battle.apiMaxhpsCombined[i + 1]
                    if (lastHp <= maxHp / 4) {
                        App.getInstance().startService(Intent(App.getInstance().applicationContext, HeavilyDamagedWarningService::class.java))
                        return
                    }
                } else if (battle is IEachCombinedBattle) {
                    val maxHp = battle.apiMaxhpsCombined[i + 1]
                    if (lastHp <= maxHp / 4) {
                        App.getInstance().startService(Intent(App.getInstance()
                                .applicationContext, HeavilyDamagedWarningService::class.java))
                        return
                    }
                }
            }
        }

        App.getInstance().stopService(Intent(App.getInstance().applicationContext, HeavilyDamagedWarningService::class.java))
        */
    }
}
