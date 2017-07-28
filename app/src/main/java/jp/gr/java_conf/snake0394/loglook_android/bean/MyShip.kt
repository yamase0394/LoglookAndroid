package jp.gr.java_conf.snake0394.loglook_android.bean

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmInt

/**
 * 所有艦船データ
 */
@RealmClass
open class MyShip(
        /**
         * @return 艦娘ID
         */
        @PrimaryKey
        @SerializedName("api_id")
        open var id: Int = -1,

        /**
         * 並び替え順？
         */
        @SerializedName("api_sortno")
        open var sortno: Int = -1,

        /**
         * 艦娘マスターID
         */
        @SerializedName("api_ship_id")
        open var shipId: Int = -1,

        /**
         * レベル
         */
        @SerializedName("api_lv")
        open var lv: Int = -1,

        /**
         * [0]=累計経験値、[1]=次のレベルまでの経験値、[2]=経験値バーの割合
         */
        @SerializedName("api_exp")
        open var exp: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 現在のhp
         */
        @SerializedName("api_nowhp")
        open var nowhp: Int = -1,

        /**
         * 最大hp
         */
        @SerializedName("api_maxhp")
        open var maxhp: Int = -1,

        /**
         * @return 射程
         */
        @SerializedName("api_leng")
        open var leng: Int = -1,

        /**
         * 装備スロット。装備idが各スロットの位置に入る。空きの場合は-1が入る。
         */
        @SerializedName("api_slot")
        open var slot: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 艦載機搭載数
         *
         * @return 各スロットの搭載数
         */
        @SerializedName("api_onslot")
        open var onslot: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 補強増設
         */
        @SerializedName("api_slot_ex")
        open var slotEx: Int = -1,

        /**
         * 近代化改修状態。[0]=火力、[1]=雷装、[2]=対空、[3]=装甲、[4]=運。
         */
        @SerializedName("api_kyouka")
        open var kyouka: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * レアリティ
         */
        @SerializedName("api_backs")
        open var backs: Int = -1,

        /**
         * 搭載燃料
         */
        @SerializedName("api_fuel")
        open var fuel: Int = -1,

        /**
         * 搭載弾薬
         */
        @SerializedName("api_bull")
        open var bull: Int = -1,

        /**
         * 装備可能スロット数
         */
        @SerializedName("api_slotnum")
        open var slotnum: Int = -1,

        /**
         * 入渠時間（ミリ秒）
         */
        @SerializedName("api_ndock_time")
        open var ndockTime: Long = -1,

        /**
         * 入渠時の消費資材量。[0]=燃料、[1]=鋼材
         */
        @SerializedName("api_ndock_item")
        open var ndockItem: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 改装☆？
         */
        @SerializedName("api_srate")
        open var srate: Int = -1,

        /**
         * コンディション
         */
        @SerializedName("api_cond")
        open var cond: Int = -1,

        /**
         * 火力。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_karyoku")
        open var karyoku: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 雷装。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_raisou")
        open var raisou: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 対空。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_taiku")
        open var taiku: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 装甲。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_soukou")
        open var soukou: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 回避。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_kaihi")
        open var kaihi: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 対潜。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_taisen")
        open var taisen: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 索敵。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_sakuteki")
        open var sakuteki: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * 運。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
         */
        @SerializedName("api_lucky")
        open var lucky: RealmList<RealmInt> = RealmList<RealmInt>(),

        /**
         * ロックされているか
         */
        @SerializedName("api_locked")
        open var locked: Int = -1,

        /**
         * ロックされている装備を装備しているか
         */
        @SerializedName("api_locked_equip")
        open var lockedEquip: Int = -1,

        /**
         * 札。札がついていない場合は-1が入る
         */
        @SerializedName("api_sally_area")
        open var sallyArea: Int = -1
) : RealmObject()