package jp.gr.java_conf.snake0394.loglook_android;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Objects;

import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import uk.co.senab.photoview.PhotoViewAttacher;


public class DeckListOverlayService extends Service {
    
    private static final String TAG = "DeckListOverlayService";
    
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    
    private int displayWidth;
    private int displayHeight;
    
    RelativeLayout rootLayout;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Logger.d(TAG, "onCreate");
        
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        wm.getDefaultDisplay()
          .getMetrics(metrics);
        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;
    }
    
    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        Logger.d(TAG, "onStartCommand");
        
        if(!Objects.equals(rootLayout, null)){
            wm.removeViewImmediate(rootLayout);
        }
        
        //タッチイベントを取得するためのviewを作る
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        rootLayout = (RelativeLayout) layoutInflater.inflate(R.layout.layout_deck_list_overlay, null);
        
        params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, -(displayWidth / 2), -displayHeight / 2, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        
        ImageButton closeButton = ButterKnife.findById(rootLayout, R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        
        Bitmap image = BitmapFactory.decodeFile(intent.getStringExtra("uri"));
        Logger.d(TAG, "uri:" + intent.getStringExtra("uri"));
        if (Objects.equals(image, null)) {
            Logger.d(TAG, "image is null");
            stopSelf();
            return START_NOT_STICKY;
        }
        ImageView preview = ButterKnife.findById(rootLayout, R.id.imageView);
        preview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        preview.setImageBitmap(image);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(preview);
        mAttacher.setMaximumScale(3f);
        mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        final ImageView dragHandle = ButterKnife.findById(rootLayout, R.id.view_drag_handle);
        dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getRawX();
                int y = (int) motionEvent.getRawY();
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    wm.getDefaultDisplay()
                      .getMetrics(metrics);
                    displayWidth = metrics.widthPixels;
                    displayHeight = metrics.heightPixels;
                    
                    int centerX = x - (displayWidth / 2);
                    int centerY = y - (displayHeight / 2);
                    
                    params.x = centerX + rootLayout.getWidth() / 2 - dragHandle.getWidth();
                    params.y = centerY + rootLayout.getHeight() / 2 - dragHandle.getHeight();
                    
                    wm.updateViewLayout(rootLayout, params);
                }
                return false;
            }
        });
        
        wm.addView(rootLayout, params);
        
        return START_NOT_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeViewImmediate(rootLayout);
    }
    
}
