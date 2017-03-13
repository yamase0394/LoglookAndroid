package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import java.util.Calendar;

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

        if (Integer.parseInt(req.getParameterMap().get("api_highspeed").get(0)) == 0) {
            int dockId = Integer.parseInt(req.getParameterMap().get("api_ndock_id").get(0));
            int shipId = Integer.parseInt(req.getParameterMap().get("api_ship_id").get(0));
            DockTimer.INSTANCE.startTimer(App.getInstance(), dockId, shipId, Calendar.getInstance().getTimeInMillis() + MyShipManager.INSTANCE.getMyShip(shipId).getNdockTime());
        }
    }
}
