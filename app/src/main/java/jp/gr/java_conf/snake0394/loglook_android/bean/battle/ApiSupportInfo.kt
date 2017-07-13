package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/06.
 */
data class ApiSupportInfo(
        /**
         * 空撃
         * スペルミス atack
         */
        val apiSupportAiratack: ApiSupportAirattack,
        val apiSupportHourai: ApiSupportHourai
) {
    data class ApiSupportAirattack(
            val apiStageFlag: List<Int>,
            val apiStage1: ApiStage1,
            val apiStage2: ApiStage2,
            val apiStage3: ApiStage3
    ) {
        data class ApiStage1(
                /**
                 * 味方艦載機数
                 */
                val apiFCount: Int,

                /**
                 * 味方艦載機の撃墜機数
                 */
                val apiFLostcount: Int,

                /**
                 * 敵艦載機数
                 */
                val apiECount: Int,

                /**
                 * 敵艦載機の撃墜機数
                 */
                val apiELostcount: Int
                )

        data class ApiStage2(
                /**
                 * 味方艦載機数(艦攻/艦爆数)
                 */
                val apiFCount: Int,

                /**
                 * 味方艦載機の撃墜機数(艦攻/艦爆数)
                 */
                val apiFLostcount: Int
        )

        data class ApiStage3(
                /**
                 * 敵被ダメージ -1から
                 */
                val apiEdam: List<Int>
        )
    }

    /**
     * 砲撃または雷撃
     */
    data class ApiSupportHourai(
            /**
             * 敵被ダメージ -1から
             */
            val apiDamage:List<Int>
    )
}