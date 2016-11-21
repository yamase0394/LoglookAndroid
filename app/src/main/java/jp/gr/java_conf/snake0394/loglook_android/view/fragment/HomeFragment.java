package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.SlantLauncher;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ProxyServerService;
import jp.gr.java_conf.snake0394.loglook_android.R;


public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        ToggleButton tb = (ToggleButton) getActivity().findViewById(R.id.toggleButton);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
                    boolean isProxyServerRunnning = false;
                    boolean isMyServiceRunnning = false;
                    for (ActivityManager.RunningServiceInfo curr : listServiceInfo) {
                        // クラス名を比較
                        if (curr.service.getClassName().equals(ProxyServerService.class.getName())) {
                            // 実行中のサービスと一致
                            isProxyServerRunnning = true;
                        }
                        if (curr.service.getClassName().equals(SlantLauncher.class.getName())) {
                            // 実行中のサービスと一致
                            isMyServiceRunnning = true;
                        }
                    }
                    if (!isProxyServerRunnning || !isMyServiceRunnning) {
                        Intent intent = new Intent(getActivity(), ProxyServerService.class);
                        getActivity().startService(intent);
                        intent = new Intent(getActivity(), SlantLauncher.class);
                        getActivity().startService(intent);
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        Toast.makeText(getActivity(), "起動 port:" + sp.getString("port", "8080"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), ProxyServerService.class);
                    getActivity().stopService(intent);
                    intent = new Intent(getActivity(), SlantLauncher.class);
                    getActivity().stopService(intent);
                    Toast.makeText(getActivity(), "停止", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tb.setChecked(true);
    }
}
