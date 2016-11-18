package jp.gr.java_conf.snake0394.loglook_android;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;


/**
 * アプリを機動するランチャー
 */
public class SlantLauncher extends Service implements SensorEventListener {
    private boolean isTouching = false; //指定箇所がタップされているか
    private SensorManager sm;
    private WindowManager wm;
    private SharedPreferences sp;
    //タップ検出領域
    private View v;
    //加速度センサの値から重力を取り除くのに使用
    final float alpha = (float) 0.8;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    private boolean isUsing = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SlantLauncher", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        Log.d("SlantLauncher", "onStartCommand");

        //センサ情報を取得する
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensors = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, sensors, SensorManager.SENSOR_DELAY_NORMAL);

        //タッチイベントを取得するためのviewを作る
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        v = new View(this);
        //検出領域の透明度を設定
        int transparent;
        if (sp.getBoolean("showView", true)) {
            //不透明
            transparent = PixelFormat.OPAQUE;
            //黄色
            v.setBackgroundColor(0xffffff00);
        } else {
            //透明
            transparent = PixelFormat.TRANSPARENT;
        }
        Point point = getDisplaySize();
        //座標
        //初期位置は、端末を横にした時左上
        int viewX = sp.getInt("viewX", point.x / -2);
        int viewY = sp.getInt("viewY", point.y / -2);
        //大きさ
        int viewWidth = sp.getInt("viewWidth", 20);
        int viewHeight = sp.getInt("viewHeight", 50);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(viewWidth, viewHeight, viewY, viewX, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, transparent);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //タップされた
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isTouching = true;

                    //振動させる
                    if (sp.getBoolean("touchVibration", true)) {
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(30);
                    }

                    //指が画面から離れた
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isTouching = false;
                }
                return true;
            }
        });
        wm.addView(v, params);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(this);
        wm.removeView(v);
        //Log.d("SlantLauncher", "onDestroy");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //加速度センサの値から重力の影響を取り除く
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        //gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        //linear_acceleration[2] = event.values[2] - gravity[2];
        //Log.d("liner",String.valueOf(linear_acceleration[0])+","+String.valueOf(linear_acceleration[1])+","+String.valueOf(linear_acceleration[2]));
        if (isUsing) {
            return;
        }
        //画面が指定箇所に触れている場合処理を行う
        if (isTouching) {
            //艦これがフォアグラウンドの場合
            if (isForeground()) {
                //設定により左右どちらに振ったとき発動するか区別
                //処理の重複を避けるため、スレッドを使用
                if (linear_acceleration[1] < -1) {
                    new Thread() {
                        @Override
                        public void run() {
                            isUsing = true;
                            Intent intent = new Intent(SlantLauncher.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("usesLandscape", true);
                            intent.putExtra("position", MainActivity.Fragment.DECK.getPosition());
                            startActivity(intent);
                            try {
                                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isUsing = false;
                            isTouching = false;
                        }
                    }.start();
                } else if (linear_acceleration[1] > 1) {
                    new Thread() {
                        @Override
                        public void run() {
                            isUsing = true;
                            Intent intent = new Intent(SlantLauncher.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("usesLandscape", true);
                            intent.putExtra("position", MainActivity.Fragment.TACTICAL_SITUATION.getPosition());
                            startActivity(intent);
                            try {
                                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isUsing = false;
                            isTouching = false;
                        }
                    }.start();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //画面のサイズを求める
    private Point getDisplaySize() {
        Display display = wm.getDefaultDisplay();
        Point real = new Point(0, 0);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                // Android 4.2以上
                display.getRealSize(real);
                return real;

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                // Android 3.2以上
                Method getRawWidth = Display.class.getMethod("getRawWidth");
                Method getRawHeight = Display.class.getMethod("getRawHeight");
                int width = (Integer) getRawWidth.invoke(display);
                int height = (Integer) getRawHeight.invoke(display);
                real.set(width, height);
                return real;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        return real;
    }

    //艦これがフォアグラウンドか確認する
    private boolean isForeground() {
        String packageName = "com.dmm.dmmlabo.kancolle";
        boolean isForeGround = false;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processInfoList) {
            if (info.processName.contains(packageName)) {
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    isForeGround = true;
                }
            }
        }
        return isForeGround;
    }
}
