package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiHougeki;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiHougekiDeserializer;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiOpeningTaisen;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiOpeningTaisenDeserializer;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.CombinedBattleBattleWater;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_combined_battle/battle_water")
public class ApiReqCombinedBattleBattleWater implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        /*
        CombinedWater combinedWater = new CombinedWater();
        combinedWater.set(json.toString());
        //TacticalSituation.INSTANCE.set(combinedWater);
        */

        try {
            JsonObject data = json.getAsJsonObject("api_data");
            CombinedBattleBattleWater hou = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(ApiHougeki.class, new ApiHougekiDeserializer())
                    .registerTypeAdapter(ApiOpeningTaisen.class, new ApiOpeningTaisenDeserializer())
                    .create()
                    .fromJson(data, CombinedBattleBattleWater.class);
            Logger.d("CombinedBattleBattleWater", hou.toString());
            TacticalSituation.INSTANCE.applyBattle(hou);
            Logger.d("CombinedBattleBattleWater", TacticalSituation.INSTANCE.getPhaseList().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
