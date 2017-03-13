package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/03/02.
 */
@API("/kcsapi/api_get_member/ndock")
public class ApiGetMemberNdock implements APIListenerSpi{
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        DockTimer.INSTANCE.clearNotifications();
    }
}
