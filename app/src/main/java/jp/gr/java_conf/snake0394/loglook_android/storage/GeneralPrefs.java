package jp.gr.java_conf.snake0394.loglook_android.storage;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefField;

/**
 * {@link jp.gr.java_conf.snake0394.loglook_android.view.fragment.ConfigFragment}
 */
@Pref(name = "pref_general")
public class GeneralPrefs {

    /**
     * プロキシが利用するポート番号。
     **/
    @PrefField(name = "port")
    public int port = 8000;

    /**
     * 検出領域を可視化するか。
     */
    @PrefField(name = "showsView")
    public boolean showsView = true;

    /**
     * 検出領域のx座標。
     */
    @PrefField(name = "viewX")
    public int viewX = Short.MAX_VALUE;

    /**
     * 検出領域のy座標。
     */
    @PrefField(name = "viewY")
    public int viewY = Short.MAX_VALUE;

    /*
     * 検出領域の幅。
     */
    @PrefField(name = "viewWidth")
    public int viewWidth = 20;

    /**
     * 検出領域の高さ。
     */
    @PrefField(name = "viewHeight")
    public int viewHeight = 50;

    /**
     * 検出領域の色
     */
    @PrefField(name = "viewColor")
    public int viewColor =  0xffff00;

    /**
     * 検出領域がタッチされたとき振動させるか。
     */
    @PrefField(name = "vibratesWhenViewTouched")
    public boolean vibratesWhenViewTouched = true;

    /**
     * 上流プロキシを使用するか。
     */
    @PrefField(name = "usesProxy")
    public boolean usesProxy;

    /**
     * 上流プロキシのホスト名。
     */
    @PrefField(name = "proxyHost")
    public String proxyHost = "localhost";

    /**
     * 上流プロキシのポート番号。
     */
    @PrefField(name = "proxyPort")
    public int proxyPort = 8080;

    /**
     * Jsonを記録するか。
     * defaultValue = false
     */
    @PrefField(name = "logsJson")
    public boolean logsJson;

    /**
     * リクエストボディを記録するか。
     */
    @PrefField(name = "logsRequest")
    public boolean logsRequest;

    /**
     * 遠征完了時に通知するか。
     */
    @PrefField(name = "usesMissionNotification")
    public boolean usesMissionNotification = true;

    /**
     * 入渠完了時に通知するか。
     */
    @PrefField(name = "usesDockNotification")
    public boolean usesDockNotification = true;

    /**
     * 通知時に音を鳴らすか。
     */
    @PrefField(name = "makesSoundWhenNotify")
    public boolean makesSoundWhenNotify = true;

    /**
     * 通知時に振動させる。
     */
    @PrefField(name = "vibratesWhenNOtify")
    public boolean vibratesWhenNOtify = true;

    /**
     * 横画面固定。
     */
    @PrefField(name = "forcesLandscape")
    public boolean forcesLandscape;

    @PrefField(name = "showsWinRankOverlay")
    public boolean showsWinRankOverlay;

    @PrefField(name = "showsHeavilyDamagedOverlay")
    public boolean showsHeavilyDamagedOverlay;
}
