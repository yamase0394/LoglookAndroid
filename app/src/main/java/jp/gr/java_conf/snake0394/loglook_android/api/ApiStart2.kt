package jp.gr.java_conf.snake0394.loglook_android.api

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmUtils

/**
 * Created by snake0394 on 2017/02/16.
 */
@API("/kcsapi/api_start2")
class ApiStart2 : APIListenerSpi {

    private var gson: Gson? = null

    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {
        this.gson = RealmUtils.getGsonInstance()

        val data = json.getAsJsonObject("api_data")

        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                this.apiMstShip(data.getAsJsonArray("api_mst_ship"), realm)
                this.apiMstSlotitem(data.getAsJsonArray("api_mst_slotitem"), realm)
                this.apiMstUseitem(data.getAsJsonArray("api_mst_useitem"), realm)
                this.apiMstMission(data.getAsJsonArray("api_mst_mission"), realm)
            }
        }
        gson = null
    }

    private fun apiMstShip(array: JsonArray, realm: Realm) {
        /*
        for (e in array) {
            MstShipManager.INSTANCE.put(gson!!.fromJson(e, MstShip::class.java))
        }
        //MstShipのnullオブジェクトをput
        val mstShip = MstShip()
        //TODO ヤメロォ
        mstShip.id = -1
        mstShip.name = ""
        mstShip.yomi = ""
        MstShipManager.INSTANCE.put(mstShip)
        //MstShipManagerをシリアライズ
        MstShipManager.INSTANCE.serialize()
        */
        array.forEach { realm.insertOrUpdate(gson!!.fromJson(it, MstShip::class.java)) }
    }

    private fun apiMstSlotitem(array: JsonArray, realm: Realm) {
        for (e in array) {
            realm.insertOrUpdate(gson!!.fromJson(e, MstSlotitem::class.java))
        }
        /*
        val ms = MstSlotitem()
        //TODO ヤメロォ
        ms.id = -1
        ms.name = ""
        MstSlotitemManager.INSTANCE.put(ms)
        //MstSlotMapをシリアライズ
        MstSlotitemManager.INSTANCE.serialize()
        */
    }

    private fun apiMstUseitem(array: JsonArray, realm: Realm) {
        for (e in array) {
            realm.insertOrUpdate(gson!!.fromJson(e, MstUseitem::class.java))
        }
    }

    private fun apiMstMission(array: JsonArray, realm: Realm) {
        for (e in array) {
            realm.insertOrUpdate(gson!!.fromJson(e, MstMission::class.java))
        }
    }
}
