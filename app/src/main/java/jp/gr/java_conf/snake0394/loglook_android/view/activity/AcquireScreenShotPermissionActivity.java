package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import jp.gr.java_conf.snake0394.loglook_android.DeckListCaptureService;
import jp.gr.java_conf.snake0394.loglook_android.ScreenCaptureService;
import jp.gr.java_conf.snake0394.loglook_android.ScreenShotService;
import jp.gr.java_conf.snake0394.loglook_android.TemplateMatchingService;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;

/**
 * Serviceからダイアログを表示するためのActivity
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AcquireScreenShotPermissionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCREEN_CAPTURE = 111;

    private MediaProjectionManager mMediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.animator.keep_transparent, R.animator.keep_opaque);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
            return;
        }

        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        // MediaProjectionの利用にはパーミッションが必要。
        // ユーザーへの問い合わせのため、Intentを発行
        Intent permissionIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_CAPTURE);
        //overridePendingTransition(R.animator.fade_in, R.animator.keep_transparent);
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
        Logger.d("ScreenCaptureActivity", "onPause");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //overridePendingTransition(R.animator.keep_transparent, R.animator.fade_out);
        if (REQUEST_CODE_SCREEN_CAPTURE == requestCode) {
            if (resultCode == RESULT_OK) {
                ScreenCaptureService.setScreenshotPermission((Intent) intent.clone());
            }
            
            Intent serviceIntent;
            if (getIntent().getStringExtra("class").equals(ScreenShotService.class.getSimpleName())){
                serviceIntent = new Intent(getApplicationContext(), ScreenShotService.class);
            } else if(getIntent().getStringExtra("class").equals(DeckListCaptureService.class.getSimpleName())){
                serviceIntent = new Intent(getApplicationContext(), DeckListCaptureService.class);
            } else if(getIntent().getStringExtra("class").equals(TemplateMatchingService.class.getSimpleName())){
                serviceIntent = new Intent(getApplicationContext(), TemplateMatchingService.class);
            } else {
                finish();
                return;
            }
            startService(serviceIntent);

            String packageName = "com.dmm.dmmlabo.kancolle";
            PackageManager pm = getPackageManager();
            Intent sendIntent = pm.getLaunchIntentForPackage(packageName);
            startActivity(sendIntent);
            //overridePendingTransition(R.animator.keep_opaque, R.animator.keep_transparent);
            finish();
        }
    }
}
