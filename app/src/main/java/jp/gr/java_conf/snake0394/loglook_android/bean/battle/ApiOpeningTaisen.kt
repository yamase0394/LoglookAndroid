package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class ApiOpeningTaisen(
        /**
         * 攻撃を受ける艦の位置
         * 値が1~6は味方が7~12は敵が攻撃を受ける
         * 謎の-1はない
         */
        val apiDfList: List<List<Int>>,

        /**
         * 謎の-1はない
         */
        val apiDamage: List<List<Int>>
)