package jp.gr.java_conf.snake0394.loglook_android;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import io.netty.util.internal.StringUtil;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ScreenCaptureActivity;


/**
 * アプリを起動するランチャー
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenShotService extends Service {

    private final Handler handler = new Handler();

    private ImageReader mImageReader;
    private VirtualDisplay mVirtualDisplay;
    private WindowManager wm;
    private MediaProjection mediaProjection;
    //タップ検出領域
    private LinearLayout linearLayout;
    private WindowManager.LayoutParams params;
    private int displayWidth;
    private int displayHeight;
    private ImageView preview;
    private String filePath;

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

        setTheme(R.style.AppTheme);

        mediaProjection = ScreenCaptureActivity.getMediaProjection();
        if (mediaProjection == null) {
            stopSelf();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "起動失敗", Toast.LENGTH_SHORT)
                         .show();
                }
            });
            Logger.d("ScreenShotService", "mediaProjection = null");
            return;
        }

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

        //タッチイベントを取得するためのviewを作る
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.layout_screen_shot, null);

        params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, displayWidth / 2, displayHeight / 2, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        ImageButton captureButton = ButterKnife.findById(linearLayout, R.id.button_cap);
        captureButton.setOnTouchListener(new View.OnTouchListener() {
                                             @Override
                                             public boolean onTouch(View v, MotionEvent event) {
                                                 if (event.getAction() != MotionEvent.ACTION_UP) {
                                                     return false;
                                                 }
                                                 try {
                                                     wm.removeViewImmediate(linearLayout);

                                                     Thread.sleep(100);

                                                     Bitmap bitmap = getScreenshot();
                                                     wm.addView(linearLayout, params);
                                                     if (bitmap == null) {
                                                         return true;
                                                     }

                                                     Bitmap screenShot = removeBlank(bitmap);

                                                     preview.setImageBitmap(screenShot);
                                                     
                                                     //SDカードのディレクトリパス
                                                     File sdcard_path = new File(Environment.getExternalStorageDirectory()
                                                                                            .getPath() + "/泥提督支援アプリ/capture/screenShot");

                                                     //フォルダがなければ作成
                                                     if (!sdcard_path.exists()) {
                                                         sdcard_path.mkdirs();
                                                     }

                                                     // 日付でファイル名を作成
                                                     Date mDate = new Date();
                                                     SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
    
                                                     //パス区切り用セパレータ
                                                     String Fs = File.separator;
    
                                                     //テキストファイル保存先のファイルパス
                                                     filePath = sdcard_path + Fs + fileName.format(mDate) + ".jpg";

                                                     // 保存処理開始
                                                     FileOutputStream fos = new FileOutputStream(new File(filePath));

                                                     // jpegで保存
                                                     screenShot.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                                                     // 保存処理終了
                                                     fos.close();

                                                 } catch (Exception e) {
                                                     handler.post(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             Toast.makeText(getApplicationContext(), "スクリーンショット失敗。エラーログを記録しました。", Toast.LENGTH_LONG)
                                                                  .show();
                                                         }
                                                     });
                                                     ErrorLogger.writeLog(e);
                                                     e.printStackTrace();
                                                     stopSelf();
                                                 }

                                                 return true;
                                             }
                                         }

        );
    
        ImageButton editButton = ButterKnife.findById(linearLayout, R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtil.isNullOrEmpty(filePath)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "画像が空です", Toast.LENGTH_SHORT)
                                 .show();
                        }
                    });
                    return;
                }
    
                stopSelf();
    
                File file = new File(filePath);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(file), "image/jpg");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(intent);
            }
        });
    
        ImageButton shareButton = ButterKnife.findById(linearLayout, R.id.button_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtil.isNullOrEmpty(filePath)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "画像が空です", Toast.LENGTH_SHORT)
                                 .show();
                        }
                    });
                    return;
                }
            
                stopSelf();
            
                File file = new File(filePath);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(file), "image/jpg");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(intent);
            }
        });

        ImageButton closeButton = ButterKnife.findById(linearLayout, R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });

        preview = ButterKnife.findById(linearLayout, R.id.imageView);

        ImageButton dragHandle = ButterKnife.findById(linearLayout, R.id.view_drag_handle);
        dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getRawX();
                int y = (int) motionEvent.getRawY();
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    int centerX = x - (displayWidth / 2);
                    int centerY = y - (displayHeight / 2);

                    params.x = centerX + linearLayout.getWidth() / 2 - 10;
                    params.y = centerY + linearLayout.getHeight() / 2 - 10;

                    wm.updateViewLayout(linearLayout, params);
                }
                return false;
            }
        });

        wm.addView(linearLayout, params);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeViewImmediate(linearLayout);
        mVirtualDisplay.release();
        mediaProjection.stop();
        Logger.d("ScreenShotService", "onDestroy");
    }

    private Bitmap getScreenshot() {
        Image image = mImageReader.acquireLatestImage();
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

    private Bitmap removeBlank(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        // Bitmap から Pixel を取得
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        // Pixel 操作部分
        Logger.d("mwidth", String.valueOf(displayWidth));
        Logger.d("mheight", String.valueOf(displayHeight));
        Logger.d("width", String.valueOf(width));
        Logger.d("height", String.valueOf(height));
        int kcsWidth = height * 5 / 3;
        if (kcsWidth < displayWidth) {
            Logger.d("kcswidth", String.valueOf(kcsWidth));
            int blackWidth = (displayWidth - kcsWidth) / 2;
            Logger.d("minX", String.valueOf(blackWidth));
            int[] newPixcels = new int[kcsWidth * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[x + y * width];
                    if (x < blackWidth || x > kcsWidth + blackWidth - 1) {
                        continue;
                    }
                    newPixcels[(x - blackWidth) + y * kcsWidth] = pixel;
                }
            }

            return Bitmap.createBitmap(newPixcels, kcsWidth, height, Bitmap.Config.ARGB_8888);
        } else if (kcsWidth == displayWidth) {
            return bitmap;
        } else {
            int kcsHeight = displayWidth * 3 / 5;
            int blackHeight = (displayHeight - kcsHeight) / 2;
            int[] newPixcels = new int[displayWidth * kcsHeight];
            for (int y = 0; y < height; y++) {
                if (y < blackHeight || y > kcsHeight + blackHeight - 1) {
                    continue;
                }
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[x + y * width];
                    newPixcels[(y - blackHeight) + y * displayWidth] = pixel;
                }
            }

            return Bitmap.createBitmap(newPixcels, displayWidth, kcsHeight, Bitmap.Config.ARGB_8888);
        }
    }
}
