package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.SlantLauncher;
import jp.gr.java_conf.snake0394.loglook_android.proxy.LittleProxyServerService;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefsSpotRepository;

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

        final GeneralPrefs prefs = GeneralPrefsSpotRepository.getEntity(getContext());

        //ポート番号
        portEditText.setText(String.valueOf(prefs.port));

        //検出領域を可視化するか
        if (prefs.showsView) {
            showsViewCheck.setChecked(true);
        } else {
            showsViewCheck.setChecked(false);
        }

        //検出領域のx,y座標
        final Point point = getDisplaySize();
        if (prefs.viewX == Short.MAX_VALUE) {
            prefs.viewX = point.x / -2;
        }
        viewXEditText.setText(String.valueOf(prefs.viewX));

        if (prefs.viewY == Short.MAX_VALUE) {
            prefs.viewY = point.y / -2;
        }
        viewYEditText.setText(String.valueOf(prefs.viewY));

        //検出領域の大きさ
        viewWidthEditText.setText(String.valueOf(prefs.viewWidth));
        viewHeightEditText.setText(String.valueOf(prefs.viewHeight));

        //検出領域に触れたとき振動させるか
        if (prefs.vibratesWhenViewTouched) {
            vibratesWhenViewTouchedCheck.setChecked(true);
        } else {
            vibratesWhenViewTouchedCheck.setChecked(false);
        }

        //プロキシを使用するか
        if (prefs.usesProxy) {
            usesProxyCheck.setChecked(true);
        } else {
            usesProxyCheck.setChecked(false);
        }

        //ホスト名
        proxyHostEditText.setText(prefs.proxyHost);

        //ポート番号
        proxyPortEditText.setText(String.valueOf(prefs.proxyPort));

        //jsonを保存するか
        if (prefs.logsJson) {
            logsJsonCheck.setChecked(true);
        } else {
            logsJsonCheck.setChecked(false);
        }
        logsJsonCheck.setVisibility(View.GONE);

        //リクエストを保存するか
        if (prefs.logsRequest) {
            logsRequsetCheck.setChecked(true);
        } else {
            logsRequsetCheck.setChecked(false);
        }
        logsRequsetCheck.setVisibility(View.GONE);

        usesMissionNotificationCheck.setChecked(prefs.usesMissionNotification);
        usesDockNotificationCheck.setChecked(prefs.usesDockNotification);
        makesSoundWhenNotifyCheck.setChecked(prefs.makesSoundWhenNotify);
        vibratesWhenNotifyCheck.setChecked(prefs.vibratesWhenNOtify);

        //設定を保存するボタン
        Button tb = (Button) getActivity().findViewById(R.id.saveBtn);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ポ―ト番号
                SpannableStringBuilder sb = (SpannableStringBuilder) portEditText.getText();
                try {
                    prefs.port = Integer.parseInt(sb.toString());
                } catch (Exception e) {
                    prefs.port = 8000;
                    portEditText.setText(String.valueOf(prefs.port));
                }

                //検出領域を可視化するか
                prefs.showsView = showsViewCheck.isChecked();

                //検出領域のX座標
                sb = (SpannableStringBuilder) viewXEditText.getText();
                try {
                    prefs.viewX = Integer.parseInt(sb.toString());
                } catch (Exception e) {
                    prefs.viewX = point.x / -2;
                    viewXEditText.setText(String.valueOf(prefs.viewX));
                }

                //検出領域のY座標
                sb = (SpannableStringBuilder) viewYEditText.getText();
                try {
                    prefs.viewY = Integer.parseInt(sb.toString());
                } catch (Exception e) {
                    prefs.viewY = point.y / -2;
                    viewYEditText.setText(String.valueOf(prefs.viewY));
                }

                //検出領域の幅
                sb = (SpannableStringBuilder) viewWidthEditText.getText();
                try {
                    prefs.viewWidth = Integer.parseInt(sb.toString());
                    if (prefs.viewWidth > 150) {
                        prefs.viewWidth = 150;
                        viewWidthEditText.setText(String.valueOf(prefs.viewWidth));
                    }
                } catch (Exception e) {
                    prefs.viewWidth = 20;
                    viewWidthEditText.setText(String.valueOf(prefs.viewWidth));
                }

                //検出領域の高さ
                sb = (SpannableStringBuilder) viewHeightEditText.getText();
                try {
                    prefs.viewHeight = Integer.parseInt(sb.toString());
                    if (prefs.viewHeight > 150) {
                        prefs.viewHeight = 150;
                        viewHeightEditText.setText(String.valueOf(prefs.viewHeight));
                    }
                } catch (Exception e) {
                    prefs.viewHeight = 50;
                    viewHeightEditText.setText(String.valueOf(prefs.viewHeight));
                }

                //検出領域タッチ時に振動させるか
                prefs.vibratesWhenViewTouched = vibratesWhenViewTouchedCheck.isChecked();

                //上流プロキシを使用するか
                prefs.usesProxy = usesProxyCheck.isChecked();

                //上流プロキシのホスト名
                sb = (SpannableStringBuilder) proxyHostEditText.getText();
                prefs.proxyHost = sb.toString();

                //上流プロキシのポート番号
                sb = (SpannableStringBuilder) proxyPortEditText.getText();
                try {
                    prefs.proxyPort = Integer.parseInt(sb.toString());
                } catch (Exception e) {
                    prefs.proxyPort = 8080;
                    proxyPortEditText.setText(String.valueOf(prefs.proxyPort));
                }

                //Jsonを記録するか
                prefs.logsJson = logsJsonCheck.isChecked();

                //リクエストボディを記録するか
                prefs.logsRequest = logsRequsetCheck.isChecked();

                prefs.usesMissionNotification = usesMissionNotificationCheck.isChecked();
                prefs.usesDockNotification = usesDockNotificationCheck.isChecked();
                prefs.makesSoundWhenNotify = makesSoundWhenNotifyCheck.isChecked();
                prefs.vibratesWhenNOtify = vibratesWhenNotifyCheck.isChecked();

                GeneralPrefsSpotRepository.putEntity(getContext(), prefs);

                //稼働中のサービスを一度停止させてから再び起動させる
                Intent intent = new Intent(getActivity(), LittleProxyServerService.class);
                getActivity().stopService(intent);
                intent = new Intent(getActivity(), SlantLauncher.class);
                getActivity().stopService(intent);

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                if (!sp.getBoolean("SystemAlertPermissionGranted", true) || !sp.getBoolean("UsageAccessPermissionGranted", true)) {
                    Toast.makeText(getContext(), "端末の設定から権限の許可を行う必要があります。", Toast.LENGTH_LONG)
                         .show();
                    return;
                }

                intent = new Intent(getActivity(), LittleProxyServerService.class);
                getActivity().startService(intent);
                intent = new Intent(getActivity(), SlantLauncher.class);
                getActivity().startService(intent);
            }
        });
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
