package jp.gr.java_conf.snake0394.loglook_android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.nio.ByteBuffer;

import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.AcquireScreenShotPermissionActivity;


/**
 * アプリを起動するランチャー
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenCaptureService extends Service {

    private final Handler handler = new Handler();

    private  static ImageReader mImageReader;
    private  static VirtualDisplay mVirtualDisplay;
    private WindowManager wm;
    private static MediaProjection mediaProjection;
    private static MediaProjectionManager mediaProjectionManager;
    
    private static Intent screenshotPermission = null;
    
    private static int displayWidth;
    private static int displayHeight;
    
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.d("ScreenShotService", "onCreate");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            stopSelf();
            return;
        }
    
        while (mediaProjection == null) {
            getScreenshotPermission();
        }

        setTheme(R.style.AppTheme);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        wm.getDefaultDisplay()
          .getMetrics(metrics);
        
        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;
        int density = metrics.densityDpi;

        mImageReader = ImageReader.newInstance(displayWidth, displayHeight, PixelFormat.RGBA_8888, 2);

        mVirtualDisplay = mediaProjection.createVirtualDisplay("Capturing Display", displayWidth, displayHeight, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVirtualDisplay.release();
        mediaProjection.stop();
        Logger.d("ScreenShotService", "onDestroy");
    }

    public static Bitmap getScreenshot() {
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            return null;
        }
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();

        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * displayWidth;

        Bitmap bitmap = Bitmap.createBitmap(displayWidth + rowPadding / pixelStride, displayHeight, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        return bitmap;
    }
    
    protected static void getScreenshotPermission() {
        try {
            if (screenshotPermission != null) {
                if(null != mediaProjection) {
                    mediaProjection.stop();
                    mediaProjection = null;
                }
                mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, (Intent) screenshotPermission.clone());
            } else {
                openScreenshotPermissionRequester();
            }
        } catch (final RuntimeException ignored) {
            openScreenshotPermissionRequester();
        }
    }
    
    protected static void openScreenshotPermissionRequester(){
        final Intent intent = new Intent(App.getInstance() , AcquireScreenShotPermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        App.getInstance().startActivity(intent);
    }
    
    public static void setScreenshotPermission(final Intent permissionIntent) {
        screenshotPermission = permissionIntent;
    }
}
