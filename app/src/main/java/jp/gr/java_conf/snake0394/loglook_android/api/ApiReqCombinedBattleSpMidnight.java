package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedSpMidnight;
import jp.gr.java_conf.snake0394.loglook_android.bean.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.BattleLogger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_combined_battle/sp_midnight")
public class ApiReqCombinedBattleSpMidnight implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        CombinedSpMidnight combinedSpMidnight = new CombinedSpMidnight();
        combinedSpMidnight.set(json.toString());
        BattleLogger.INSTANCE.setBattle(combinedSpMidnight);
        TacticalSituation.INSTANCE.set(combinedSpMidnight);
    }
}
