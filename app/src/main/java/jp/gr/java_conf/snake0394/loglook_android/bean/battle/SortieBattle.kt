package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import com.google.gson.annotations.SerializedName

data class SortieBattle(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiFNowhps: List<Int>,
        override val apiFMaxhps: List<Int>,
        override val apiENowhps: List<Int>,
        override val apiEMaxhps: List<Int>,
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
        override val apiHougeki3: ApiHougeki,
        override val apiRaigeki: ApiRaigeki
) : IBattle, IFormation, IAirBaseAttack, IInjectionKouku, IKouku, ISupport, IOpeningTaisen, IOpeningAttack, IHourai