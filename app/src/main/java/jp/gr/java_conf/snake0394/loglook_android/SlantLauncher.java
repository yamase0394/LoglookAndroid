package jp.gr.java_conf.snake0394.loglook_android;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.DialogActivity;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ScreenCaptureActivity;


/**
 * アプリを起動するランチャー
 */
public class SlantLauncher extends Service implements SensorEventListener {
    private static final String TAG = "SlantLauncher";

    private boolean isTouching = false; //指定箇所がタップされているか
    private SensorManager sm;
    private GeneralPrefs prefs;
    //タップ検出領域
    private View v;
    //加速度センサの値から重力を取り除くのに使用
    final float alpha = (float) 0.8;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("SlantLauncher", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        Logger.d("SlantLauncher", "onStartCommand");

        //センサ情報を取得する
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensors = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, sensors, SensorManager.SENSOR_DELAY_NORMAL);

        //関連するセンサを動作させないと加速度が正しく取れないことがある
        final Sensor ori = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sm.registerListener(this, ori, SensorManager.SENSOR_DELAY_NORMAL);
        final Sensor mag = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL);

        this.prefs = new GeneralPrefs(getApplicationContext());

        v = new View(this);
        //検出領域の透明度を設定
        int transparent;
        if (prefs.getShowsView()) {
            //不透明
            transparent = PixelFormat.OPAQUE;
            v.setBackgroundColor(prefs.getViewColor());
        } else {
            //透明
            transparent = PixelFormat.TRANSPARENT;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(prefs.getViewWidth(), prefs.getViewHeight(), prefs.getViewY(), prefs.getViewX(), WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, transparent);
        v.setOnTouchListener(new FlickTouchListener(getApplicationContext()));
        OverlayService.addOverlayView(v,params);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(this);
        OverlayService.removeOverlayView(v);
        Logger.d("SlantLauncher", "onDestroy");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        //加速度センサの値から重力の影響を取り除く
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        //gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        //linear_acceleration[2] = event.values[2] - gravity[2];
        //Log.d("liner",String.valueOf(linear_acceleration[0])+","+String.valueOf(linear_acceleration[1])+","+String.valueOf(linear_acceleration[2]));

        if (!isTouching) {
            return;
        }

        if (linear_acceleration[1] < -1) {
            //左に傾けた
            isTouching = false;

            Intent intent = new Intent(SlantLauncher.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("usesLandscape", true);
            intent.putExtra("position", MainActivity.Screen.DECK.getPosition());
            startActivity(intent);
        } else if (linear_acceleration[1] > 1) {
            //右に傾けた
            isTouching = false;

            Intent intent = new Intent(SlantLauncher.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("usesLandscape", true);
            intent.putExtra("position", MainActivity.Screen.TACTICAL_SITUATION.getPosition());
            startActivity(intent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //艦これがフォアグラウンドか確認する
    private boolean isForeground() {
        String packageName = "com.dmm.dmmlabo.kancolle";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<ComponentName> list = getForegroundAppList(1);
            for (ComponentName name : list) {
                if (name.getPackageName()
                        .equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processInfoList) {
            if (info.processName.contains(packageName)) {
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<ComponentName> getForegroundAppList(int size) {
        List<ComponentName> nameList = new ArrayList<>();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return nameList;
        }

        long time = System.currentTimeMillis();
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long interval = 10 * 1000;
        do {
            UsageEvents events = usm.queryEvents(time - interval, time);
            while (events.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                if (events.getNextEvent(event)) {
                    // eventTypeがMOVE_TO_FOREGROUNDのものだけ取ったらgetRunningTasksっぽかったのでフィルタしています。
                    // 古い順に取得されるので先頭から追加して新しい順に直しています。
                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        // 使いやすいようにcomponentNameに変えています。
                        String packageName = event.getPackageName();
                        ComponentName name = new ComponentName(packageName, event.getClassName());
                        // リストにの先頭に追加する
                        nameList.add(0, name);
                    }
                }
            }
            interval *= 10;
        }
        // 指定した数以上、foregroundのイベントが取得できるまで続けてます。
        // 無限ループになりそうなので10万秒くらいを上限にしています。これでも大きすぎる気がします。
        while (nameList.size() < size || interval > 100000 * 1000);
        return nameList;
    }

    private class FlickTouchListener implements View.OnTouchListener {
        // 最後にタッチされた座標
        private float startTouchX;
        private float startTouchY;

        // 現在タッチ中の座標
        private float nowTouchedX;
        private float nowTouchedY;

        // フリックの遊び部分（最低限移動しないといけない距離）
        private float adjust = 120;

        private Context context;

        public FlickTouchListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onTouch(View v_, MotionEvent event_) {
            // タッチされている指の本数
            //Log.v("motionEvent", "--touch_count = " + event_.getPointerCount());

            if (!isForeground()) {
                if (event_.getAction() == MotionEvent.ACTION_DOWN) {
                    isTouching = false;
                }
                return true;
            }

            // タッチされている座標
            //Log.v("Y", "" + event_.getY());
            //Log.v("X", "" + event_.getX());

            switch (event_.getAction()) {
                // タッチ
                case MotionEvent.ACTION_DOWN:
                    //Log.v("motionEvent", "--ACTION_DOWN");
                    startTouchX = event_.getX();
                    startTouchY = event_.getY();

                    isTouching = true;

                    //振動させる
                    if (prefs.getVibratesWhenViewTouched()) {
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(30);
                    }
                    break;

                // タッチ中に追加でタッチした場合
                case MotionEvent.ACTION_POINTER_DOWN:
                    //Log.v("motionEvent", "--ACTION_POINTER_DOWN");
                    break;

                // スライド
                case MotionEvent.ACTION_MOVE:
                    //Log.v("motionEvent", "--ACTION_MOVE");
                    break;

                // タッチが離れた
                case MotionEvent.ACTION_UP:
                    if (!isTouching) {
                        //傾きランチャーが起動している
                        return true;
                    }

                    //Log.v("motionEvent", "--ACTION_UP");
                    nowTouchedX = event_.getX();
                    nowTouchedY = event_.getY();

                    isTouching = false;

                    //上
                    if (startTouchY > nowTouchedY) {
                        //左
                        if (startTouchX > nowTouchedX) {
                            if ((startTouchY - nowTouchedY) > (startTouchX - nowTouchedX)) {
                                if (startTouchY > nowTouchedY + adjust) {
                                    //Log.v("Flick", "左上上");
                                    // 上フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            } else if ((startTouchY - nowTouchedY) < (startTouchX - nowTouchedX)) {
                                if (startTouchX > nowTouchedX + adjust) {
                                    //Log.v("Flick", "左上左");
                                    // 左フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            }
                            //右
                        } else if (startTouchX < nowTouchedX) {
                            if ((startTouchY - nowTouchedY) > (nowTouchedX - startTouchX)) {
                                if (startTouchY > nowTouchedY + adjust) {
                                    //Log.v("Flick", "右上上");
                                    // 上フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            } else if ((startTouchY - nowTouchedY) < (nowTouchedX - startTouchX)) {
                                if (startTouchX + adjust < nowTouchedX) {
                                    //Log.v("Flick", "右上右");
                                    // 右フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            }
                        }
                        //下
                    } else if (startTouchY < nowTouchedY) {
                        //左
                        if (startTouchX > nowTouchedX) {
                            if ((nowTouchedY - startTouchY) > (startTouchX - nowTouchedX)) {
                                if (startTouchY + adjust < nowTouchedY) {
                                    //Log.v("Flick", "左下下");
                                    // 下フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            } else if ((nowTouchedY - startTouchY) < (startTouchX - nowTouchedX)) {
                                if (startTouchX > nowTouchedX + adjust) {
                                    //Log.v("Flick", "左下左");
                                    // 左フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            }
                            //右
                        } else if (startTouchX < nowTouchedX) {
                            if ((nowTouchedY - startTouchY) > (nowTouchedX - startTouchX)) {
                                if (startTouchY + adjust < nowTouchedY) {
                                    //Log.v("Flick", "右下下");
                                    // 下フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            } else if ((nowTouchedY - startTouchY) < (nowTouchedX - startTouchX)) {
                                if (startTouchX + adjust < nowTouchedX) {
                                    //Log.v("Flick", "右下右");
                                    // 右フリック時の処理を記述する
                                    this.showLauncher();
                                }
                            }
                        }
                    }
                    break;

                // アップ後にほかの指がタッチ中の場合
                case MotionEvent.ACTION_POINTER_UP:
                    //Log.v("motionEvent", "--ACTION_POINTER_UP");
                    break;

                // UP+DOWNの同時発生(タッチのキャンセル）
                case MotionEvent.ACTION_CANCEL:
                    //Log.v("motionEvent", "--ACTION_CANCEL");

                    // ターゲットとするUIの範囲外を押下
                case MotionEvent.ACTION_OUTSIDE:
                    //Log.v("motionEvent", "--ACTION_OUTSIDE");
                    break;
            }
            return true;
        }

        private void showLauncher() {
            /*
            Intent i = new Intent(context, DialogActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.showActivity(i);
            */

            final View launcherLayout = View.inflate(getApplicationContext(), R.layout.layout_launcher, null);
            launcherLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OverlayService.removeOverlayView(launcherLayout);
                }
            });

            ListView screenListview = ButterKnife.findById(launcherLayout, R.id.list_screen);
            String[] launcherItems = new String[]{"艦隊", "遠征", "入渠", "損傷艦", "艦娘一覧", "装備一覧", "戦況", "編成一覧"};
            CustomAdapter customAdapter = new CustomAdapter(context, 0, launcherItems);
            screenListview.setAdapter(customAdapter);
            screenListview.setDivider(null);
            screenListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //そのまま支援アプリを起動すると艦これがブラックアウトするため透明なアクティビティを間に挟む
                    OverlayService.removeOverlayView(launcherLayout);
                    Intent intent = new Intent(context, DialogActivity.class);
                    intent.putExtra("usesLandscape", true);
                    intent.putExtra("position", MainActivity.Screen.toScreen((String) adapterView.getItemAtPosition(i))
                            .getPosition());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    context.startActivity(intent);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                screenListview = ButterKnife.findById(launcherLayout, R.id.list_sub);
                launcherItems = new String[]{"スクリーンショット", "編成記録"};
                customAdapter = new CustomAdapter(context, 0, launcherItems);
                screenListview.setAdapter(customAdapter);
                screenListview.setDivider(null);
                screenListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        OverlayService.removeOverlayView(launcherLayout);
                        Intent intent = new Intent(context, ScreenCaptureActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        switch (i) {
                            case 0:
                                intent.putExtra("class", ScreenShotService.class.getSimpleName());
                                break;
                            case 1:
                                intent.putExtra("class", DeckListCaptureService.class.getSimpleName());
                                break;
                        }
                        context.startActivity(intent);
                    }
                });
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
            OverlayService.addOverlayView(launcherLayout, params);
        }

        class CustomAdapter extends ArrayAdapter<String> {

            class ViewHolder {
                TextView labelText;
            }

            private LayoutInflater inflater;

            // コンストラクタ
            public CustomAdapter(Context context, int textViewResourceId, String[] labelList) {
                super(context, textViewResourceId, labelList);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder holder;
                View view = convertView;

                // Viewを再利用している場合は新たにViewを作らない
                if (view == null) {
                    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.layout_launcher_item, null);
                    TextView label = (TextView) view.findViewById(R.id.name);
                    holder = new ViewHolder();
                    holder.labelText = label;
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }

                // 特定の行のデータを取得
                String str = getItem(position);

                if (!TextUtils.isEmpty(str)) {
                    // テキストビューにラベルをセット
                    holder.labelText.setText(str);
                }

                /*
                // 行毎に背景色を変える
                if (position % 2 == 0) {
                    holder.labelText.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                } else {
                    holder.labelText.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }
                */

                // XMLで定義したアニメーションを読み込む
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_from_left);
                // リストアイテムのアニメーションを開始
                view.startAnimation(anim);

                return view;
            }
        }
    }

}
