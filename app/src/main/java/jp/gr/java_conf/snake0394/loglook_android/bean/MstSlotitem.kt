package jp.gr.java_conf.snake0394.loglook_android.bean

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmInt

/**
 * 装備品のデータを持つクラス
 */
@RealmClass
open class MstSlotitem(
        /**
         * id
         */
        @PrimaryKey
        @SerializedName("api_id")
        open var id: Int = -1,

        /**
         * 名前
         */
        @SerializedName("api_name")
        open var name: String = "",

        /**
         * 並び替え順
         */
        @SerializedName("api_sortno")
        open var sortno: Int = -1,

        /**
         * 装備タイプ [0]=大分類、[1]=図鑑表示、[2]=カテゴリ、[3]=アイコンID
         */
        @SerializedName("api_type")
        open var type: RealmList<RealmInt> = RealmList(),

        /**
         * 耐久(0)
         */
        @SerializedName("api_taik")
        open var taik: Int = 0,

        /**
         * 装甲
         */
        @SerializedName("api_souk")
        open var souk: Int = 0,

        /**
         * 火力
         */
        @SerializedName("api_houg")
        open var houg: Int = 0,

        /**
         * 雷装
         */
        @SerializedName("api_raig")
        open var raig: Int = 0,

        /**
         * 速力
         */
        @SerializedName("api_soku")
        open var soku: Int = 0,

        /**
         * 爆装
         */
        @SerializedName("api_baku")
        open var baku: Int = 0,

        /**
         * 対空
         */
        @SerializedName("api_tyku")
        open var tyku: Int = 0,

        /**
         * 対潜
         */
        @SerializedName("api_tais")
        open var tais: Int = 0,

        /**
         * ?(0)
         */
        @SerializedName("api_atap")
        open var atap: Int = 0,

        /**
         * 命中
         */
        @SerializedName("api_houm")
        open var houm: Int = 0,

        /**
         * 雷撃命中
         */
        @SerializedName("api_raim")
        open var raim: Int = 0,

        /**
         * 回避
         */
        @SerializedName("api_houk")
        open var houk: Int = 0,

        /**
         * 雷撃回避(0)
         */
        @SerializedName("api_raik")
        open var raik: Int = 0,

        /**
         * 爆撃回避(0)
         */
        @SerializedName("api_bakk")
        open var bakk: Int = 0,

        /**
         * 索敵
         */
        @SerializedName("api_saku")
        open var saku: Int = 0,

        /**
         * 索敵妨害(0)
         */
        @SerializedName("api_sakb")
        open var sakb: Int = 0,

        /**
         * 運(0)
         */
        @SerializedName("api_luck")
        open var luck: Int = 0,

        /**
         * 射程
         */
        @SerializedName("api_leng")
        open var leng: Int = 0,

        /**
         * レアリティ
         */
        @SerializedName("api_rare")
        open var rare: Int = 0,

        /**
         * 廃棄資材
         */
        @SerializedName("api_broken")
        open var broken: RealmList<RealmInt> = RealmList(),

        /**
         * 図鑑情報
         */
        @SerializedName("api_info")
        open var info: String = "",

        /**
         * ?
         */
        @SerializedName("api_usebull")
        open var usebull: String = "",

        /**
         * 航空機コスト
         */
        @SerializedName("api_cost")
        open var cost: Int = 0,

        /**
         * 航続距離
         */
        @SerializedName("api_distance")
        open var distance: Int = 0
) : RealmObject()