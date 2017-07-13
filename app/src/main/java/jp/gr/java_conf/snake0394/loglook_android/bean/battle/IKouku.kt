package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/05.
 */
interface IKouku {
    /**
     * size=3 index=1のときindex+1のStageがnullになる
     */
    val apiStageFlag:List<Int>
    val apiKouku:ApiKouku
}