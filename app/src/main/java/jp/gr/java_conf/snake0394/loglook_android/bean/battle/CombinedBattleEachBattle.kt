package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * 連合艦隊(機動部隊) vs 敵連合艦隊
 */
data class CombinedBattleEachBattle(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipKeCombined: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiShipLvCombined: List<Int>,
        override val apiNowhps: List<Int>,
        override val apiMaxhps: List<Int>,
        override val apiNowhpsCombined: List<Int>,
        override val apiMaxhpsCombined: List<Int>,
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
        override val apiHougeki1: ApiHougeki,
        override val apiHougeki2: ApiHougeki,
        override val apiRaigeki: ApiRaigeki,
        override val apiHougeki3: ApiHougeki
) : IEachCombinedBattle, IFormation, IAirBaseAttack, IInjectionKouku, IKouku, ISupport, IOpeningTaisen, IOpeningAttack, IHourai