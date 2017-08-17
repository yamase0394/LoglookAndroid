package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class ApiInjectionKouku(
        val apiStage1: ApiStage1?,
        val apiStage2: ApiStage2?,
        val apiStage3: ApiKouku.ApiStage3?,
        val apiStage3Combined:ApiKouku.ApiStage3Combined?
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
            val apiELostcount: Int
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
            val apiELostcount: Int
    )
}