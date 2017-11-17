package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class MidnightSpMidnight(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiFNowhps: List<Int>,
        override val apiFMaxhps: List<Int>,
        override val apiENowhps: List<Int>,
        override val apiEMaxhps: List<Int>,
        override val apiFormation: List<Int>,
        override val apiTouchPlane: List<Int>,
        override val apiFlarePos: List<Int>,
        override val apiHougeki: ApiHougeki
) : IMidnightBattle, IFormation