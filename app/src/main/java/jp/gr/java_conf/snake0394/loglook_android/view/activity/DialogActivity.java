package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;

/**
 * Serviceからダイアログを表示するためのActivity
 */
public class DialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usesLandscape", getIntent().getBooleanExtra("usesLandscape", true));
        intent.putExtra("position", getIntent().getIntExtra("position", 0));
        startActivity(intent);
        finish();

        //android.support.v4.app.DialogFragment dialogFragment = LauncherDialogFragment.newInstance();
        //dialogFragment.show(getSupportFragmentManager(), "fragment_dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decor = this.getWindow()
                         .getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("DialogActivity", "onPause");
        finish();
    }

    public void onDialogDissmissed() {
        finish();
    }
}
