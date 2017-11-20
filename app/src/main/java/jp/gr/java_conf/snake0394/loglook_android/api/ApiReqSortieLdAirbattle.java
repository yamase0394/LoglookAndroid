package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.battle.SortieLdAirbattle;
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

@API("/kcsapi/api_req_sortie/ld_airbattle")
public class ApiReqSortieLdAirbattle implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getAsJsonObject("api_data");
        SortieLdAirbattle battle = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(data, SortieLdAirbattle.class);
        Logger.d("SortieLdAirbattle", battle.toString());
        TacticalSituation.INSTANCE.applyBattle(battle);
        Logger.d("SortieLdAirbattle", TacticalSituation.INSTANCE.getPhaseList()
                .toString());
    }
}
