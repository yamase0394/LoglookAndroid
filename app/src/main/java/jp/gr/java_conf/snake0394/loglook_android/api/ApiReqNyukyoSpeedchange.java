package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_nyukyo/speedchange")
public class ApiReqNyukyoSpeedchange implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        String requestBody;
        try {
            requestBody = IOUtils.toString(req.getRequestBody()
                                              .get(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();

            return;
        }

        String regex = "api%5Fndock%5Fid=(\\d+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(requestBody);
        m.find();
        DockTimer.INSTANCE.stop(Integer.parseInt(m.group(1)));
    }
}
