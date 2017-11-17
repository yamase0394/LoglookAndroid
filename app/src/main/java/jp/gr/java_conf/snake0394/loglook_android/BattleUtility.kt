package jp.gr.java_conf.snake0394.loglook_android

object BattleUtility {
    /**
     * api_disp_seiku
     */
    fun getDispSeiku(num: Int): String {
        return when (num) {
            0 -> "制空均衡"
            1 -> "制空権確保"
            2 -> "航空優勢"
            3 -> "航空劣勢"
            4 -> "制空権喪失"
            else -> throw IllegalArgumentException("illegal value:$num")
        }
    }

    /**
     * api_formation[2]
     */
    fun getTactic(num: Int): String {
        return when (num) {
            1 -> "同航戦"
            2 -> "反航戦"
            3 -> "T字戦有利"
            4 -> "T字戦不利"
            else -> throw IllegalArgumentException("illegal value:$num")
        }
    }

    /**
     * api_formation[0,1]
     */
    fun getFormation(num: Int): String {
        return when (num) {
            1 -> "単縦陣"
            2 -> "複縦陣"
            3 -> "輪形陣"
            4 -> "梯形陣"
            5 -> "単横陣"
            6 -> "警戒陣"
            11 -> "第一警戒航行序列"
            12 -> "第二警戒航行序列"
            13 -> "第三警戒航行序列"
            14 -> "第四警戒航行序列"
            else -> throw IllegalArgumentException("illegal value:$num")
        }
    }
}