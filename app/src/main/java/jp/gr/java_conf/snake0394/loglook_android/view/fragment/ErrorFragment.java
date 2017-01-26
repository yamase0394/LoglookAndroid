package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jp.gr.java_conf.snake0394.loglook_android.R;


public class ErrorFragment extends Fragment {

    public ErrorFragment() {
        // Required empty public constructor
    }


    public static ErrorFragment newInstance() {
        ErrorFragment fragment = new ErrorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button btn = (Button) getActivity().findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = "com.dmm.dmmlabo.kancolle";
                PackageManager pm = getActivity().getPackageManager();
                Intent sendIntent = pm.getLaunchIntentForPackage(packageName);
                startActivity(sendIntent);
            }
        });
    }
}
