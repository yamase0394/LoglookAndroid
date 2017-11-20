package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/05.
 */
interface IEnemyCombinedBattle : IBattle {
    /**
     * 敵随伴艦隊艦船ID
     */
    val apiShipKeCombined: List<Int>
    /**
     * 敵随伴艦隊Lv
     */
    val apiShipLvCombined: List<Int>

    val apiENowhpsCombined: List<Int>
    val apiEMaxhpsCombined: List<Int>
}