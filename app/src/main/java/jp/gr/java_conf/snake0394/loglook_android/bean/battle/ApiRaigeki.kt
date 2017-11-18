package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/05.
 */
data class ApiRaigeki(
        /**
         * 味方被ダメージ
         */
        val apiFdam: List<Int>,

        /**
         * 敵被ダメージ
         */
        val apiEdam: List<Int>
)