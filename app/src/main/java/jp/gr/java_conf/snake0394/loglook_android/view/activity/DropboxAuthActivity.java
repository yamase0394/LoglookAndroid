package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import jp.gr.java_conf.snake0394.loglook_android.DropboxAuthManager;
import jp.gr.java_conf.snake0394.loglook_android.R;

public class DropboxAuthActivity extends AppCompatActivity {

    DropboxAuthManager dropboxAuthManager;
    DropboxAPI<AndroidAuthSession> mDBApi;
    boolean isFirstOnResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox_auth);

        dropboxAuthManager = new DropboxAuthManager(this);

        //認証済みかを確認
        if (!dropboxAuthManager.hasLoadAndroidAuthSession()) {
            AppKeyPair appKeys = new AppKeyPair(dropboxAuthManager.APPKEY, dropboxAuthManager.APPKEYSECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys);
            mDBApi = new DropboxAPI<>(session);
            mDBApi.getSession().startOAuth2Authentication(this); //認証していない場合は認証ページを表示
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("saveInDropbox", true);
            editor.apply();
            finish();
            Intent sendIntent = new Intent(this, MainActivity.class);
            sendIntent.putExtra("position", MainActivity.Fragment.CONFIG.getPosition());
            startActivity(sendIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("auth", "resume");
        if (mDBApi != null && mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                dropboxAuthManager.storeOauth2AccessToken(mDBApi.getSession().getOAuth2AccessToken());
                Toast.makeText(this, "認証しました", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("saveInDropbox", true);
                editor.apply();
            } catch (IllegalStateException e) {
            }
        }
        if (!isFirstOnResume) {
            finish();
            Intent sendIntent = new Intent(this, MainActivity.class);
            sendIntent.putExtra("position", MainActivity.Fragment.CONFIG.getPosition());
            startActivity(sendIntent);
        }
        isFirstOnResume = false;
    }
}
