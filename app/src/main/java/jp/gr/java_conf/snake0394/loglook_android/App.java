package jp.gr.java_conf.snake0394.loglook_android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import io.realm.Realm;

/**
 * Created by snake0394 on 2016/10/27.
 */

public class App extends Application {
    // コンテキスト
    private static App instance;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
