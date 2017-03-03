package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.util.Log;

import jp.gr.java_conf.snake0394.loglook_android.BuildConfig;

/**
 * Created by snake0394 on 2017/03/03.
 */
public class Logger {
    private static final String TAG = "LoglookAndroid";

    public static final void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static final void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }
}
