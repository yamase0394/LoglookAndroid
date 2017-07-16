package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.gr.java_conf.snake0394.loglook_android.OverlayService;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.SlantLauncher;
import jp.gr.java_conf.snake0394.loglook_android.proxy.LittleProxyServerService;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;
import yuku.ambilwarna.AmbilWarnaDialog;

import static android.content.Context.WINDOW_SERVICE;

public class ConfigFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.port)
    EditText portEditText;
    @BindView(R.id.showViewCheck)
    CheckBox showsViewCheck;
    @BindView(R.id.viewX)
    EditText viewXEditText;
    @BindView(R.id.viewY)
    EditText viewYEditText;
    @BindView(R.id.viewWidth)
    EditText viewWidthEditText;
    @BindView(R.id.viewHeight)
    EditText viewHeightEditText;
    @BindView(R.id.view_color)
    View viewColor;
    @BindView(R.id.touchVibrationCheck)
    CheckBox vibratesWhenViewTouchedCheck;
    @BindView(R.id.useProxyCheck)
    CheckBox usesProxyCheck;
    @BindView(R.id.proxyHost)
    EditText proxyHostEditText;
    @BindView(R.id.proxyPort)
    EditText proxyPortEditText;
    @BindView(R.id.saveJsoncheck)
    CheckBox logsJsonCheck;
    @BindView(R.id.saveRequesstCheck)
    CheckBox logsRequsetCheck;
    @BindView(R.id.usesMissionNotification)
    CheckBox usesMissionNotificationCheck;
    @BindView(R.id.usesDockNotification)
    CheckBox usesDockNotificationCheck;
    @BindView(R.id.makesSoundWhenNotify)
    CheckBox makesSoundWhenNotifyCheck;
    @BindView(R.id.vibratesWhenNotify)
    CheckBox vibratesWhenNotifyCheck;
    @BindView(R.id.forceLandscapeCheck)
    CheckBox forceLandscapeCheck;

    public ConfigFragment() {
        // Required empty public constructor
    }

    public static ConfigFragment newInstance() {
        ConfigFragment fragment = new ConfigFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config, container, false);
        this.unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final GeneralPrefs prefs = new GeneralPrefs(getContext());

        //ポート番号
        portEditText.setText(String.valueOf(prefs.getPort()));

        //検出領域を可視化するか
        if (prefs.getShowsView()) {
            showsViewCheck.setChecked(true);
        } else {
            showsViewCheck.setChecked(false);
        }

        //検出領域のx,y座標
        final Point point = getDisplaySize();
        if (prefs.getViewX() == Short.MAX_VALUE) {
            prefs.setViewX(point.x / -2);
        }
        viewXEditText.setText(String.valueOf(prefs.getViewX()));

        if (prefs.getViewY() == Short.MAX_VALUE) {
            prefs.setViewY(point.y / -2);
        }
        viewYEditText.setText(String.valueOf(prefs.getViewY()));

        //検出領域の大きさ
        viewWidthEditText.setText(String.valueOf(prefs.getViewWidth()));
        viewHeightEditText.setText(String.valueOf(prefs.getViewHeight()));

        //検出領域の色
        viewColor.setBackgroundColor(prefs.getViewColor());

        //検出領域に触れたとき振動させるか
        if (prefs.getVibratesWhenViewTouched()) {
            vibratesWhenViewTouchedCheck.setChecked(true);
        } else {
            vibratesWhenViewTouchedCheck.setChecked(false);
        }

        //プロキシを使用するか
        if (prefs.getUsesProxy()) {
            usesProxyCheck.setChecked(true);
        } else {
            usesProxyCheck.setChecked(false);
        }

        //ホスト名
        proxyHostEditText.setText(prefs.getProxyHost());

        //ポート番号
        proxyPortEditText.setText(String.valueOf(prefs.getProxyPort()));

        //jsonを保存するか
        if (prefs.getLogsJson()) {
            logsJsonCheck.setChecked(true);
        } else {
            logsJsonCheck.setChecked(false);
        }
        logsJsonCheck.setVisibility(View.GONE);

        //リクエストを保存するか
        if (prefs.getLogsRequest()) {
            logsRequsetCheck.setChecked(true);
        } else {
            logsRequsetCheck.setChecked(false);
        }
        logsRequsetCheck.setVisibility(View.GONE);

        usesMissionNotificationCheck.setChecked(prefs.getUsesMissionNotification());
        usesDockNotificationCheck.setChecked(prefs.getUsesDockNotification());
        makesSoundWhenNotifyCheck.setChecked(prefs.getMakesSoundWhenNotify());
        vibratesWhenNotifyCheck.setChecked(prefs.getVibratesWhenNOtify());
        forceLandscapeCheck.setChecked(prefs.getForcesLandscape());

        //設定を保存するボタン
        Button tb = (Button) getActivity().findViewById(R.id.saveBtn);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ポ―ト番号
                try {
                    prefs.setPort(Integer.parseInt(portEditText.getText()
                            .toString()));
                } catch (Exception e) {
                    prefs.setPort(8000);
                    portEditText.setText(String.valueOf(prefs.getPort()));
                }

                //検出領域を可視化するか
                prefs.setShowsView(showsViewCheck.isChecked());

                //検出領域のX座標
                try {
                    prefs.setViewX(Integer.parseInt(viewXEditText.getText()
                            .toString()));
                } catch (Exception e) {
                    prefs.setViewX(point.x / -2);
                    viewXEditText.setText(String.valueOf(prefs.getViewX()));
                }

                //検出領域のY座標
                try {
                    prefs.setViewY(Integer.parseInt(viewYEditText.getText()
                            .toString()));
                } catch (Exception e) {
                    prefs.setViewY(point.y / -2);
                    viewYEditText.setText(String.valueOf(prefs.getViewY()));
                }

                //検出領域の幅
                try {
                    prefs.setViewWidth(Integer.parseInt(viewWidthEditText.getText()
                            .toString()));
                    if (prefs.getViewWidth() > 150) {
                        prefs.setViewWidth(150);
                        viewWidthEditText.setText(String.valueOf(prefs.getViewWidth()));
                    }
                } catch (Exception e) {
                    prefs.setViewWidth(20);
                    viewWidthEditText.setText(String.valueOf(prefs.getViewWidth()));
                }

                //検出領域の高さ
                try {
                    prefs.setViewHeight(Integer.parseInt(viewHeightEditText.getText()
                            .toString()));
                    if (prefs.getViewHeight() > 150) {
                        prefs.setViewHeight(150);
                        viewHeightEditText.setText(String.valueOf(prefs.getViewHeight()));
                    }
                } catch (Exception e) {
                    prefs.setViewHeight(50);
                    viewHeightEditText.setText(String.valueOf(prefs.getViewHeight()));
                }

                //検出領域の色
                ColorDrawable colorDrawable = (ColorDrawable) viewColor.getBackground();
                int colorInt = colorDrawable.getColor();
                prefs.setViewColor(colorInt);

                //検出領域タッチ時に振動させるか
                prefs.setVibratesWhenViewTouched(vibratesWhenViewTouchedCheck.isChecked());

                //上流プロキシを使用するか
                prefs.setUsesProxy(usesProxyCheck.isChecked());

                //上流プロキシのホスト名
                prefs.setProxyHost(proxyHostEditText.getText()
                        .toString());

                //上流プロキシのポート番号
                try {
                    prefs.setProxyPort(Integer.parseInt(proxyPortEditText.getText()
                            .toString()));
                } catch (Exception e) {
                    prefs.setProxyPort(8080);
                    proxyPortEditText.setText(String.valueOf(prefs.getProxyPort()));
                }

                //Jsonを記録するか
                prefs.setLogsJson(logsJsonCheck.isChecked());

                //リクエストボディを記録するか
                prefs.setLogsRequest(logsRequsetCheck.isChecked());

                prefs.setUsesMissionNotification(usesMissionNotificationCheck.isChecked());
                prefs.setUsesDockNotification(usesDockNotificationCheck.isChecked());
                prefs.setMakesSoundWhenNotify(makesSoundWhenNotifyCheck.isChecked());
                prefs.setVibratesWhenNOtify(vibratesWhenNotifyCheck.isChecked());
                prefs.setForcesLandscape(forceLandscapeCheck.isChecked());

                //稼働中のサービスを一度停止させてから再び起動させる
                getActivity().stopService(new Intent(getActivity().getApplicationContext(), LittleProxyServerService.class));
                getActivity().stopService(new Intent(getActivity(), SlantLauncher.class));
                getActivity().stopService(new Intent(getActivity().getApplicationContext(), OverlayService.class));

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
                        return;
                    }
                }

                getActivity().startService(new Intent(getActivity().getApplicationContext(), OverlayService.class));
                getActivity().startService(new Intent(getActivity().getApplicationContext(), LittleProxyServerService.class));
                getActivity().startService(new Intent(getActivity(), SlantLauncher.class));
            }
        });
    }

    @OnClick(R.id.view_color)
    void showColorPicker(){
        //検出領域の色
        ColorDrawable colorDrawable = (ColorDrawable) viewColor.getBackground();
        new AmbilWarnaDialog(getContext(), colorDrawable.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // color is the color selected by the user.
                viewColor.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        }).show();
    }

    //検出領域を画面の左上に表示するために画面のサイズを求める
    private Point getDisplaySize() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point real = new Point(0, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Android 4.2以上
            display.getRealSize(real);
            return real;

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // Android 3.2以上
            try {
                Method getRawWidth = Display.class.getMethod("getRawWidth");
                Method getRawHeight = Display.class.getMethod("getRawHeight");
                int width = (Integer) getRawWidth.invoke(display);
                int height = (Integer) getRawHeight.invoke(display);
                real.set(width, height);
                return real;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return real;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }
}
