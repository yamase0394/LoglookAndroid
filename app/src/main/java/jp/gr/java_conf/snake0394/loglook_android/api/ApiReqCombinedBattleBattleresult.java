package jp.gr.java_conf.snake0394.loglook_android.api;

import android.content.Intent;

import com.google.gson.JsonObject;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.HeavilyDamagedWarningService;
import jp.gr.java_conf.snake0394.loglook_android.WinRankOverlayService;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.IBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ICombinedBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.IEachCombinedBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.PhaseState;
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_combined_battle/battleresult")
public class ApiReqCombinedBattleBattleresult implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getAsJsonObject("api_data");

        if (!data.get("api_escape").isJsonNull()) {
            JsonObject apiEscape = data.getAsJsonObject("api_escape");
            int damaged = apiEscape.getAsJsonArray("api_escape_idx")
                    .get(0)
                    .getAsInt();
            int towing = apiEscape.getAsJsonArray("api_tow_idx")
                    .get(0)
                    .getAsInt();
            if (damaged > 6) {
                damaged = DeckManager.INSTANCE.getDeck(2)
                        .getShipId()
                        .get(damaged - 7);
            } else {
                damaged = DeckManager.INSTANCE.getDeck(1)
                        .getShipId()
                        .get(damaged - 1);
            }
            if (towing > 6) {
                towing = DeckManager.INSTANCE.getDeck(2)
                        .getShipId()
                        .get(towing - 7);
            } else {
                towing = DeckManager.INSTANCE.getDeck(1)
                        .getShipId()
                        .get(towing - 1);
            }
            Escape.INSTANCE.ready(damaged, towing);
        }

        //勝利ランクオーバーレイを消す
        App.getInstance().stopService(new Intent(App.getInstance().getApplicationContext(), WinRankOverlayService.class));

        GeneralPrefs prefs = new GeneralPrefs(App.getInstance().getApplicationContext());
        if (!prefs.getShowsHeavilyDamagedOverlay()) {
            return;
        }

        //大破警告オーバーレイ
        List<PhaseState> phaseStates = TacticalSituation.INSTANCE.getPhaseList();
        IBattle battle = TacticalSituation.INSTANCE.getBattle();
        for (int i = 0; i < phaseStates.get(0)
                .getFHp()
                .size(); i++) {
            int lastHp = phaseStates.get(phaseStates.size() - 1)
                    .getFHp()
                    .get(i);
            int maxHp = battle.getApiMaxhps()
                    .get(i + 1);
            if (lastHp <= maxHp / 4) {
                App.getInstance()
                        .startService(new Intent(App.getInstance()
                                .getApplicationContext(), HeavilyDamagedWarningService.class));
                return;
            }
        }

        for (int i = 0; i < phaseStates.get(0)
                .getFHpCombined()
                .size(); i++) {
            int lastHp = phaseStates.get(phaseStates.size() - 1)
                    .getFHpCombined()
                    .get(i);
            if (battle instanceof ICombinedBattle) {
                int maxHp = ((ICombinedBattle) battle).getApiMaxhpsCombined()
                        .get(i + 1);
                if (lastHp <= maxHp / 4) {
                    App.getInstance()
                            .startService(new Intent(App.getInstance()
                                    .getApplicationContext(), HeavilyDamagedWarningService.class));
                    return;
                }
            } else if (battle instanceof IEachCombinedBattle) {
                int maxHp = ((IEachCombinedBattle) battle).getApiMaxhpsCombined()
                        .get(i + 1);
                if (lastHp <= maxHp / 4) {
                    App.getInstance()
                            .startService(new Intent(App.getInstance()
                                    .getApplicationContext(), HeavilyDamagedWarningService.class));
                    return;
                }
            }
        }

        App.getInstance()
                .stopService(new Intent(App.getInstance()
                        .getApplicationContext(), HeavilyDamagedWarningService.class));
    }
}
