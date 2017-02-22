package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.logger.CreateShipLogger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_kousyou/createship")
public class ApiReqKousyouCreateship implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        int kdockId = Integer.parseInt(req.getParameterMap().get("api_kdock_id").get(0));
        int largeFlag = Integer.parseInt(req.getParameterMap().get("api_large_flag").get(0));

        CreateShipLogger.INSTANCE.ready(kdockId,largeFlag);
    }
}
