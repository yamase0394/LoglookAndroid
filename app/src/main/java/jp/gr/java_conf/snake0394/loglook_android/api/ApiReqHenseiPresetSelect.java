package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.DeckUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_hensei/preset_select")
public class ApiReqHenseiPresetSelect implements APIListenerSpi{

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getAsJsonObject("api_data");

        Deck deck = DeckManager.INSTANCE.getDeck(data.getAsJsonPrimitive("api_id")
                                                     .getAsInt());

        Type listType = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> shipIdList = new Gson().fromJson(data.get("api_ship"), listType);

        deck.setShipId(shipIdList);
        deck.setLevelSum(DeckUtility.INSTANCE.getLevelSum(deck));
        deck.setCondRecoveryTime(DeckUtility.INSTANCE.getCondRecoveryTime(deck));
    }
}
