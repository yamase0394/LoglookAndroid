package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.SortieBattleresult;
import jp.gr.java_conf.snake0394.loglook_android.logger.BattleLogger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_combined_battle/battleresult")
public class ApiReqCombinedBattleBattleresult implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getAsJsonObject("api_data");
        SortieBattleresult battleresult = new Gson().fromJson(data, SortieBattleresult.class);

        try {
            JsonObject apiEscape = data.getAsJsonObject("api_escape");
            int damaged = apiEscape.getAsJsonArray("api_escape_idx").get(0).getAsInt();
            int towing = apiEscape.getAsJsonArray("api_tow_idx").get(0).getAsInt();
            if (damaged > 6) {
                damaged = DeckManager.INSTANCE.getDeck(2).getShipId().get(damaged - 7);
            } else {
                damaged = DeckManager.INSTANCE.getDeck(1).getShipId().get(damaged - 1);
            }
            if (towing > 6) {
                towing = DeckManager.INSTANCE.getDeck(2).getShipId().get(towing - 7);
            } else {
                towing = DeckManager.INSTANCE.getDeck(1).getShipId().get(towing - 1);
            }
            Escape.INSTANCE.ready(damaged, towing);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BattleLogger.INSTANCE.writeLog(battleresult);
    }
}
