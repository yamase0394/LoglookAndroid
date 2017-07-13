package jp.gr.java_conf.snake0394.loglook_android.bean.battle


data class PhaseState(
        val type: Type,
        val fHp: MutableList<Int>,
        val fHpCombined: MutableList<Int>?,
        val eHp: MutableList<Int>,
        val eHpCombined: MutableList<Int>?
) {
    fun deepCopy(type: Type): PhaseState {
        return PhaseState(type, fHp.toMutableList(), fHpCombined?.toMutableList(), eHp.toMutableList(), eHpCombined?.toMutableList())
    }

    enum class Type(name: String) {
        BEFORE("戦闘前"),
        BASE_INJECTION("噴式基地航空隊"),
        INJECTION("噴式"),
        BASE("基地航空隊"),
        KOUKU("航空戦"),
        OPENING_ANTI_SUBMARINE("開幕対潜"),
        OPENING_TORPEDO("開幕雷撃"),
        SUPPORT("支援攻撃"),
        SHELLING("砲雷撃戦"),
        KOUKU2("航空戦2"),
        MIDNIGHT("夜戦")
    }
}