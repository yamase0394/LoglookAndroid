package jp.gr.java_conf.snake0394.loglook_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by snake0394 on 2016/08/09.
 */
public class NotificationUtility {

    /**
     * NotifierのonReceiveを呼び出すアラームをセットします
     *
     * @param context  呼び出し元のContext
     * @param ticker   通知時ステータスバーに表示される文字（バージョンによっては表示されない）
     * @param title    通知のタイトル
     * @param text     通知の本文
     * @param id       通知とアラームのID
     * @param interval 通知を行う時刻。エポックミリ秒
     */
    public static void setNotification(Context context, String ticker, String title, String text, int id, long interval) {
        Intent intent = new Intent(context, Notifier.class);
        intent.putExtra("ticker", ticker);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        intent.putExtra("id", id);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, interval, sender);
    }

    /**
     * セットされているアラームを解除します
     *
     * @param context 呼び出し元のContext
     * @param id      setNotificationで登録されたID
     */
    public static void cancelNotification(Context context, int id) {
        Intent intent = new Intent(context, Notifier.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        MissionTimer.INSTANCE.stop(id);
    }


}
