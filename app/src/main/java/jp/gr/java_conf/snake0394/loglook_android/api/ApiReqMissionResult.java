package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.bean.MissionResult;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem;
import jp.gr.java_conf.snake0394.loglook_android.logger.MissionLogger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_mission/result")
public class ApiReqMissionResult implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getAsJsonObject("api_data");

        MissionResult mr;
        try(Realm realm = Realm.getDefaultInstance()) {
            mr = new Gson().fromJson(data, MissionResult.class);
            MstMission mstMission = realm.where(MstMission.class).equalTo("name", mr.getQuestName()).findFirst();
            if (mr.getUseitemFlag().get(0) != 0 && mr.getUseitemFlag().get(0) != 4) {
                mr.setUseitemId1(mstMission.getWinItem1().get(0).getValue());
                MstUseitem mstUseitem = realm.where(MstUseitem.class).equalTo("id", mr.getUseitemId1()).findFirst();
                mr.setUseitemName1(mstUseitem.getName());
            }
            if (mr.getUseitemFlag().get(1) != 0 && mr.getUseitemFlag().get(1) != 4) {
                mr.setUseitemId2(mstMission.getWinItem2().get(0).getValue());
                MstUseitem mstUseitem = realm.where(MstUseitem.class).equalTo("id", mr.getUseitemId2()).findFirst();
                mr.setUseitemName2(mstUseitem.getName());
            }
        } catch (JsonSyntaxException e) {
            //api_get_materialが配列でない場合
            Gson gson = new Gson();
            mr = new MissionResult();
            mr.setShipId((List<Integer>) gson.fromJson(data.get("api_ship_id"), new TypeToken<List<Integer>>() {
            }.getType()));
            mr.setClearResult(data.get("api_clear_result")
                                  .getAsInt());
            mr.setMapareaName(data.get("api_maparea_name")
                                  .getAsString());
            mr.setQuestName(data.get("api_quest_name")
                                .getAsString());
            mr.setGainMaterial(Arrays.asList(0, 0, 0, 0));
        }

        JsonArray exp = data.getAsJsonArray("api_get_ship_exp");
        int expSum = 0;
        for (JsonElement e : exp) {
            expSum += e.getAsInt();
        }
        mr.setExpSum(expSum);

        MissionLogger.INSTANCE.writeLog(mr);
    }
}
