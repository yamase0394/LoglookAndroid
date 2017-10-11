package jp.gr.java_conf.snake0394.loglook_android.api

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.*
import jp.gr.java_conf.snake0394.loglook_android.bean.*
import jp.gr.java_conf.snake0394.loglook_android.logger.MaterialLogger
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmUtils
import java.util.*

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_port/port")
class ApiPortPort : APIListenerSpi {
    private var gson: Gson? = null

    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {
        //WinRankOverlayを消す
        App.getInstance().stopService(Intent(App.getInstance().getApplicationContext(), WinRankOverlayService::class.java))
        App.getInstance().stopService(Intent(App.getInstance().applicationContext, HeavilyDamagedWarningService::class.java))

        gson = RealmUtils.getGsonInstance()

        val data = json.getAsJsonObject("api_data")

        //api_shipが先でないとapi_deck_portでnullが発生する
        this.apiShip(data.getAsJsonArray("api_ship"))
        this.apiMaterial(data.getAsJsonArray("api_material"))
        this.apiDeckPort(data.getAsJsonArray("api_deck_port"))
        this.apiNdock(data.getAsJsonArray("api_ndock"))
        this.apiBasic(data.getAsJsonObject("api_basic"))

        MissionTimer.INSTANCE.clearNotifications()
        Escape.INSTANCE.close()
        gson = null
    }

    private fun apiMaterial(array: JsonArray) {

        val materialList = array.mapTo(ArrayList<Int>()) { it.asJsonObject.get("api_value").asInt }

        //[4]高速建造剤と[5]高速修復材を入れ替える
        val temp = materialList[4]
        materialList[4] = materialList[5]
        materialList[5] = temp
        val material = Material()
        material.materialList = materialList

        MaterialLogger.INSTANCE.writeLog(material)
    }

    private fun apiDeckPort(array: JsonArray) {
        for (e in array) {
            val deck = this.gson!!.fromJson(e, Deck::class.java)
            deck.levelSum = DeckUtility.getLevelSum(deck)
            deck.condRecoveryTime = DeckUtility.getCondRecoveryTime(deck)

            DeckManager.INSTANCE.put(deck)

            //遠征タイマーを制御
            val mission = DeckManager.INSTANCE.getDeck(deck.id).mission

            //艦隊が遠征中か強制帰投中
            if (mission[0].toInt() == 1 || mission[0].toInt() == 3) {
                //指定された艦隊に対するタイマーが作動中でない
                if (!MissionTimer.INSTANCE.isRunning(deck.id)) {
                    //遠征タイマーをセット
                    MissionTimer.INSTANCE.ready(deck.id, mission[1].toInt())
                    MissionTimer.INSTANCE.startTimer(App.getInstance(), mission[2])
                }
                //未出撃
            } else if (mission[0].toInt() == 0) {
                //指定された艦隊に対するタイマーが作動中
                if (MissionTimer.INSTANCE.isRunning(deck.id)) {
                    MissionTimer.INSTANCE.cancel(deck.id)
                }
            }
        }
    }

    private fun apiNdock(array: JsonArray) {
        for (e in array) {
            val obj = e.asJsonObject
            val dockId = obj.get("api_id").asInt
            val dockingShipId = obj.get("api_ship_id").asInt
            if (dockingShipId != 0 && !DockTimer.INSTANCE.isRunning(dockId)) {
                val completeTime = obj.get("api_complete_time").asLong
                DockTimer.INSTANCE.startTimer(App.getInstance(), dockId, dockingShipId, completeTime)
            }
        }
    }

    private fun apiShip(array: JsonArray) {
        val idList = ArrayList<Int>()
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                for (e in array) {
                    val myShip = gson!!.fromJson(e, MyShip::class.java)
                    idList.add(myShip.id)
                    //MyShipManager.INSTANCE.put(myShip.getId(), myShip);
                    realm.insertOrUpdate(myShip)
                }
                //MyShipManager.INSTANCE.delete(idList);
                //retain all
                realm.where(MyShip::class.java).not().`in`("id", idList.toTypedArray()).findAll().deleteAllFromRealm()
            }
        }
    }

    private fun apiBasic(obj: JsonObject) {
        Basic.INSTANCE.level = obj.get("api_level").asInt
    }
}
