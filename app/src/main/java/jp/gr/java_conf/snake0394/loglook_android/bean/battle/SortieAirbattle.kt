package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import com.google.gson.annotations.SerializedName

data class SortieAirbattle(
        @SerializedName("api_dock_id")
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiNowhps: List<Int>,
        override val apiMaxhps: List<Int>,
        override val apiFormation: List<Int>,
        override val apiAirBaseInjection: ApiAirBaseInjection,
        override val apiInjectionKouku: ApiInjectionKouku,
        override val apiAirBaseAttack: List<ApiAirBaseAttack>,
        override val apiStageFlag: List<Int>,
        override val apiKouku: ApiKouku,
        override val apiStageFlag2: List<Int>,
        override val apiKouku2: ApiKouku
) : IBattle, IFormation, IAirBaseAttack, IInjectionKouku, IKouku, IKouku2