package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String requestBody = req.getRequestBody();

        String regex = "Fkdock%5Fid=(\\d+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(requestBody);
        m.find();
        int kdockId = Integer.parseInt(m.group(1));

        regex = "large%5Fflag=(\\d+)";
        p = Pattern.compile(regex);
        m = p.matcher(requestBody);
        m.find();
        int largeFlag = Integer.parseInt(m.group(1));

        CreateShipLogger.INSTANCE.ready(kdockId,largeFlag);
    }
}
