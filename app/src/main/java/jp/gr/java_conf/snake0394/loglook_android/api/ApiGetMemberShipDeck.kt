package jp.gr.java_conf.snake0394.loglook_android.api

import android.content.Intent
import com.google.gson.JsonObject
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.App
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmUtils
import jp.gr.java_conf.snake0394.loglook_android.view.activity.HeavilyDamagedWarningActivity
import java.util.*

/**
 * api_req_map/nextの直前
 */
@API("/kcsapi/api_get_member/ship_deck")
class ApiGetMemberShipDeck : APIListenerSpi {
    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {

        val data = json.getAsJsonObject("api_data")
        val apiShipData = data.getAsJsonArray("api_ship_data")
        val gson = RealmUtils.getGsonInstance()
        val heavyDamaged = ArrayList<Int>()

        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                for (e in apiShipData) {
                    val myShip = gson.fromJson(e, MyShip::class.java)

                    realm.insertOrUpdate(myShip)

                    if (myShip.nowhp <= myShip.maxhp / 4) {
                        /*
                        退避している場合は警告を出さない
                        if (Escape.INSTANCE.isEscaped(shipObj[i].getInt("api_id"))) {
                            continue;
                        }
                        */
                        heavyDamaged.add(myShip.id)
                    }
                }
            }
        }

        if (!heavyDamaged.isEmpty()) {
            if (GeneralPrefs(App.getInstance()).showsHeavilyDamagedWarningWindow) {
                //大破進撃警告画面を表示
                val intent = Intent(App.getInstance(), HeavilyDamagedWarningActivity::class.java)
                intent.putIntegerArrayListExtra("shipId", heavyDamaged)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                App.getInstance().startActivity(intent)
            }
        }
    }
}
