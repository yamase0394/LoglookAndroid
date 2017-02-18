package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.bean.Kdock;
import jp.gr.java_conf.snake0394.loglook_android.logger.CreateShipLogger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_get_member/kdock")
public class ApiGetMemberKdock implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        CreateShipLogger logger = CreateShipLogger.INSTANCE;
        if (!logger.isReady()) {
            return;
        }

        JsonArray data = json.getAsJsonArray("api_data");

        JsonElement e = data.get(logger.getKdockId() - 1);
        Kdock kdock = new Gson().fromJson(e, Kdock.class);

        //空きドック数
        int emptyDockNum = 0;
        for(JsonElement element : data) {
            if(element.getAsJsonObject().get("api_state").getAsInt() == 0){
                emptyDockNum++;
            }
        }
        
        logger.write(kdock, emptyDockNum);
    }
}
