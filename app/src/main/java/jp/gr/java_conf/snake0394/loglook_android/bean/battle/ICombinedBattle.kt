package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/05.
 */
interface ICombinedBattle : IBattle {
    val apiNowhpsCombined: List<Int>
    val apiMaxhpsCombined: List<Int>
}