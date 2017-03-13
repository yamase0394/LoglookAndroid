package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.PracticeBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_practice/battle")
public class ApiReqPracticeBattle implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        PracticeBattle practiceBattle = new PracticeBattle();
        practiceBattle.set(json.toString());
        TacticalSituation.INSTANCE.set(practiceBattle);
    }
}
