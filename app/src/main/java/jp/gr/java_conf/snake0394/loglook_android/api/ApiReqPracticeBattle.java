package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_practice/battle")
public class ApiReqPracticeBattle implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getAsJsonObject("api_data");
        jp.gr.java_conf.snake0394.loglook_android.bean.battle.PracticeBattle hou = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(data, jp.gr.java_conf.snake0394.loglook_android.bean.battle.PracticeBattle.class);
        Logger.d("PracticeBattle", hou.toString());
        TacticalSituation.INSTANCE.applyBattle(hou);
        Logger.d("PracticeBattle", TacticalSituation.INSTANCE.getPhaseList()
                .toString());
    }
}
