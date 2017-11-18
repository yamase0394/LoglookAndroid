package jp.gr.java_conf.snake0394.loglook_android.bean.battle

interface INSupport {
    /**
     * 支援艦隊フラグ
     * 0=なし 1=空撃 2=砲撃
     */
    val apiNSupportFlag: Int
    val apiNSupportInfo: ApiSupportInfo
}