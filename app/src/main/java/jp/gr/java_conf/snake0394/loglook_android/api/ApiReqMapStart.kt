package jp.gr.java_conf.snake0394.loglook_android.api

import android.content.Intent
import com.google.gson.JsonObject
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.App
import jp.gr.java_conf.snake0394.loglook_android.HeavilyDamagedWarningService
import jp.gr.java_conf.snake0394.loglook_android.TacticalSituation
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs
import jp.gr.java_conf.snake0394.loglook_android.view.activity.HeavilyDamagedWarningActivity
import java.util.*

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_map/start")
class ApiReqMapStart : APIListenerSpi {
    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {

        val data = json.getAsJsonObject("api_data")
        TacticalSituation.isBoss = data.get("api_event_id").asInt == 5
        Logger.d(ApiReqMapNext::class.java.name, "isBoss=" + TacticalSituation.isBoss)

        val prefs = GeneralPrefs(App.getInstance())
        if (!prefs.showsHeavilyDamagedOverlay && !prefs.showsHeavilyDamagedWarningWindow) {
            return
        }

        val heavyDamaged = ArrayList<Int>()
        Realm.getDefaultInstance().use { realm ->
            val deckId = Integer.parseInt(req.parameterMap["api_deck_id"]!![0])
            DeckManager.INSTANCE.getDeck(deckId).shipId
                    .filter { it != -1 }
                    .map { realm.where(MyShip::class.java).equalTo("id", it).findFirst() }
                    .filter { it.nowhp <= it.maxhp / 4 }
                    .forEach { heavyDamaged.add(it.id) }
        }

        if (!heavyDamaged.isEmpty()) {
            if (prefs.showsHeavilyDamagedWarningWindow) {
                //大破進撃警告画面を表示
                val intent = Intent(App.getInstance(), HeavilyDamagedWarningActivity::class.java)
                intent.putIntegerArrayListExtra("shipId", heavyDamaged)
                intent.putExtra("first", true)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                App.getInstance().startActivity(intent)
            } else if (prefs.showsHeavilyDamagedOverlay) {
                App.getInstance().startService(Intent(App.getInstance().applicationContext, HeavilyDamagedWarningService::class.java))
            }
        }
    }
}
