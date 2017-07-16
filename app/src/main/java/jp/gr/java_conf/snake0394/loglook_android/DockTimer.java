package jp.gr.java_conf.snake0394.loglook_android;

import android.app.NotificationManager;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;


/**
 * 入渠タイマー
 */
public enum DockTimer {
    INSTANCE;

    /**
     * ドック毎のタイマー
     */
    private enum Timer {
        DOCK1(1),
        DOCK2(2),
        DOCK3(3),
        DOCK4(4),
        DOCK5(5),
        NULL(0);

        /**
         * ドックID
         */
        private int dockId = 0;

        /**
         * 入渠している艦の艦船固有iD
         */
        private int shipId = 0;

        /**
         * 入渠完了時刻(エポックミリ秒)
         */
        private long completeTime = 0;

        /**
         * タイマーが稼働中か
         */
        private boolean isRunning = false;

        Timer(int dockId) {
            this.dockId = dockId;
        }

        private void start(Context context, int shipId, long completeTime) {
            if (this == Timer.NULL) {
                return;
            }
            if (!new GeneralPrefs(context).getUsesDockNotification()) {
                return;
            }

            this.shipId = shipId;
            //入渠は一分前に完了する
            completeTime -= TimeUnit.MINUTES.toMillis(1);
            this.completeTime = completeTime;

            //通知を登録する。通知IDは dockId + 10
            NotificationUtility.setNotification(context, "入渠完了", "入渠完了", MyShipManager.INSTANCE.getMyShip(shipId).getName(), dockId + 10, completeTime);
            isRunning = true;
        }

        private void stop() {
            shipId = 0;
            isRunning = false;
        }
    }

    /**
     * 準備完了状態のタイマーを稼働させる
     *
     * @param context
     * @param completeTime 遠征完了時刻
     */
    public void startTimer(Context context, int dockId, int shipId, long completeTime) {
        Timer dock = getDock(dockId);
        dock.start(context, shipId, completeTime);
    }

    /**
     * 指定された艦隊のisRunningをfalseにする
     *
     * @param dockId 艦隊ID
     */
    public void cancel(int dockId) {
        Timer dock = getDock(dockId);
        dock.stop();
        NotificationUtility.cancelNotification(App.getInstance(), dockId + 10);
    }

    /**
     * 指定された艦隊のタイマーが作動中か
     *
     * @param dockId 確認するタイマーの艦隊ID
     * @return 指定された艦隊のタイマーが作動中かどうか
     */
    public boolean isRunning(int dockId) {
        Timer dock = getDock(dockId);
        return dock.isRunning;
    }

    public int getShipId(int dockId) {
        Timer dock = getDock(dockId);
        return dock.shipId;
    }

    public long getCompleteTime(int dockId) {
        Timer dock = getDock(dockId);
        return dock.completeTime;
    }

    private Timer getDock(int dockId) {
        switch (dockId) {
            case 1:
                return Timer.DOCK1;
            case 2:
                return Timer.DOCK2;
            case 3:
                return Timer.DOCK3;
            case 4:
                return Timer.DOCK4;
            case 5:
                return Timer.DOCK5;
            default:
                return Timer.NULL;
        }
    }

    public void clearNotifications() {
        NotificationManager nm = (NotificationManager) App.getInstance()
                                                          .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(11);
        nm.cancel(12);
        nm.cancel(13);
        nm.cancel(14);
    }
}
