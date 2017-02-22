package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.util.Log;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.api.API;
import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/21.
 */
@API("")
public class ApiLogger implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        Log.d("uri", req.getRequestURI());
        Log.d("request", req.getRequestBody());
        Log.d("response", json.toString());
    }
}
