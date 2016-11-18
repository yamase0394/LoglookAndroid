package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

/**
 * DropBoxの認証情報を管理するクラスです。
 */
public class DropboxAuthManager {
    private static final String TOKEN = "token";
    private static final String PREF_NAME = "dropbox";
    public static final String APPKEY;
    public static final String APPKEYSECRET;
    static {
        APPKEY = App.getInstance().getString(R.string.appkey);
        APPKEYSECRET =App.getInstance().getString(R.string.appkeysecret);
    }
    private Context context;

    public DropboxAuthManager(Context context) {
        this.context = context;
    }

    public void storeOauth2AccessToken(String secret) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, secret);
        editor.commit();
    }

    public AndroidAuthSession loadAndroidAuthSession() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        if (token != null) {
            AppKeyPair appKeys = new AppKeyPair(APPKEY, APPKEYSECRET);
            return new AndroidAuthSession(appKeys, token);
        } else {

            return null;
        }
    }

    public boolean hasLoadAndroidAuthSession() {
        return loadAndroidAuthSession() != null;
    }
}
