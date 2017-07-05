package jp.gr.java_conf.snake0394.loglook_android;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefsSpotRepository;


/**
 * アプリを起動するランチャー
 */
public class HeavilyDamagedWarningService extends Service  {
    private static final String TAG = "HeavilyDamagedWarningService";

    private WindowManager wm;
    private GeneralPrefs prefs;
    private View v;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        Logger.d(TAG, "onStartCommand");
        //タッチイベントを取得するためのviewを作る
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        this.prefs = GeneralPrefsSpotRepository.getEntity(getApplicationContext());

        v = View.inflate(getApplicationContext(), R.layout.overray_heavily_damaged_warning, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.OPAQUE);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        wm.addView(v, params);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(v);
    }
}
