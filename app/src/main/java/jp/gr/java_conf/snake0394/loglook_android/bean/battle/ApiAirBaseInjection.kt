package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class ApiAirBaseInjection(
        val apiStage1: ApiStage1?,
        val apiStage2: ApiAirBaseAttack.ApiStage2?,
        val apiStage3: ApiAirBaseAttack.ApiStage3?,
        val apiStage3Combined: ApiAirBaseAttack.ApiStage3Combined?
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
}