package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_nyukyo/start")
public class ApiReqNyukyoStart implements APIListenerSpi {
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

        String regex = "api%5Fhighspeed=(\\d+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(requestBody);
        m.find();
        if (Integer.parseInt(m.group(1)) == 0) {
            regex = "api%5Fndock%5Fid=(\\d+)";
            p = Pattern.compile(regex);
            m = p.matcher(requestBody);
            m.find();
            int dockId = Integer.parseInt(m.group(1));
            regex = "api%5Fship%5Fid=(\\d+)";
            p = Pattern.compile(regex);
            m = p.matcher(requestBody);
            m.find();
            int shipId = Integer.parseInt(m.group(1));
            DockTimer.INSTANCE.startTimer(App.getInstance(), dockId, shipId, Calendar.getInstance().getTimeInMillis() + MyShipManager.INSTANCE.getMyShip(shipId).getNdockTime());
        }
    }
}
