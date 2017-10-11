package jp.gr.java_conf.snake0394.loglook_android.api

import com.google.gson.JsonObject
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmUtils

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_get_member/slot_item")
class ApiGetMemberSlotItem : APIListenerSpi {
    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {
        //App.getInstance().stopService(new Intent(App.getInstance().getApplicationContext(), HeavilyDamagedWarningService.class));

        val data = json.getAsJsonArray("api_data")

        val gson = RealmUtils.getGsonInstance()
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                val idList = arrayListOf<Int>()
                data.forEach {
                    val mySlotitem = gson.fromJson(it, MySlotItem::class.java)
                    idList.add(mySlotitem.id)
                    realm.insertOrUpdate(mySlotitem)
                }

                realm.where(MySlotItem::class.java).not().`in`("id", idList.toTypedArray()).findAll().deleteAllFromRealm()
            }
        }
    }
}
