package jp.gr.java_conf.snake0394.loglook_android.bean.battle

interface IEnemyCombinedMidnightBattle :IMidnightBattle{
    /**
     * 戦闘参加艦隊フラグ [2] [0]=味方側, [1]=敵側 1=主力艦隊, 2=随伴艦隊
     */
    val apiActiveDeck:List<Int>
}