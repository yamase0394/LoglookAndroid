package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jp.gr.java_conf.snake0394.loglook_android.view.fragment.LauncherDialogFragment;

/**
 * Serviceからダイアログを表示するためのActivity
 */

public class DialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v4.app.DialogFragment dialogFragment = LauncherDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "fragment_dialog");
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
