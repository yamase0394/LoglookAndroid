package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMissionManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitemManager;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/16.
 */
@API("/kcsapi/api_start2")
public class ApiStart2 implements APIListenerSpi {

    private Gson gson;

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        this.gson = new Gson();

        JsonObject data = json.getAsJsonObject("api_data");

        this.apiMstShip(data.getAsJsonArray("api_mst_ship"));
        this.apiMstSlotitem(data.getAsJsonArray("api_mst_slotitem"));
        this.apiMstUseitem(data.getAsJsonArray("api_mst_useitem"));
        this.apiMstMission(data.getAsJsonArray("api_mst_mission"));
    }

    private void apiMstShip(JsonArray array) {
        for (JsonElement e : array) {
            MstShipManager.INSTANCE.put(gson.fromJson(e, MstShip.class));
        }
        //MstShipのnullオブジェクトをput
        MstShip mstShip = new MstShip();
        mstShip.setId(-1);
        mstShip.setName("");
        mstShip.setYomi("");
        MstShipManager.INSTANCE.put(mstShip);
        //MstShipManagerをシリアライズ
        MstShipManager.INSTANCE.serialize();
    }

    private void apiMstSlotitem(JsonArray array) {
        for (JsonElement e : array) {
            MstSlotitemManager.INSTANCE.put(gson.fromJson(e, MstSlotitem.class));
        }
        MstSlotitem ms = new MstSlotitem();
        ms.setId(-1);
        ms.setName("");
        MstSlotitemManager.INSTANCE.put(ms);
        //MstSlotMapをシリアライズ
        MstSlotitemManager.INSTANCE.serialize();
    }

    private void apiMstUseitem(JsonArray array) {
        for (JsonElement e : array) {
            MstUseitemManager.INSTANCE.put(gson.fromJson(e, MstUseitem.class));
        }
        //MstUseitemManagerをシリアライズ
        MstUseitemManager.INSTANCE.serialize();
    }

    private void apiMstMission(JsonArray array) {
        for (JsonElement e : array) {
            MstMissionManager.INSTANCE.put(gson.fromJson(e, MstMission.class));
        }
        //MstMissionManagerをシリアライズ
        MstMissionManager.INSTANCE.serialize();
    }
}
