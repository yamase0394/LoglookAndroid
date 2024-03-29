package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;


public class DockFragment extends Fragment {

    public DockFragment() {
        // Required empty public constructor
    }

    public static DockFragment newInstance() {
        DockFragment fragment = new DockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dock, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //エラー画面の準備
        FragmentManager manager = getChildFragmentManager();
        ErrorFragment fragment = (ErrorFragment) manager.findFragmentById(R.id.fragment4);
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();

        try {
            for (int i = 1; i <= 4; i++) {
                if (!DockTimer.INSTANCE.isRunning(i)) {
                    String name = "name" + i;
                    int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    TextView text = (TextView) getActivity().findViewById(strId);
                    text.setText("");
                    text.setBackgroundColor(0x00000000);

                    name = "time" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "until" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");
                    continue;
                }
                String name = "name" + i;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                TextView text = (TextView) getActivity().findViewById(strId);
                text.setText(MyShipManager.INSTANCE.getMyShip(DockTimer.INSTANCE.getShipId(i))
                                                   .getName());
                text.setBackgroundColor(0x00000000);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                name = "time" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                long finishTimeInMillis = DockTimer.INSTANCE.getCompleteTime(i);
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
                switch (diff) {
                    case 0:
                        text.setText(sdf.format(finishTimeInMillis));
                        break;
                    case 1:
                        text.setText("明日 " + sdf.format(finishTimeInMillis));
                        break;
                    case 2:
                        text.setText("明後日 " + sdf.format(finishTimeInMillis));
                        break;
                    case 3:
                        text.setText("明々後日 " + sdf.format(finishTimeInMillis));
                        break;
                    default:
                        text.setText(diff + "日後 " + sdf.format(finishTimeInMillis));
                        break;
                }

                name = "until" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                long millsInFuture = DockTimer.INSTANCE.getCompleteTime(i) - System.currentTimeMillis();
                text = (TextView) getActivity().findViewById(strId);
                CountDown countDown = new CountDown(millsInFuture, 100, text, i);
                countDown.start();
            }
        } catch (Exception e) {
            fragment = (ErrorFragment) manager.findFragmentById(R.id.fragment4);
            transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }
    }

    private class CountDown extends CountDownTimer {
        TextView text;
        int dockId;

        public CountDown(long millisInFuture, long countDownInterval, TextView text, int dockId) {
            super(millisInFuture, countDownInterval);
            this.text = text;
            this.dockId = dockId;
        }

        @Override
        public void onFinish() {
            // 完了
            if (isAdded()) {
                text.setText("");

                String name = "name" + dockId;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                TextView text = (TextView) getActivity().findViewById(strId);
                text.setBackgroundColor(Color.rgb(237, 185, 24));

                name = "time" + dockId;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText("");
            }
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を日、分、秒、ミリ秒に分割
            StringBuffer sb = new StringBuffer();
            long day = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
            if (day != 0) {
                sb.append(day + "日");
                millisUntilFinished -= TimeUnit.DAYS.toMillis(day);
            }
            long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
            if (hour != 0) {
                sb.append(hour + "時間");
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hour);
            }
            long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            if (minute != 0) {
                sb.append(minute + "分");
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minute);
            }
            long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
            if (second != 0) {
                sb.append(second + "秒");
            }

            text.setText(sb.toString());
        }
    }
}
