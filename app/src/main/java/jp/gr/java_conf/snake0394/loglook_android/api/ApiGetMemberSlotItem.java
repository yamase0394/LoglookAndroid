package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_get_member/slot_item")
public class ApiGetMemberSlotItem implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonArray data = json.getAsJsonArray("api_data");

        List<Integer> idList = new ArrayList<>();
        for (JsonElement e : data) {
            MySlotItem temp = new Gson().fromJson(e, MySlotItem.class);
            MySlotItemManager.INSTANCE.put(temp);
            idList.add(temp.getId());
        }
        MySlotItemManager.INSTANCE.delete(idList);
        MySlotItemManager.INSTANCE.serialize();
    }
}
