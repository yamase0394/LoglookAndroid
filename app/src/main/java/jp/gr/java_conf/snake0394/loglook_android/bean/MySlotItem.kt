package jp.gr.java_conf.snake0394.loglook_android.bean

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * 所有する装備のデータ
 */
@RealmClass
open class MySlotItem(
        /**
         * 装備固有ID
         */
        @PrimaryKey
        @SerializedName("api_id")
        open var id: Int = -1,

        /**
         * 装備ID
         */
        @SerializedName("api_slotitem_id")
        open var mstId: Int = -1,

        /**
         * 装備ロック 1=ロック、0=ロックされていない
         */
        @SerializedName("api_locked")
        open var locked: Int = -1,

        /**
         * 改修度。 0=未改修、1～10改修度
         */
        @SerializedName("api_level")
        open var level: Int = 0,

        /**
         * 熟練度。熟練度がついていない場合元データは存在しない。0=熟練度なし。1～7=熟練度。
         */
        @SerializedName("api_alv")
        open var alv: Int = 0
) : RealmObject()