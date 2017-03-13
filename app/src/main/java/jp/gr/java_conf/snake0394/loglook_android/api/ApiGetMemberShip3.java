package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_get_member/ship3")
public class ApiGetMemberShip3 implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getAsJsonObject("api_data");

        JsonArray apiShipData = data.getAsJsonArray("api_ship_data");
        for (JsonElement e : apiShipData) {
            MyShip myShip = new Gson().fromJson(e, MyShip.class);
            if (!e.getAsJsonObject()
                  .has("api_sally_area")) {
                myShip.setSallyArea(-1);
            }
            MyShipManager.INSTANCE.put(myShip.getId(), myShip);
        }
    }
}
