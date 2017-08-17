package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class ApiAirBaseAttack(
        val apiBaseId: Int,

        /**
         *  インデックスに相当するステージがあるかどうか ある場合=1 ないときは=0 size=3
         */
        val apiStageFlag: List<Int>,
        val apiStage1: ApiStage1?,
        val apiStage2: ApiStage2?,
        val apiStage3: ApiStage3?,
        val apiStage3Combined: ApiStage3Combined?
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
            val apiELostcount: Int,

            /**
             * 制空権表示 0=制空均衡, 1=制空権確保, 2=航空優勢, 3=航空劣勢, 4=制空権喪失
             */
            val apiDispSeiku: Int,

            /**
             * 触接装備ID [0]=味方, [1]=敵
             */
            val apiTouchPlane: List<Int>
    )

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

    data class ApiStage3(
            /**
             * 敵被ダメージ [0]=-1
             */
            val apiEdam: List<Int>
    )

    data class ApiStage3Combined(
            /**
             * 敵被ダメージ [0]=-1
             */
            val apiEdam: List<Int>?
    )
}