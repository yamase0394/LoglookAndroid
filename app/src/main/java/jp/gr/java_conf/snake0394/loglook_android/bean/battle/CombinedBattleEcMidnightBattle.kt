package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class CombinedBattleEcMidnightBattle(
        override val apiActiveDeck: List<Int>,
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipKeCombined: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiShipLvCombined: List<Int>,
        override val apiFNowhps: List<Int>,
        override val apiFMaxhps: List<Int>,
        override val apiENowhps: List<Int>,
        override val apiEMaxhps: List<Int>,
        override val apiENowhpsCombined: List<Int>,
        override val apiEMaxhpsCombined: List<Int>,
        override val apiTouchPlane: List<Int>,
        override val apiFlarePos: List<Int>,
        override val apiHougeki: ApiHougeki
) : IEnemyCombinedMidnightBattle, IEnemyCombinedBattle