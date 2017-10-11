package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.EquipType2;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.logger.CreateItemLogger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_kousyou/createitem")
public class ApiReqKousyouCreateitem implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        int fuel = Integer.parseInt(req.getParameterMap().get("api_item1").get(0));
        int bullet = Integer.parseInt(req.getParameterMap().get("api_item2").get(0));
        int steel = Integer.parseInt(req.getParameterMap().get("api_item3").get(0));
        int bauxite = Integer.parseInt(req.getParameterMap().get("api_item4").get(0));

        CreateItemLogger.INSTANCE.ready(fuel, bullet, steel, bauxite);

        JsonObject data = json.getAsJsonObject("api_data");
        int createFlag = data.get("api_create_flag")
                             .getAsInt();

        if (createFlag == 0) {
            CreateItemLogger.INSTANCE.write(createFlag, null, null);
            return;
        }

        int equipTypeId = data.get("api_type3")
                              .getAsInt();
        EquipType2 equipType2 = EquipType2.toEquipType2(equipTypeId);

        JsonObject apiSlotItem = data.get("api_slot_item").getAsJsonObject();
        int mstSlotItemId = apiSlotItem.get("api_slotitem_id").getAsInt();

        try(Realm realm = Realm.getDefaultInstance()) {
            MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mstSlotItemId).findFirst();
            CreateItemLogger.INSTANCE.write(createFlag, mstSlotitem, equipType2);
        }
    }
}
