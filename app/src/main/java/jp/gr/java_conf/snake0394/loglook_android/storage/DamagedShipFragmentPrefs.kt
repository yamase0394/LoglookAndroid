package jp.gr.java_conf.snake0394.loglook_android.storage

import android.content.Context
import jp.takuji31.koreference.KoreferenceModel
import jp.takuji31.koreference.stringPreference

/**
 * [jp.gr.java_conf.snake0394.loglook_android.view.fragment.DamagedShipFragment]
 */
class DamagedShipFragmentPrefs (context: Context):KoreferenceModel(context = context, name = "pref_damaged_ship_fragment"){

    var sortType by stringPreference("修復時間")

    var order by stringPreference("降順")
}//Empty constructor is needed