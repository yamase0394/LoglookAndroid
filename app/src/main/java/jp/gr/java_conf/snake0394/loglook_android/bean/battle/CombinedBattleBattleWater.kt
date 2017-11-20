package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * 連合艦隊(水上部隊) vs 敵連合艦隊
 */
data class CombinedBattleBattleWater(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiFNowhps: List<Int>,
        override val apiFMaxhps: List<Int>,
        override val apiENowhps: List<Int>,
        override val apiEMaxhps: List<Int>,
        override val apiFNowhpsCombined: List<Int>,
        override val apiFMaxhpsCombined: List<Int>,
        override val apiFormation: List<Int>,
        override val apiAirBaseInjection: ApiAirBaseInjection,
        override val apiInjectionKouku: ApiInjectionKouku,
        override val apiAirBaseAttack: List<ApiAirBaseAttack>,
        override val apiStageFlag: List<Int>,
        override val apiKouku: ApiKouku,
        override val apiSupportFlag: Int,
        override val apiSupportInfo: ApiSupportInfo,
        override val apiOpeningTaisenFlag: Int,
        override val apiOpeningTaisen: ApiOpeningTaisen,
        override val apiOpeningFlag: Int,
        override val apiOpeningAtack: ApiRaigeki,
        override val apiHouraiFlag: List<Int>,

        /**
         * 本隊
         */
        override val apiHougeki1: ApiHougeki,

        /**
         * 本隊
         */
        override val apiHougeki2: ApiHougeki,

        /**
         * 随伴
         */
        override val apiHougeki3: ApiHougeki,

        /**
         * 随伴 雷撃
         */
        override val apiRaigeki: ApiRaigeki
) : ICombinedBattle, IFormation, IAirBaseAttack, IInjectionKouku, IKouku, ISupport, IOpeningTaisen, IOpeningAttack, IHourai