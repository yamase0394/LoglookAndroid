package jp.gr.java_conf.snake0394.loglook_android.api;

import android.content.Intent;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.WinRankOverlayService;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/07/12.
 */
@API("kcsapi/api_req_practice/battle_result")
public class ApiReqPracticeBattleResult implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        App.getInstance().stopService(new Intent(App.getInstance().getApplicationContext(), WinRankOverlayService.class));
    }
}
