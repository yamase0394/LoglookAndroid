package jp.gr.java_conf.snake0394.loglook_android.storage

import android.content.Context
import android.support.v4.content.ContextCompat
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.takuji31.koreference.KoreferenceModel
import jp.takuji31.koreference.booleanPreference
import jp.takuji31.koreference.intPreference
import jp.takuji31.koreference.stringPreference

/**
 * [jp.gr.java_conf.snake0394.loglook_android.view.fragment.ConfigFragment]
 */
class GeneralPrefs(context: Context) : KoreferenceModel(context = context, name = "pref_general") {

    /**
     * プロキシが利用するポート番号。
     */
    var port by intPreference(8000)

    /**
     * 検出領域を可視化するか。
     */
    var showsView by booleanPreference(true)

    /**
     * 検出領域のx座標。
     */
    var viewX by intPreference(-Short.MAX_VALUE.toInt())

    /**
     * 検出領域のy座標。
     */
    var viewY by intPreference(-Short.MAX_VALUE.toInt())

    /*
     * 検出領域の幅。
     */
    var viewWidth by intPreference(20)

    /**
     * 検出領域の高さ。
     */
    var viewHeight by intPreference(50)

    /**
     * 検出領域の色
     */
    var viewColor by intPreference(ContextCompat.getColor(context, R.color.slant_launcher_default))

    /**
     * 検出領域がタッチされたとき振動させるか。
     */
    var vibratesWhenViewTouched by booleanPreference(true)

    /**
     * 上流プロキシを使用するか。
     */
    var usesProxy by booleanPreference(false)

    /**
     * 上流プロキシのホスト名。
     */
    var proxyHost by stringPreference("localhost")

    /**
     * 上流プロキシのポート番号。
     */
    var proxyPort by intPreference(8080)

    /**
     * Jsonを記録するか。
     * defaultValue = false
     */
    var logsJson by booleanPreference(false)

    /**
     * リクエストボディを記録するか。
     */
    var logsRequest by booleanPreference(false)

    /**
     * 遠征完了時に通知するか。
     */
    var usesMissionNotification by booleanPreference(true)

    /**
     * 入渠完了時に通知するか。
     */
    var usesDockNotification by booleanPreference(true)

    /**
     * 通知時に音を鳴らすか。
     */
    var makesSoundWhenNotify by booleanPreference(true)

    /**
     * 通知時に振動させる。
     */
    var vibratesWhenNOtify by booleanPreference(true)

    var showsWinRankOverlay by booleanPreference(true)

    var showsHeavilyDamagedOverlay by booleanPreference(true)

    var showsHeavilyDamagedWarningWindow by booleanPreference(true)

    var sendsJson by booleanPreference(false)
}
