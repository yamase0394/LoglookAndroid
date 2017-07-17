package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.battle.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_map/next")
public class ApiReqMapNext implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        //App.getInstance().stopService(new Intent(App.getInstance().getApplicationContext(), HeavilyDamagedWarningService.class));
        JsonObject data = json.getAsJsonObject("api_data");
        TacticalSituation.INSTANCE.setBoss(data.get("api_event_id").getAsInt() == 5);
        Logger.d(ApiReqMapNext.class.getName(), "isBoss=" + TacticalSituation.INSTANCE.isBoss());
    }
}
