package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;


public class DockFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dock, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                text.setText(MyShipManager.INSTANCE.getMyShip(DockTimer.INSTANCE.getShipId(i)).getName());
                text.setBackgroundColor(0x00000000);

                SimpleDateFormat sdf = new SimpleDateFormat("M月dd日 HH:mm:ss");
                name = "time" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(sdf.format(DockTimer.INSTANCE.getCompleteTime(i)));

                name = "until" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                long millsInFuture = DockTimer.INSTANCE.getCompleteTime(i) - Calendar.getInstance().getTimeInMillis();
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
