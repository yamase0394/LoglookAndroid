package jp.gr.java_conf.snake0394.loglook_android.storage

import android.content.Context
import jp.takuji31.koreference.KoreferenceModel
import jp.takuji31.koreference.stringPreference

/**
 * [jp.gr.java_conf.snake0394.loglook_android.view.fragment.EquipmentFragment]
 */
class EquipmentFragmentPrefs(context: Context): KoreferenceModel(context = context, name = "pref_equip_fragment") {

    var sortType by stringPreference("名前")

    var order by stringPreference("昇順")
}//Empty constructor is needed