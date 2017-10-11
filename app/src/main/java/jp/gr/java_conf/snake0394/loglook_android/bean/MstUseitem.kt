package jp.gr.java_conf.snake0394.loglook_android.bean

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by snake0394 on 2016/08/08.
 */
@RealmClass
open class MstUseitem(
        /**アイテムID */
        @PrimaryKey
        @SerializedName("api_id")
        open var id: Int = -1,

        /**アイテム名 */
        @SerializedName("api_name")
        open var name: String = ""
) : RealmObject()
