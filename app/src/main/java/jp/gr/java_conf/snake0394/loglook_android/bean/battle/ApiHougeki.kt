package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * Created by snake0394 on 2017/07/05.
 */
data class ApiHougeki(
        /**
         * 攻撃フラグ 0=味方, 1=敵が攻撃する -1は消した
         * CombinedEachBattle, CombinedEachBattleWater, CombinedEcBattle以外のときはnull
         */
        val apiAtEflag: List<Int>?,

        /**
         * 砲撃戦の攻撃を受ける艦の位置。
         * 攻撃を受ける順 1～6=味方、7～12=敵
         * -1は消した
         */
        val apiDfList: List<List<Int>>,

        /**
         * -1は消した
         */
        val apiDamage: List<List<Int>>
)