package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiHougeki;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiHougekiDeserializer;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.CombinedBattleMidnightBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

@API("/kcsapi/api_req_combined_battle/midnight_battle")
public class ApiReqCombinedBattleMidnightBattle implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        try {
            JsonObject data = json.getAsJsonObject("api_data");
            CombinedBattleMidnightBattle hou = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(ApiHougeki.class, new ApiHougekiDeserializer())
                    .create()
                    .fromJson(data, CombinedBattleMidnightBattle.class);
            Logger.d("CombinedBattleMidnightBattle", hou.toString());
            TacticalSituation.INSTANCE.applyMidnightBattle(hou);
            Logger.d("CombinedBattleMidnightBattle", TacticalSituation.INSTANCE.getPhaseList().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
