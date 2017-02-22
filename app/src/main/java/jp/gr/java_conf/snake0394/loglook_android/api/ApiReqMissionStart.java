package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.MissionTimer;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_mission/start")
public class ApiReqMissionStart implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        int deckId = Integer.parseInt(req.getParameterMap().get("api_deck_id").get(0));
        int missionId = Integer.parseInt(req.getParameterMap().get("api_mission_id").get(0));
        MissionTimer.INSTANCE.ready(deckId, missionId);


        JsonObject data = json.getAsJsonObject("api_data");
        MissionTimer.INSTANCE.startTimer(App.getInstance(), data.get("api_complatetime")
                                                                .getAsLong());
    }
}
