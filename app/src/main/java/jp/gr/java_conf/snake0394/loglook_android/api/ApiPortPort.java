package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.DeckUtility;
import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.MissionTimer;
import jp.gr.java_conf.snake0394.loglook_android.NotificationUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.Basic;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.Material;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.logger.MaterialLogger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_port/port")
public class ApiPortPort implements APIListenerSpi {
    private Gson gson;

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        this.gson = new Gson();

        JsonObject data = json.getAsJsonObject("api_data");

        //api_shipが先でないとapi_deck_portでnullが発生する
        this.apiShip(data.getAsJsonArray("api_ship"));
        this.apiMaterial(data.getAsJsonArray("api_material"));
        this.apiDeckPort(data.getAsJsonArray("api_deck_port"));
        this.apiNdock(data.getAsJsonArray("api_ndock"));
        this.apiBasic(data.getAsJsonObject("api_basic"));

        Escape.INSTANCE.close();
    }

    private void apiMaterial(JsonArray array) {
        List<Integer> materialList = new ArrayList<>();
        for (JsonElement e : array) {
            materialList.add(e.getAsJsonObject()
                              .get("api_value")
                              .getAsInt());
        }

        //[4]高速建造剤と[5]高速修復材を入れ替える
        int temp = materialList.get(4);
        materialList.set(4, materialList.get(5));
        materialList.set(5, temp);
        Material material = new Material();
        material.setMaterialList(materialList);

        MaterialLogger.INSTANCE.writeLog(material);
    }

    private void apiDeckPort(JsonArray array) {
        for (JsonElement e : array) {
            Deck deck = this.gson.fromJson(e, Deck.class);
            deck.setLevelSum(DeckUtility.getLevelSum(deck));
            deck.setCondRecoveryTime(DeckUtility.getCondRecoveryTime(deck));

            DeckManager.INSTANCE.put(deck);

            //遠征タイマーを制御
            List<Long> mission = DeckManager.INSTANCE.getDeck(deck.getId())
                                                     .getMission();
            //艦隊が遠征中か強制帰投中
            if (mission.get(0) == 1 || mission.get(0) == 3) {
                //指定された艦隊に対するタイマーが作動中でない
                if (!(MissionTimer.INSTANCE.isRunning(deck.getId()))) {
                    //遠征タイマーをセット
                    MissionTimer.INSTANCE.ready(deck.getId(), Integer.valueOf(mission.get(1)
                                                                                     .toString()));
                    MissionTimer.INSTANCE.startTimer(App.getInstance(), mission.get(2));
                }
                //未出撃
            } else if (mission.get(0) == 0) {
                //指定された艦隊に対するタイマーが作動中
                if (MissionTimer.INSTANCE.isRunning(deck.getId())) {
                    //遠征タイマーを中断
                    NotificationUtility.cancelNotification(App.getInstance(), deck.getId());
                }
            }
        }
    }

    private void apiNdock(JsonArray array) {
        for (JsonElement e : array) {
            JsonObject obj = e.getAsJsonObject();
            int dockId = obj.get("api_id")
                            .getAsInt();
            int dockingShipId = obj.get("api_ship_id")
                                   .getAsInt();
            if (dockingShipId != 0 && !DockTimer.INSTANCE.isRunning(dockId)) {
                long completeTime = obj.get("api_complete_time")
                                       .getAsLong();
                DockTimer.INSTANCE.startTimer(App.getInstance(), dockId, dockingShipId, completeTime);
            }
        }
    }

    private void apiShip(JsonArray array) {
        List<Integer> idList = new ArrayList<>();
        for (JsonElement e : array) {
            MyShip myShip = gson.fromJson(e, MyShip.class);
            if (!e.getAsJsonObject()
                 .has("api_sally_area")) {
                myShip.setSallyArea(-1);
            }
            idList.add(myShip.getId());
            MyShipManager.INSTANCE.put(myShip.getId(), myShip);
        }
        MyShipManager.INSTANCE.delete(idList);
    }

    private void apiBasic(JsonObject obj) {
        Basic.INSTANCE.setLevel(obj.get("api_level")
                                   .getAsInt());
    }
}
