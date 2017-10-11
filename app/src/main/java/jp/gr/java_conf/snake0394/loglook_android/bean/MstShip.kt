package jp.gr.java_conf.snake0394.loglook_android.bean

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * 艦娘の基本データを持つクラス
 */
@RealmClass
open class MstShip(
        /**
         * 艦娘固有id
         * api_id
         */
        @PrimaryKey
        @SerializedName("api_id")
        open var id: Int = -1,

        /**
         * 名前
         * api_name
         */
        @SerializedName("api_name")
        open var name: String = "",

        /**
         * ふりがな(深海棲艦の場合はflagship、elite）
         * api_yomi
         */
        @SerializedName("api_yomi")
        open var yomi: String = "",

        /**api_stype 艦種 */
        @SerializedName("api_stype")
        open var stype: Int = 0,

        /**
         * 最大搭載燃料
         * api_fuel_max
         */
        @SerializedName("api_fuel_max")
        open var fuelMax: Int = 0,

        /**
         * 最大搭載弾薬
         * api_bull_max
         */
        @SerializedName("api_bull_max")
        open var bullMax: Int = 0
) : RealmObject()
