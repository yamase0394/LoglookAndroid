package jp.gr.java_conf.snake0394.loglook_android.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmUtils

/**
 * Created by snake0394 on 2017/02/16.
 */
@API("/kcsapi/api_get_member/require_info")
class ApiGetMemberRequireInfo : APIListenerSpi {

    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {

        val data = json.getAsJsonObject("api_data")

        this.apiSlotItem(data.getAsJsonArray("api_slot_item"))
    }

    private fun apiSlotItem(array: JsonArray) {
        val gson = RealmUtils.getGsonInstance()
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                val idList = arrayListOf<Int>()
                array.forEach {
                    val mySlotitem = gson.fromJson(it, MySlotItem::class.java)
                    idList.add(mySlotitem.id)
                    realm.insertOrUpdate(mySlotitem)
                }

                realm.where(MySlotItem::class.java).not().`in`("id", idList.toTypedArray()).findAll().deleteAllFromRealm()
            }
        }
    }
}
