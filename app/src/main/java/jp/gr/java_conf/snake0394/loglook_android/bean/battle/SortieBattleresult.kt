package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class SortieBattleresult(

        /**
         * 戦闘結果ランク
         */
        val apiWinRank: String,

        /**
         * 海域名
         */
        val apiQuestName: String,

        val apiEnemyInfo: ApiEnemyInfo,

        val apiGetShip: ApiGetShip?
) {

    data class ApiEnemyInfo(
            /**
             * 敵艦隊名
             */
            val apiDeckName: String
    )

    data class ApiGetShip(
            /**
             * ドロップした艦種
             */
            val apiShipType: String,

            /**
             * ドロップした艦名
             */
            val apiShipName: String
    )
}
