package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/05.
 */
interface IEachCombinedBattle : IBattle {
    /**
     * 敵随伴艦隊艦船ID [7] -1から
     */
    val apiShipKeCombined: List<Int>
    /**
     * 敵随伴艦隊Lv [7] -1から
     */
    val apiShipLvCombined: List<Int>
    /**
     * 味方/敵随伴艦隊現在HP [13] (-1から始まる)
     */
    val apiNowhpsCombined: List<Int>
    /**
     * 味方/敵随伴艦隊最大HP [13] (-1から始まる)
     */
    val apiMaxhpsCombined: List<Int>
}