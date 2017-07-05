package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.OverlayService;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.SlantLauncher;
import jp.gr.java_conf.snake0394.loglook_android.proxy.LittleProxyServerService;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;


public class HomeFragment extends Fragment {

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

    @Override
    public void onStart() {
        super.onStart();

        final ToggleButton tb = (ToggleButton) getActivity().findViewById(R.id.toggleButton);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                getActivity().stopService(new Intent(getActivity().getApplicationContext(), LittleProxyServerService.class));
                getActivity().stopService(new Intent(getActivity(), SlantLauncher.class));
                getActivity().stopService(new Intent(getActivity().getApplicationContext(), OverlayService.class));

                if (!isChecked) {
                    return;
                }

                if (!MainActivity.canGetUsageStats(getContext())) {
                    new AlertDialog.Builder(getActivity()).setTitle("権限の説明")
                            .setMessage("艦これがフォアグラウンドで起動しているか検知するために\"使用状況へのアクセス\"の権限が必要です")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .create()
                            .show();

                    tb.setChecked(false);
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Android6以降の端末でランチャーのオーバーレイ用の権限を取得する
                    if (!Settings.canDrawOverlays(getContext())) {
                        new AlertDialog.Builder(getActivity()).setTitle("権限の説明")
                                .setMessage("ランチャー、スクリーンショットを使用するために\"他のアプリに重ねて表示\"の権限が必要です")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
                                        startActivity(intent);
                                    }
                                })
                                .create()
                                .show();
                        tb.setChecked(false);
                        return;
                    }

                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        new AlertDialog.Builder(getActivity()).setTitle("権限の説明")
                                .setMessage("スクリーンショット、戦闘ログの記録を行うためにに\"外部記憶領域への書き込み\"の権限が必要です")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                    }
                                })
                                .create()
                                .show();
                        tb.setChecked(false);
                        return;
                    }
                }

                getActivity().startService(new Intent(getActivity().getApplicationContext(), OverlayService.class));
                getActivity().startService(new Intent(getActivity().getApplicationContext(), LittleProxyServerService.class));
                getActivity().startService(new Intent(getActivity(), SlantLauncher.class));
            }
        });

        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
        boolean isProxyServerRunnning = false;
        boolean isMyServiceRunnning = false;
        for (ActivityManager.RunningServiceInfo curr : listServiceInfo) {
            // クラス名を比較
            if (curr.service.getClassName()
                    .equals(LittleProxyServerService.class.getName())) {
                // 実行中のサービスと一致
                isProxyServerRunnning = true;
            }
            if (curr.service.getClassName()
                    .equals(SlantLauncher.class.getName())) {
                // 実行中のサービスと一致
                isMyServiceRunnning = true;
            }
        }
        tb.setChecked(isProxyServerRunnning && isMyServiceRunnning);
    }


}
