package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import jp.gr.java_conf.snake0394.loglook_android.BattleType

data class CombinedBattleMidnightBattle(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiFNowhps: List<Int>,
        override val apiFMaxhps: List<Int>,
        override val apiENowhps: List<Int>,
        override val apiEMaxhps: List<Int>,
        override val apiNowhpsCombined: List<Int>,
        override val apiMaxhpsCombined: List<Int>,
        override val apiTouchPlane: List<Int>,
        override val apiFlarePos: List<Int>,
        override val apiHougeki: ApiHougeki
) : IMidnightBattle, ICombinedBattle