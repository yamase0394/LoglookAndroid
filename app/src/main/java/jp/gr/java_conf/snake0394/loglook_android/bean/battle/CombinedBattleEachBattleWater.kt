package jp.gr.java_conf.snake0394.loglook_android.bean.battle

/**
 * 連合艦隊(水上部隊) vs 敵連合艦隊
 */
data class CombinedBattleEachBattleWater(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipKeCombined: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiShipLvCombined: List<Int>,
        override val apiFNowhps: List<Int>,
        override val apiFMaxhps: List<Int>,
        override val apiENowhps: List<Int>,
        override val apiEMaxhps: List<Int>,
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

        /**
         * 第一次砲撃戦（主力艦隊vs主力艦隊）
         */
        override val apiHougeki1: ApiHougeki,

        /**
         * 随伴艦隊vs随伴艦隊
         */
        override val apiHougeki2: ApiHougeki,

        /**
         * 主力艦隊vs主力艦隊, 対全体
         */
        override val apiHougeki3: ApiHougeki,
        /**
         * 雷撃 対全体
         */
        override val apiRaigeki: ApiRaigeki
) : IEachCombinedBattle, IFormation, IAirBaseAttack, IInjectionKouku, IKouku, ISupport, IOpeningTaisen, IOpeningAttack, IHourai