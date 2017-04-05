package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Intent;

import com.google.gson.JsonObject;

import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ScreenCaptureActivity;

public class HeavyDamagedWarningService implements APIListenerSpi {
    
    private static final String TAG = "HeavyDamagedWarningService";
    
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        switch (req.getRequestURI()) {
            case "/kcsapi/api_req_sortie/battleresult":
                /*
                TacticalSituation ts = TacticalSituation.INSTANCE;
                for (int i = 0; i < ts.getFriendShipId()
                                      .size(); i++) {
                    if (ts.getFriendNowhps()
                          .get(i) <= ts.getFriendMaxhps()
                                       .get(i) / 4) {
                        Intent intent = new Intent(App.getInstance(), ScreenCaptureActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("class", TemplateMatchingService.class.getSimpleName());
                        App.getInstance().startActivity(intent);
                        break;
                    }
                }
                break;
                */
                Intent intent = new Intent(App.getInstance(), ScreenCaptureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("class", TemplateMatchingService.class.getSimpleName());
                App.getInstance().startActivity(intent);
                break;
            
            default:
                App.getInstance().stopService(new Intent(App.getInstance(), TemplateMatchingService.class));
            
        }
    }
}
