package jp.gr.java_conf.snake0394.loglook_android.bean.battle

data class CombinedBattleLdAirbattle(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiNowhps: List<Int>,
        override val apiMaxhps: List<Int>,
        override val apiNowhpsCombined: List<Int>,
        override val apiMaxhpsCombined: List<Int>,
        override val apiFormation: List<Int>,
        override val apiStageFlag: List<Int>,
        override val apiKouku: ApiKouku
) : ICombinedBattle, IFormation, IKouku