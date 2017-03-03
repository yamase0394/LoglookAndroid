package jp.gr.java_conf.snake0394.loglook_android.storage;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefBoolean;
import io.github.kobakei.spot.annotation.PrefInt;
import io.github.kobakei.spot.annotation.PrefString;

/**
 * {@link jp.gr.java_conf.snake0394.loglook_android.view.fragment.ConfigFragment}
 */
@Pref(name = "pref_general")
public class GeneralPrefs {

    /**
     * プロキシが利用するポート番号。
     * defaultValue = 8000
     **/
    @PrefInt(name = "port", defaultValue = 8000)
    public int port;

    /**
     * 検出領域を可視化するか。
     * defaultValue = true
     */
    @PrefBoolean(name = "showsView", defaultValue = true)
    public boolean showsView;

    /**
     * 検出領域のx座標。
     * defaultValue = Short.MAX_VALUE
     */
    @PrefInt(name = "viewX", defaultValue = Short.MAX_VALUE)
    public int viewX;

    /**
     * 検出領域のy座標。
     * defaultValue = Short.MAX_VALUE
     */
    @PrefInt(name = "viewY", defaultValue = Short.MAX_VALUE)
    public int viewY;

    /*
     * 検出領域の幅。
     * defaultValue = 20
     */
    @PrefInt(name = "viewWidth", defaultValue = 20)
    public int viewWidth;

    /**
     * 検出領域の高さ。
     * defaultValue = 50
     */
    @PrefInt(name = "viewHeight", defaultValue = 50)
    public int viewHeight;

    /**
     * 検出領域がタッチされたとき振動させるか。
     * defaultValue = true
     */
    @PrefBoolean(name = "vibratesWhenViewTouched", defaultValue = true)
    public boolean vibratesWhenViewTouched;

    /**
     * 上流プロキシを使用するか。
     * defaultValue = false
     */
    @PrefBoolean(name = "usesProxy")
    public boolean usesProxy;

    /**
     * 上流プロキシのホスト名。
     * defaultValue = localhost
     */
    @PrefString(name = "proxyHost", defaultValue = "localhost")
    public String proxyHost;

    /**
     * 上流プロキシのポート番号。
     * defaultValue = 8080
     */
    @PrefInt(name = "proxyPort", defaultValue = 8080)
    public int proxyPort;

    /**
     * Jsonを記録するか。
     * defaultValue = false
     */
    @PrefBoolean(name = "logsJson", defaultValue = false)
    public boolean logsJson;

    /**
     * リクエストボディを記録するか。
     * defaultValue = false
     */
    @PrefBoolean(name = "logsRequest", defaultValue = false)
    public boolean logsRequest;

    /**
     * 遠征完了時に通知するか。
     * defaultValue = true
     */
    @PrefBoolean(name = "usesMissionNotification", defaultValue = true)
    public boolean usesMissionNotification;

    /**
     * 入渠完了時に通知するか。
     * defaultValue = true
     */
    @PrefBoolean(name = "usesDockNotification", defaultValue = true)
    public boolean usesDockNotification;

    /**
     * 通知時に音を鳴らすか。
     * defaultValue = true
     */
    @PrefBoolean(name = "makesSoundWhenNotify", defaultValue = true)
    public boolean makesSoundWhenNotify;

    /**
     * 通知時に振動させる。
     * defaultValue = true
     */
    @PrefBoolean(name = "vibratesWhenNOtify", defaultValue = true)
    public boolean vibratesWhenNOtify;

}
