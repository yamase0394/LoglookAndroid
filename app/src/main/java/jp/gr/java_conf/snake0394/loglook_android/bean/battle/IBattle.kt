package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2016/09/07.
 */
interface IBattle {
    /**
     * 出撃自艦隊ID
     */
    val apiDeckId: Int

    /**
     * 敵艦船ID -1からでない 空きはない
     */
    val apiShipKe: List<Int>

    /**
     * 敵艦船Lv からでない
     */
    val apiShipLv: List<Int>

    /**
     * 味方現在HP
     */
    val apiFNowhps: List<Int>

    /**
     * 味方最大HP
     */
    val apiFMaxhps: List<Int>

    /**
     * 味方現在HP
     */
    val apiENowhps: List<Int>

    /**
     * 味方最大HP
     */
    val apiEMaxhps: List<Int>
}
