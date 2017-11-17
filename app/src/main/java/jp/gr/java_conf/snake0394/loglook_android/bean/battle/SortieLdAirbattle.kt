package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import com.google.gson.annotations.SerializedName

data class SortieLdAirbattle(
        override val apiDeckId: Int,
        override val apiShipKe: List<Int>,
        override val apiShipLv: List<Int>,
        override val apiFNowhps: List<Int>,
        override val apiFMaxhps: List<Int>,
        override val apiENowhps: List<Int>,
        override val apiEMaxhps: List<Int>,
        override val apiFormation: List<Int>,
        override val apiStageFlag: List<Int>,
        override val apiKouku: ApiKouku
) : IBattle, IFormation, IKouku