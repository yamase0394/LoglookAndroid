package jp.gr.java_conf.snake0394.loglook_android.bean.battle

interface IMidnightBattle :IBattle{
    /**
     * 夜間触接装備ID [0]=味方, [1]=敵　文字列型になることがある
     */
    val apiTouchPlane:List<Int>

    /**
     * [0]=味方 [1]=敵 1～6
     */
    val apiFlarePos:List<Int>

    val apiHougeki:ApiHougeki
}