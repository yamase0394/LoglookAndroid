package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.battle.CombinedBattleEcMidnightBattle;
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

@API("/kcsapi/api_req_combined_battle/ec_midnight_battle")
public class ApiReqCombinedBattleEcMidnightBattle implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getAsJsonObject("api_data");
        CombinedBattleEcMidnightBattle hou = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(data, CombinedBattleEcMidnightBattle.class);
        Logger.d("CombinedBattleEcMidnightBattle", hou.toString());
        TacticalSituation.INSTANCE.applyMidnightBattle(hou);
        Logger.d("CombinedBattleEcMidnightBattle", TacticalSituation.INSTANCE.getPhaseList()
                .toString());
    }
}
