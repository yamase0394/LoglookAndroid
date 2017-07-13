package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/05.
 */
data class ApiKouku(
        val apiStage1: ApiStage1?,
        val apiStage2: ApiStage2?,
        val apiStage3: ApiStage3?,
        val apiStage3Combined:ApiStage3Combined?
) {
    /**
     * 航空戦1 空対空戦闘
     */
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
            val apiELostcount: Int,

            /**
             * 制空権表示 0=制空均衡, 1=制空権確保, 2=航空優勢, 3=航空劣勢, 4=制空権喪失
             */
            val apiDispSeiku: Int,

            /**
             * 触接装備ID [0]=味方, [1]=敵
             */
            val apiTouchPlane: MutableList<Int>
    )

    /**
     * 航空戦2 対空砲火
     */
    data class ApiStage2(
            /**
             * 味方艦載機数(艦攻/艦爆数)
             */
            val apiFCount: Int,

            /**
             * 味方艦載機の撃墜機数(艦攻/艦爆数)
             */
            val apiFLostcount: Int,

            /**
             * 敵艦載機数(艦攻/艦爆数)
             */
            val apiECount: Int,

            /**
             * 敵艦載機の撃墜機数(艦攻/艦爆数)
             */
            val apiELostcount: Int,

            val apiAirFire: ApiAirFire
    ) {
        /**
         * 対空カットイン
         */
        data class ApiAirFire(
                /**
                 * 発動艦のインデックス 0から始まる
                 */
                val apiIdx: Int,

                /**
                 * カットイン種別
                 * 1：高角砲x2/電探
                 * 2：高角砲/電探
                 * 3：高角砲x2
                 * 4：大口径主砲/三式弾/高射装置/電探
                 * 5：高角砲+高射装置x2/電探
                 * 6：大口径主砲/三式弾/高射装置
                 * 7：高角砲/高射装置/電探
                 * 8：高角砲+高射装置/電探
                 * 9：高角砲/高射装置
                 * 10：高角砲/集中機銃/電探
                 * 11：高角砲/集中機銃
                 * 12：集中機銃/機銃/電探
                 * 14：高角砲/対空機銃/電探
                 * 15：高角砲/対空機銃
                 * 16：高角砲/対空機銃/電探
                 * 17：高角砲/対空機銃
                 * 18：集中機銃
                 */
                val apiKind: Int
        )
    }

    /**
     * 航空攻撃
     */
    data class ApiStage3(
            /**
             * 味方被ダメージ かばわれると+0.1 -1から
             */
            val apiFdam: List<Int>,

            /**
             * 敵被ダメージ -1から
             */
            val apiEdam: List<Int>
    )

    /**
     * 航空攻撃
     */
    data class ApiStage3Combined(
            /**
             * 味方被ダメージ かばわれると+0.1 -1から
             */
            val apiFdam: List<Int>,

            /**
             * 敵被ダメージ -1から
             */
            val apiEdam: List<Int>?
    )
}
