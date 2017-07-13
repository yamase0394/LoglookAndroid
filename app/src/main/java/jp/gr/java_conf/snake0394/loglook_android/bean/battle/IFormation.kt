package jp.gr.java_conf.snake0394.loglook_android.bean.battle

interface IFormation {
    /**
     * 陣形/交戦形態 [0]=味方, [1]=敵, [2]=交戦形態
     * [0,1]：1=単縦陣 2=複縦陣, 3=輪形陣, 4=梯形陣, 5=単横陣
     * [2]：1=同航戦, 2=反航戦, 3=T字有利, 4=T字不利
     */
    val apiFormation: List<Int>
}