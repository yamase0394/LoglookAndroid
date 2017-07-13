package jp.gr.java_conf.snake0394.loglook_android.bean.battle

interface ISupport {
    /**
     * 支援艦隊フラグ
     * 0=なし 1=空撃 2=砲撃 3=雷撃
     */
    val apiSupportFlag: Int
    val apiSupportInfo: ApiSupportInfo
}