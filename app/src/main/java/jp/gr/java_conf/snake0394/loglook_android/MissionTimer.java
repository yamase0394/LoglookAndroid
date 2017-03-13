package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMissionManager;

/**
 * 遠征タイマー
 */
public enum MissionTimer {
    INSTANCE;

    /**
     * 艦隊毎のタイマー
     * 第五艦隊が開放される日は来るのだろうか...
     */
    private enum Timer {
        DECK2(2),
        DECK3(3),
        DECK4(4),
        DECK5(5),
        NULL(0);

        private int deckId;
        private int missionId;

        private boolean isRunning = false;

        Timer(int deckId) {
            this.deckId = deckId;
        }

        private void startTimer(Context context, long completeTime) {
            if (this == Timer.NULL) {
                return;
            }

            //Deckに遠征情報を反映
            Deck deck = DeckManager.INSTANCE.getDeck(deckId);
            List<Long> mission = deck.getMission();
            mission.set(0, (long) 1);
            mission.set(1, (long) missionId);
            mission.set(2, completeTime);

            //遠征は一分前に完了する
            completeTime -= TimeUnit.MINUTES.toMillis(1);

            MstMission mstMission = MstMissionManager.INSTANCE.getMstMission(missionId);

            //通知をセット。通知IDはdeckId
            NotificationUtility.setNotification(context, "遠征完了", "遠征完了", mstMission.getName(), deckId, completeTime);
            MissionTimer.INSTANCE.setReadeyDeck(Timer.NULL);
            isRunning = true;
        }

        private void setMissionId(int missionId) {
            this.missionId = missionId;
        }

        private void stop() {
            isRunning = false;
            Deck deck = DeckManager.INSTANCE.getDeck(deckId);
            deck.getMission().set(0, (long) 2);
        }
    }

    /**
     * 準備完了状態の艦隊の遠征タイマー
     */
    private Timer readeyDeck = Timer.NULL;

    /**
     * 指定された艦隊の遠征タイマーを準備完了状態にする
     *
     * @param deckId    準備完了状態にする艦隊ID
     * @param missionId deckIdの艦隊が出撃する遠征
     */
    public void ready(int deckId, int missionId) {
        Timer timer = getTimer(deckId);
        readeyDeck = timer;
        timer.setMissionId(missionId);
    }

    /**
     * {@link #ready(int, int)}で準備完了状態にされたタイマーを稼働させる
     *
     * @param context
     * @param completeTime 遠征完了時刻
     */
    public void startTimer(Context context, long completeTime) {
        readeyDeck.startTimer(context, completeTime);
    }

    /**
     * 指定された艦隊のisRunningをfalseにする
     *
     * @param deckId 艦隊ID
     */
    public void stop(int deckId) {
        Timer timer = getTimer(deckId);
        timer.stop();
    }

    /**
     * 指定された艦隊のタイマーが作動中か
     *
     * @param deckId 確認するタイマーの艦隊ID
     * @return 指定された艦隊のタイマーが作動中かどうか
     */
    public boolean isRunning(int deckId) {
        Timer timer = getTimer(deckId);
        return timer.isRunning;
    }

    private void setReadeyDeck(Timer readeyDeck) {
        this.readeyDeck = readeyDeck;
    }

    private Timer getTimer(int deckId) {
        switch (deckId) {
            case 2:
                return Timer.DECK2;
            case 3:
                return Timer.DECK3;
            case 4:
                return Timer.DECK4;
            case 5:
                return Timer.DECK5;
            default:
                return Timer.NULL;
        }
    }

}
