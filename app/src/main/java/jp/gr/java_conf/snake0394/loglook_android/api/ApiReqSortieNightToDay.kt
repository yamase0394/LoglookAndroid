package jp.gr.java_conf.snake0394.loglook_android.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.CombinedBattleBattleWater
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.CombinedBattleEcNightToDay
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.SortieNightToDay
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData

/**
 * Created by raide on 2017/11/20.
 */
@API("/kcsapi/api_req_sortie/night_to_day")
class ApiReqSortieNightToDay : APIListenerSpi {
    override fun accept(json: JsonObject, req: RequestMetaData?, res: ResponseMetaData?) {
        val data = json.getAsJsonObject("api_data")
        val hou = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(data, SortieNightToDay::class.java)
        Logger.d("SortieNightToDay", hou.toString())
        TacticalSituation.applyBattle(hou)
        Logger.d("SortieNightToDay", TacticalSituation.phaseList.toString())
    }
}