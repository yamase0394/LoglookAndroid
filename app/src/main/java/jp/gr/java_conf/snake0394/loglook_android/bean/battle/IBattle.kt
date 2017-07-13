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
     * 敵艦船ID -1から 空きは-1
     */
    val apiShipKe: List<Int>

    /**
     * 敵艦船Lv -1から
     */
    val apiShipLv: List<Int>

    /**
     * 味方/敵艦船の現在HP -1から
     */
    val apiNowhps: List<Int>
    /**
     * 味方/敵艦船の現在HP -1から
     */

    val apiMaxhps: List<Int>
}
