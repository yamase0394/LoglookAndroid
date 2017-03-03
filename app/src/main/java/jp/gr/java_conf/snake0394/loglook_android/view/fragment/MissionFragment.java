package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMissionManager;


public class MissionFragment extends Fragment {

    private CountDown countDown2;
    private CountDown countDown3;
    private CountDown countDown4;
    private CountDown countDown5;

    public MissionFragment() {
        // Required empty public constructor
    }

    public static MissionFragment newInstance() {
        MissionFragment fragment = new MissionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mission, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //エラー画面の準備
        FragmentManager manager = getChildFragmentManager();
        ErrorFragment fragment = (ErrorFragment) manager.findFragmentById(R.id.fragment2);
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();

        try {
            //第2～4艦隊の遠征状況を表示する
            for (int i = 2; i <= 4; i++) {
                String name = "deck" + i;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                Deck deck = DeckManager.INSTANCE.getDeck(i);
                TextView text = (TextView) getActivity().findViewById(strId);

                if(deck == null){
                    text.setText("");
                    name = "mission" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");
                    text.setBackgroundColor(0x00000000);

                    name = "time" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "remaining" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    continue;
                }

                text.setText(deck.getName());

                List<Long> mission = deck.getMission();
                if (mission.get(0) == 1 || mission.get(0) == 3) {
                    name = "mission" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    int missionId = Integer.parseInt(mission.get(1).toString());
                    text.setText(MstMissionManager.INSTANCE.getMstMission(missionId).getName());
                    text.setBackgroundColor(0x00000000);


                    name = "time" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    TextView conpTime = (TextView) getActivity().findViewById(strId);
                    long finishTimeInMillis = mission.get(2) - TimeUnit.MINUTES.toMillis(1);
                    Date now = Calendar.getInstance()
                                       .getTime();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(finishTimeInMillis);
                    Date finish = calendar.getTime();
                    int diff = 0;
                    while (!org.apache.commons.lang3.time.DateUtils.isSameDay(now, finish)) {
                        now = DateUtils.addDays(now, 1);
                        diff++;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    switch (diff) {
                        case 0:
                            conpTime.setText(sdf.format(finishTimeInMillis));
                            break;
                        case 1:
                            conpTime.setText("明日 "+ sdf.format(finishTimeInMillis));
                            break;
                        case 2:
                            conpTime.setText("明後日 "+ sdf.format(finishTimeInMillis));
                            break;
                        case 3:
                            conpTime.setText("明々後日 "+ sdf.format(finishTimeInMillis));
                            break;
                        default:
                            conpTime.setText(diff + "日後 "+ sdf.format(finishTimeInMillis));
                            break;
                    }

                    name = "remaining" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    long millsInFuture = mission.get(2) - Calendar.getInstance().getTimeInMillis() - TimeUnit.MINUTES.toMillis(1);
                    text = (TextView) getActivity().findViewById(strId);

                    switch (i) {
                        case 2:
                            try {
                                countDown2.cancel();
                            } catch (Exception e) {
                            }
                            countDown2 = new CountDown(millsInFuture, 100, conpTime, text, i);
                            countDown2.start();
                            break;
                        case 3:
                            try {
                                countDown3.cancel();
                            } catch (Exception e) {
                            }
                            countDown3 = new CountDown(millsInFuture, 100, conpTime, text, i);
                            countDown3.start();
                            break;
                        case 4:
                            try {
                                countDown4.cancel();
                            } catch (Exception e) {
                            }
                            countDown4 = new CountDown(millsInFuture, 100, conpTime, text, i);
                            countDown4.start();
                            break;
                        case 5:
                            try {
                                countDown5.cancel();
                            } catch (Exception e) {
                            }
                            countDown5 = new CountDown(millsInFuture, 100, conpTime, text, i);
                            countDown5.start();
                            break;
                    }
                } else if (mission.get(0) == 2) {
                    name = "mission" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    int missionId = Integer.parseInt(mission.get(1).toString());
                    text.setText(MstMissionManager.INSTANCE.getMstMission(missionId).getName());
                    text.setBackgroundColor(Color.rgb(237, 185, 24));

                    name = "time" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "remaining" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");
                } else {
                    name = "mission" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");
                    text.setBackgroundColor(0x00000000);

                    name = "time" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "remaining" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");
                }
            }
        } catch (Exception e) {
            fragment = (ErrorFragment) manager.findFragmentById(R.id.fragment2);
            transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
            e.printStackTrace();
        }
    }


    private class CountDown extends android.os.CountDownTimer {
        TextView compTime;
        TextView remaining;
        int dockId;

        public CountDown(long millisInFuture, long countDownInterval, TextView compTime, TextView remaining, int dockId) {
            super(millisInFuture, countDownInterval);
            this.compTime = compTime;
            this.remaining = remaining;
            this.dockId = dockId;
        }

        @Override
        public void onFinish() {
            if (isAdded()) {
                // 完了
                remaining.setText("");
                compTime.setText("");

                //任務名に色をつける
                String name = "mission" + dockId;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                remaining = (TextView) getActivity().findViewById(strId);
                remaining.setBackgroundColor(Color.rgb(237, 185, 24));
            }
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisremainingFinished) {
            if (isAdded()) {
                // 残り時間を分、秒、ミリ秒に分割
                StringBuffer sb = new StringBuffer();
                long day = TimeUnit.MILLISECONDS.toDays(millisremainingFinished);
                if (day != 0) {
                    sb.append(day + "日");
                    millisremainingFinished -= TimeUnit.DAYS.toMillis(day);
                }
                long hour = TimeUnit.MILLISECONDS.toHours(millisremainingFinished);
                if (hour != 0) {
                    sb.append(hour + "時間");
                    millisremainingFinished -= TimeUnit.HOURS.toMillis(hour);
                }
                long minute = TimeUnit.MILLISECONDS.toMinutes(millisremainingFinished);
                if (minute != 0) {
                    sb.append(minute + "分");
                    millisremainingFinished -= TimeUnit.MINUTES.toMillis(minute);
                }
                long second = TimeUnit.MILLISECONDS.toSeconds(millisremainingFinished);
                if (second != 0) {
                    sb.append(second + "秒");
                }

                remaining.setText(sb.toString());
            }
        }
    }
}
