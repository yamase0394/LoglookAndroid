package jp.gr.java_conf.snake0394.loglook_android.api;

import android.content.Intent;

import com.google.gson.JsonObject;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.HeavilyDamagedWarningService;
import jp.gr.java_conf.snake0394.loglook_android.WinRankOverlayService;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.IBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.PhaseState;
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_sortie/battleresult")
public class ApiReqSortieBattleresult implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        App.getInstance().stopService(new Intent(App.getInstance().getApplicationContext(), WinRankOverlayService.class));

        List<PhaseState> phaseStates = TacticalSituation.INSTANCE.getPhaseList();
        IBattle battle = TacticalSituation.INSTANCE.getBattle();
        for (int i = 0; i < phaseStates.get(0).getFHp().size(); i++) {
            int lastHp = phaseStates.get(phaseStates.size() - 1).getFHp().get(i);
            int maxHp = battle.getApiMaxhps().get(i + 1);
            if (lastHp <= maxHp / 4) {
                App.getInstance().startService(new Intent(App.getInstance().getApplicationContext(), HeavilyDamagedWarningService.class));
                return;
            }
        }
        App.getInstance().stopService(new Intent(App.getInstance().getApplicationContext(), HeavilyDamagedWarningService.class));
    }
}
