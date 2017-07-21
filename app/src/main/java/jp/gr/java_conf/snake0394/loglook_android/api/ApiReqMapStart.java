package jp.gr.java_conf.snake0394.loglook_android.api;

import android.content.Intent;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.HeavilyDamagedWarningService;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.HeavilyDamagedWarningActivity;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_map/start")
public class ApiReqMapStart implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getAsJsonObject("api_data");
        TacticalSituation.INSTANCE.setBoss(data.get("api_event_id")
                .getAsInt() == 5);
        Logger.d(ApiReqMapNext.class.getName(), "isBoss=" + TacticalSituation.INSTANCE.isBoss());


        GeneralPrefs prefs = new GeneralPrefs(App.getInstance());
        if (!prefs.getShowsHeavilyDamagedOverlay() && !prefs.getShowsHeavilyDamagedWarningWindow()) {
            return;
        }

        int deckId = Integer.parseInt(req.getParameterMap()
                .get("api_deck_id")
                .get(0));
        Deck deck = DeckManager.INSTANCE.getDeck(deckId);
        List<Integer> shipIdList = deck.getShipId();
        ArrayList<Integer> heavyDamaged = new ArrayList<>();
        for (int i = 0; i < shipIdList.size(); i++) {
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipIdList.get(i));
            if (shipIdList.get(i) != -1 && myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                heavyDamaged.add(myShip.getId());
            }
        }

        if (!heavyDamaged.isEmpty()) {
            if(prefs.getShowsHeavilyDamagedWarningWindow()) {
                //大破進撃警告画面を表示
                Intent intent = new Intent(App.getInstance(), HeavilyDamagedWarningActivity.class);
                intent.putIntegerArrayListExtra("shipId", heavyDamaged);
                intent.putExtra("first", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                App.getInstance()
                        .startActivity(intent);
            } else if(prefs.getShowsHeavilyDamagedOverlay()){
                App.getInstance().startService(new Intent(App.getInstance().getApplicationContext(), HeavilyDamagedWarningService.class));
            }
        }
    }
}
