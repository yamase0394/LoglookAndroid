package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import jp.gr.java_conf.snake0394.loglook_android.api.API;
import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/21.
 */
@API("")
public class ApiLogger implements APIListenerSpi{

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        try {
            Log.d("uri", req.getRequestURI());
            Log.d("request", IOUtils.toString(req.getRequestBody()
                                                     .get(), "UTF-8"));
            Log.d("response", json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
