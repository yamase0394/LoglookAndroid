package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.MissionTimer;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_mission/start")
public class ApiReqMissionStart implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        String requestBody;
        try {
            requestBody = IOUtils.toString(req.getRequestBody()
                                              .get(), "UTF-8");
            String regex = "Fdeck%5Fid=(\\d+)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(requestBody);
            m.find();
            int deckId = Integer.parseInt(m.group(1));

            regex = "Fmission%5Fid=(\\d+)";
            p = Pattern.compile(regex);
            m = p.matcher(requestBody);
            m.find();
            int missionId = Integer.parseInt(m.group(1));
            MissionTimer.INSTANCE.ready(deckId, missionId);


            JsonObject data = json.getAsJsonObject("api_data");
            MissionTimer.INSTANCE.startTimer(App.getInstance(), data.get("api_complatetime").getAsLong());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
