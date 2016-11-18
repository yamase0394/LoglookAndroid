package jp.gr.java_conf.snake0394.loglook_android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by snake0394 on 2016/08/09.
 */
public class Notifier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //通知タップ時の動作。艦これのアプリを開く
        String packageName = "com.dmm.dmmlabo.kancolle";
        PackageManager pm = context.getPackageManager();
        Intent sendIntent = pm.getLaunchIntentForPackage(packageName);
        PendingIntent sender = PendingIntent.getActivity(context, 0, sendIntent, 0);

        //通知生成
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.noti).setTicker(intent.getStringExtra("ticker")).setContentTitle(intent.getStringExtra("title")).setContentText(intent.getStringExtra("text")).setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS).setContentIntent(sender);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(intent.getIntExtra("id", 0), builder.build());

        //トーストを表示
        Toast.makeText(context, "<" + intent.getStringExtra("title") + ">\n" + intent.getStringExtra("text"), Toast.LENGTH_LONG).show();

        //タイマーを停止状態にする
        if (intent.getIntExtra("id", 0) > 10) {
            DockTimer.INSTANCE.stop(intent.getIntExtra("id", 0) - 10);
        } else {
            MissionTimer.INSTANCE.stop(intent.getIntExtra("id", 0));
        }
    }
}


