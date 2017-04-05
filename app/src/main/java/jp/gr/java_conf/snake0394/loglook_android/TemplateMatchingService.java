package jp.gr.java_conf.snake0394.loglook_android;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;

import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ScreenCaptureActivity;

import static org.opencv.imgproc.Imgproc.INTER_NEAREST;


/**
 * アプリを起動するランチャー
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TemplateMatchingService extends Service implements Runnable {
    
    private static final String TAG = "TemplateMatchingService";
    
    private final Handler handler = new Handler();
    
    private Thread workerThread;
    private boolean stop;
    
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
    private View rootView;
    
    private Mat template;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate");
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Logger.d(TAG, "Android version is lower than Lolipop");
            stopSelf();
            return;
        }
        
        setTheme(R.style.AppTheme);
        
        mediaProjection = ScreenCaptureActivity.getMediaProjection();
        if (mediaProjection == null) {
            Logger.d(TAG, "MediaProjection is null");
            stopSelf();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "起動失敗", Toast.LENGTH_SHORT)
                         .show();
                }
            });
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
        
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.advance2);
        Logger.d("drawableHeight", String.valueOf(bitmap.getHeight()));
        Logger.d("drawableWidth", String.valueOf(bitmap.getWidth()));
        template = new Mat();
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Bitmap screenShot = getScreenshot();
        while (screenShot == null) {
            screenShot = getScreenshot();
        }
        screenShot = removeBlank(screenShot);
        Logger.d("screenHeight", String.valueOf(screenShot.getHeight()));
        Logger.d("screenWidth", String.valueOf(screenShot.getWidth()));
        Logger.d("srcHeight", String.valueOf(src.height()));
        Logger.d("srcWidth", String.valueOf(src.width()));
        Imgproc.resize(src, src, new Size(0,0), 0.5, 0.5, INTER_NEAREST);
        Imgproc.resize(src, template, new Size(0,0), screenShot.getHeight() / 720, screenShot.getHeight() / 720, INTER_NEAREST);
        Logger.d("templateHeight", String.valueOf(template.height()));
        Logger.d("templateWidth", String.valueOf(template.width()));
        screenShot.recycle();
        src.release();
        bitmap.recycle();
    }
    
    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        workerThread = new Thread(this);
        workerThread.start();
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy");
        mVirtualDisplay.release();
        mediaProjection.stop();
        workerThread.interrupt();
        stop = true;
        template.release();
        if (rootView != null) {
            wm.removeViewImmediate(rootView);
        }
    }
    
    @Override
    public void run() {
        Logger.d(TAG, "start template matching");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!this.stop) {
            try {
                Bitmap screenShot = getScreenshot();
                if (screenShot == null) {
                    continue;
                }
                
                Bitmap noBlank = removeBlank(screenShot);
                screenShot.recycle();
                
                
                int width = noBlank.getWidth();
                int height = noBlank.getHeight();
                int[] pixels = new int[width * height];
                int newWidth = template.width();
                int newHeight = template.height();
                noBlank.getPixels(pixels, 0, newWidth, (int) (displayWidth * 0.3), (int) (displayHeight * 0.4), newWidth, newHeight);
                Bitmap cropped = Bitmap.createBitmap(pixels, newWidth, newHeight, Bitmap.Config.ARGB_8888);
                noBlank.recycle();
                
    
                /*
                int width = noBlank.getWidth();
                int height = noBlank.getHeight();
                int newWidth = template.width();
                int newHeight = template.height();
                Logger.d("noBlankWidth", String.valueOf(width));
                Logger.d("noBlankHeight", String.valueOf(height));
                Logger.d("newWidth", String.valueOf(newWidth));
                Logger.d("newHeight", String.valueOf(newHeight));
                int[] pixels = new int[newWidth * newHeight];
                noBlank.getPixels(pixels, 0, newWidth, (int) (width * 0.25), (int) (height * 0.4), newWidth, newHeight);
                Bitmap cropped = Bitmap.createBitmap(pixels, newWidth, newHeight, Bitmap.Config.ARGB_8888);
                noBlank.recycle();
                */
                
                Mat image = new Mat();
                Utils.bitmapToMat(cropped, image);
                cropped.recycle();
                
                Mat result = new Mat();
                Imgproc.matchTemplate(image, template, result, Imgproc.TM_CCOEFF_NORMED);
                
                Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);
                image.release();
                result.release();
                Logger.d("maxVal", String.valueOf(minMaxLocResult.maxVal));
                Logger.d("minVal", String.valueOf(minMaxLocResult.minVal));
                //Logger.d("x", String.valueOf(minMaxLocResult.maxLoc.x));
                //Logger.d("y", String.valueOf(minMaxLocResult.maxLoc.y));
                
                if (minMaxLocResult.minVal > 0.18) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showOverlay();
                        }
                    });
                    break;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Logger.d(TAG, "end template matching");
    }
    
    private Bitmap getScreenshot() {
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
    
    private Bitmap removeBlank(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        // Bitmap から Pixel を取得
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        // Pixel 操作部分
        int kcsWidth = height * 5 / 3;
        if (kcsWidth < displayWidth) {
            int blackWidth = (displayWidth - kcsWidth) / 2;
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
    
    private void showOverlay() {
        rootView = View.inflate(getApplicationContext(), R.layout.overlay_tactical_situation, null);
        TacticalSituation ts = TacticalSituation.INSTANCE;
        String packageName = getPackageName();
        Resources res = getResources();
        //本隊
        for (int i = 1; i <= 6; i++) {
            if (i > ts.getFriendShipId()
                      .size()) {
                String name = "name" + i;
                int strId = res.getIdentifier(name, "id", packageName);
                TextView text = ButterKnife.findById(rootView, strId);
                text.setText("");
                
                name = "lv" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setText("");
                
                name = "state" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setText("");
                
                name = "hp" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setText("");
                
                name = "beforeHp" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setText("");
                
                name = "damage" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setText("");
                
                continue;
            }
            
            int id = ts.getFriendShipId()
                       .get(i - 1);
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(id);
            
            String name = "name" + i;
            int strId = res.getIdentifier(name, "id", packageName);
            TextView text = ButterKnife.findById(rootView, strId);
            text.setText(myShip.getName());
            
            name = "lv" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            text.setText("(Lv" + myShip.getLv() + ")");
            
            name = "state" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                text.setText("退避");
                text.setTextColor(Color.LTGRAY);
            } else if (ts.getFriendNowhps()
                         .get(i - 1) <= 0) {
                text.setText("轟沈");
                //オーシャンブルー
                text.setTextColor(Color.rgb(0, 102, 204));
            } else if (ts.getFriendNowhps()
                         .get(i - 1) <= ts.getFriendMaxhps()
                                          .get(i - 1) / 4) {
                text.setText("大破");
                text.setTextColor(Color.RED);
            } else if (ts.getFriendNowhps()
                         .get(i - 1) <= ts.getFriendMaxhps()
                                          .get(i - 1) / 2) {
                text.setText("中破");
                //オレンジ
                text.setTextColor(Color.rgb(255, 140, 0));
            } else if (ts.getFriendNowhps()
                         .get(i - 1) <= ts.getFriendMaxhps()
                                          .get(i - 1) * 3 / 4) {
                text.setText("小破");
                //黄色
                text.setTextColor(Color.rgb(255, 230, 30));
            } else if (ts.getFriendNowhps()
                         .get(i - 1) < ts.getFriendMaxhps()
                                         .get(i - 1)) {
                text.setText("健在");
                //ライム
                text.setTextColor((Color.rgb(153, 204, 0)));
            } else {
                text.setText("無傷");
                //緑
                text.setTextColor(Color.rgb(59, 175, 117));
            }
            
            name = "beforeHp" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            text.setText(ts.getFriendHpBeforeBattle()
                           .get(i - 1) + "/" + myShip.getMaxhp() + "→");
            
            name = "hp" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            text.setText(ts.getFriendNowhps()
                           .get(i - 1) + "/" + ts.getFriendMaxhps()
                                                 .get(i - 1));
            
            name = "damage" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            int damage = ts.getFriendNowhps()
                           .get(i - 1) - ts.getFriendHpBeforeBattle()
                                           .get(i - 1);
            text.setText(String.valueOf(damage));
            if (damage == 0) {
                //緑
                text.setTextColor(Color.rgb(59, 175, 117));
            } else {
                //オーシャンブルー
                text.setTextColor(Color.rgb(0, 102, 204));
            }
        }
        
        //第2艦隊
        for (int i = 1; i <= 6; i++) {
            if (i > ts.getFriendShipIdCombined()
                      .size()) {
                String name = "cName" + i;
                int strId = res.getIdentifier(name, "id", packageName);
                TextView text = ButterKnife.findById(rootView, strId);
                text.setVisibility(View.GONE);
                
                name = "cLv" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setVisibility(View.GONE);
                
                name = "cState" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setVisibility(View.GONE);
                
                name = "cBeforeHp" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setVisibility(View.GONE);
                
                name = "cHp" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setVisibility(View.GONE);
                
                name = "cDamage" + i;
                strId = res.getIdentifier(name, "id", packageName);
                text = ButterKnife.findById(rootView, strId);
                text.setVisibility(View.GONE);
                
                continue;
            }
            
            int id = ts.getFriendShipIdCombined()
                       .get(i - 1);
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(id);
            
            String name = "cName" + i;
            int strId = res.getIdentifier(name, "id", packageName);
            TextView text = ButterKnife.findById(rootView, strId);
            text.setVisibility(View.VISIBLE);
            text.setText(myShip.getName());
            
            name = "cLv" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            text.setVisibility(View.VISIBLE);
            text.setText("(Lv" + myShip.getLv() + ")");
            
            name = "cState" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            text.setVisibility(View.VISIBLE);
            if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                text.setText("退避");
                text.setTextColor(Color.LTGRAY);
            } else if (ts.getFriendNowhpsCombined()
                         .get(i - 1) <= 0) {
                text.setText("轟沈");
                //オーシャンブルー
                text.setTextColor(Color.rgb(0, 102, 204));
            } else if (ts.getFriendNowhpsCombined()
                         .get(i - 1) <= ts.getFriendMaxhpsCombined()
                                          .get(i - 1) / 4) {
                text.setText("大破");
                text.setTextColor(Color.RED);
            } else if (ts.getFriendNowhpsCombined()
                         .get(i - 1) <= ts.getFriendMaxhpsCombined()
                                          .get(i - 1) / 2) {
                text.setText("中破");
                //オレンジ
                text.setTextColor(Color.rgb(255, 140, 0));
            } else if (ts.getFriendNowhpsCombined()
                         .get(i - 1) <= ts.getFriendMaxhpsCombined()
                                          .get(i - 1) * 3 / 4) {
                text.setText("小破");
                //黄色
                text.setTextColor(Color.rgb(255, 230, 30));
            } else if (ts.getFriendNowhpsCombined()
                         .get(i - 1) < ts.getFriendMaxhpsCombined()
                                         .get(i - 1)) {
                text.setText("健在");
                //ライム
                text.setTextColor((Color.rgb(153, 204, 0)));
            } else {
                text.setText("無傷");
                //緑
                text.setTextColor(Color.rgb(59, 175, 117));
            }
            
            name = "cBeforeHp" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            text.setVisibility(View.VISIBLE);
            text.setText(ts.getFriendHpBeforeBattleCombined()
                           .get(i - 1) + "/" + myShip.getMaxhp() + "→");
            
            name = "cHp" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            text.setVisibility(View.VISIBLE);
            text.setText(ts.getFriendNowhpsCombined()
                           .get(i - 1) + "/" + ts.getFriendMaxhpsCombined()
                                                 .get(i - 1));
            
            name = "cDamage" + i;
            strId = res.getIdentifier(name, "id", packageName);
            text = ButterKnife.findById(rootView, strId);
            int damage = ts.getFriendNowhpsCombined()
                           .get(i - 1) - ts.getFriendHpBeforeBattleCombined()
                                           .get(i - 1);
            text.setVisibility(View.VISIBLE);
            text.setText(String.valueOf(damage));
            if (damage == 0) {
                //緑
                text.setTextColor(Color.rgb(59, 175, 117));
            } else {
                //オーシャンブルー
                text.setTextColor(Color.rgb(0, 102, 204));
            }
        }
    
        ImageButton closeButton = ButterKnife.findById(rootView, R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        
        params = new WindowManager.LayoutParams(displayWidth / 2, ViewGroup.LayoutParams.MATCH_PARENT, -displayWidth / 2, -displayHeight / 2, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.OPAQUE);
        wm.addView(rootView, params);
    }
}
