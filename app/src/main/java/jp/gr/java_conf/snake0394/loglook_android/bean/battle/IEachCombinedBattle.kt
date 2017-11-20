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

    val apiFNowhpsCombined: List<Int>
    val apiFMaxhpsCombined: List<Int>
    val apiENowhpsCombined: List<Int>
    val apiEMaxhpsCombined: List<Int>
}