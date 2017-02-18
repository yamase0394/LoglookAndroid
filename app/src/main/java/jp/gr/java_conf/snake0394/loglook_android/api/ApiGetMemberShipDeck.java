package jp.gr.java_conf.snake0394.loglook_android.api;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.HeavilyDamagedWarningActivity;

/**
 * api_req_map/nextの直前
 */
@API("/kcsapi/api_get_member/ship_deck")
public class ApiGetMemberShipDeck implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getAsJsonObject("api_data");
        JsonArray apiShipData = data.getAsJsonArray("api_ship_data");

        ArrayList<Integer> heavyDamaged = new ArrayList<>();
        for (JsonElement e : apiShipData) {
            MyShip jsonMyShip = new Gson().fromJson(e, MyShip.class);
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(jsonMyShip.getId());

            myShip.setLv(jsonMyShip.getLv());
            myShip.setNowhp(jsonMyShip.getNowhp());
            myShip.setMaxhp(jsonMyShip.getMaxhp());
            myShip.setOnslot(jsonMyShip.getOnslot());
            myShip.setCond(jsonMyShip.getCond());
            myShip.setFuel(jsonMyShip.getFuel());
            myShip.setBull(jsonMyShip.getBull());
            if (myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                /*
                退避している場合は警告を出さない
                if (Escape.INSTANCE.isEscaped(shipObj[i].getInt("api_id"))) {
                   continue;
                }
                */
                heavyDamaged.add(myShip.getId());
            }
        }
        if (!heavyDamaged.isEmpty()) {
            //大破進撃警告画面を表示
            Intent intent = new Intent(App.getInstance(), HeavilyDamagedWarningActivity.class);
            intent.putIntegerArrayListExtra("shipId", heavyDamaged);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            App.getInstance().startActivity(intent);
        }
    }
}
