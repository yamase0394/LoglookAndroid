package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.EquipType2;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
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

        String requestBody;
        try {
            requestBody = IOUtils.toString(req.getRequestBody()
                                              .get(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String regex = "item1=(\\d+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(requestBody);
        m.find();
        int fuel = Integer.parseInt(m.group(1));

        regex = "item2=(\\d+)";
        p = Pattern.compile(regex);
        m = p.matcher(requestBody);
        m.find();
        int bullet = Integer.parseInt(m.group(1));

        regex = "item3=(\\d+)";
        p = Pattern.compile(regex);
        m = p.matcher(requestBody);
        m.find();
        int steel = Integer.parseInt(m.group(1));

        regex = "item4=(\\d+)";
        p = Pattern.compile(regex);
        m = p.matcher(requestBody);
        m.find();
        int bauxite = Integer.parseInt(m.group(1));

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

        JsonObject apiSlotItem = data.get("api_slot_item")
                                     .getAsJsonObject();
        int mstSlotItemId = apiSlotItem.get("api_slotitem_id")
                                       .getAsInt();
        MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mstSlotItemId);

        CreateItemLogger.INSTANCE.write(createFlag, mstSlotitem, equipType2);
    }
}
