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
 * Created by snake0394 on 2017/02/16.
 */
@API("/kcsapi/api_get_member/require_info")
public class ApiGetMemberRequireInfo implements APIListenerSpi {
    private Gson gson;

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        this.gson = new Gson();

        JsonObject data = json.getAsJsonObject("api_data");

        this.apiSlotItem(data.getAsJsonArray("api_slot_item"));
    }

    private void apiSlotItem(JsonArray array) {
        List<Integer> idList = new ArrayList<>();
        for (JsonElement e : array) {
            MySlotItem temp = this.gson.fromJson(e, MySlotItem.class);
            MySlotItemManager.INSTANCE.put(temp);
            idList.add(temp.getId());
        }
        MySlotItemManager.INSTANCE.retainAll(idList);
        MySlotItemManager.INSTANCE.serialize();
    }
}
