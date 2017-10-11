package jp.gr.java_conf.snake0394.loglook_android.bean

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmInt

/**
 * Created by snake0394 on 2016/08/07.
 */
@RealmClass
open class MstMission(
        /**
         * 遠征ID
         */
        @PrimaryKey
        @SerializedName("api_id")
        open var id: Int = -1,

        /**
         * 海域カテゴリID
         */
        @SerializedName("api_maparea_id")
        open var mapareaId: Int = -1,

        /**
         * 遠征名
         */
        @SerializedName("api_name")
        open var name: String = "",

        /**
         * 遠征時間(分)
         */
        @SerializedName("api_time")
        open var time: Int = -1,

        /**
         * 獲得アイテム1。[0]=アイテムID、[1]=入手個数
         */
        @SerializedName("api_win_item1")
        open var winItem1: RealmList<RealmInt> = RealmList(),

        /**
         * 獲得アイテム2。[0]=アイテムID、[1]=入手個数
         */
        @SerializedName("api_win_item2")
        open var winItem2: RealmList<RealmInt> = RealmList()
) : RealmObject()
