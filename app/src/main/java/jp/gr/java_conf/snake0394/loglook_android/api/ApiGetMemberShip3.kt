package jp.gr.java_conf.snake0394.loglook_android.api

import com.google.gson.JsonObject
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmUtils

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_get_member/ship3")
class ApiGetMemberShip3 : APIListenerSpi {
    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {

        val data = json.getAsJsonObject("api_data")
        val apiShipData = data.getAsJsonArray("api_ship_data")
        val gson = RealmUtils.getGsonInstance()

        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                for (e in apiShipData) {
                    val myShip = gson.fromJson(e, MyShip::class.java)
                    if (!e.asJsonObject.has("api_sally_area")) {
                        myShip.sallyArea = -1
                    }
                    //MyShipManager.INSTANCE.put(myShip.getId(), myShip);
                    realm.insertOrUpdate(myShip)
                }
            }
        }

    }
}
