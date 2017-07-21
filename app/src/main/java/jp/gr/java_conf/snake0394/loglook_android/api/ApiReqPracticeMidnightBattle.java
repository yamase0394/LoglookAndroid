package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiHougeki;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ApiHougekiDeserializer;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.PracticeMidnightBattle;
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_practice/midnight_battle")
public class ApiReqPracticeMidnightBattle implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getAsJsonObject("api_data");
        PracticeMidnightBattle hou = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(ApiHougeki.class, new ApiHougekiDeserializer())
                .create()
                .fromJson(data, PracticeMidnightBattle.class);
        Logger.d("PracticeMidnightBattle", hou.toString());
        TacticalSituation.INSTANCE.applyMidnightBattle(hou);
        Logger.d("PracticeMidnightBattle", TacticalSituation.INSTANCE.getPhaseList()
                .toString());
    }
}
