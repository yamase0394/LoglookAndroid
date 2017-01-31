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

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.SlantLauncher;
import jp.gr.java_conf.snake0394.loglook_android.proxy.LittleProxyServerService;

import static android.content.Context.WINDOW_SERVICE;
import static jp.gr.java_conf.snake0394.loglook_android.R.id.viewX;

public class ConfigFragment extends Fragment {

    public ConfigFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance() {
        ConfigFragment fragment = new ConfigFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //ポート番号
        EditText ed = (EditText) getActivity().findViewById(R.id.port);
        ed.setText(sharedPreferences.getString("port", "8080"));

        //検出領域を可視化するか
        CheckBox cb = (CheckBox) getActivity().findViewById(R.id.showViewCheck);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                editor.putBoolean("showView", cb.isChecked());
                editor.apply();
            }
        });
        if (sharedPreferences.getBoolean("showView", true)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }

        //検出領域のx,y座標
        Point point = getDisplaySize();
        ed = (EditText) getActivity().findViewById(viewX);
        ed.setText(String.valueOf(sharedPreferences.getInt("viewX", point.x / -2)));
        ed = (EditText) getActivity().findViewById(R.id.viewY);
        ed.setText(String.valueOf(sharedPreferences.getInt("viewY", point.y / -2)));

        //検出領域の大きさ
        ed = (EditText) getActivity().findViewById(R.id.viewWidth);
        ed.setText(String.valueOf(sharedPreferences.getInt("viewWidth", 20)));
        ed = (EditText) getActivity().findViewById(R.id.viewHeight);
        ed.setText(String.valueOf(sharedPreferences.getInt("viewHeight", 50)));

        //検出領域に触れたとき振動させるか
        cb = (CheckBox) getActivity().findViewById(R.id.touchVibrationCheck);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                editor.putBoolean("touchVibration", cb.isChecked());
                editor.apply();
            }
        });
        if (sharedPreferences.getBoolean("touchVibration", true)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }

        //プロキシを使用するか
        cb = (CheckBox) getActivity().findViewById(R.id.useProxyCheck);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                editor.putBoolean("useProxy", cb.isChecked());
                editor.apply();
            }
        });
        if (sharedPreferences.getBoolean("useProxy", false)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }

        //ホスト名
        ed = (EditText) getActivity().findViewById(R.id.proxyHost);
        ed.setText(sharedPreferences.getString("proxyHost", ""));

        //ポート番号
        ed = (EditText) getActivity().findViewById(R.id.proxyPort);
        ed.setText(sharedPreferences.getString("proxyPort", ""));

        //jsonを保存するか
        cb = (CheckBox) getActivity().findViewById(R.id.saveJsoncheck);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                editor.putBoolean("saveJson", cb.isChecked());
                editor.apply();
            }
        });
        if (sharedPreferences.getBoolean("saveJson", false)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        cb.setVisibility(View.GONE);

        //リクエストを保存するか
        cb = (CheckBox) getActivity().findViewById(R.id.saveRequesstCheck);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                editor.putBoolean("saveRequest", cb.isChecked());
                editor.apply();
            }
        });
        if (sharedPreferences.getBoolean("saveRequest", false)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        cb.setVisibility(View.GONE);

        //設定を保存するボタン
        Button tb = (Button) getActivity().findViewById(R.id.saveBtn);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ed = (EditText) getActivity().findViewById(R.id.port);
                SpannableStringBuilder sb = (SpannableStringBuilder) ed.getText();
                editor.putString("port", sb.toString());

                ed = (EditText) getActivity().findViewById(viewX);
                sb = (SpannableStringBuilder) ed.getText();
                int viewX = 0;
                try {
                    viewX = Integer.parseInt(sb.toString());
                } catch (Exception e) {
                }
                editor.putInt("viewX",viewX );

                ed = (EditText) getActivity().findViewById(R.id.viewY);
                sb = (SpannableStringBuilder) ed.getText();
                int viewY = 0;
                try {
                    viewY = Integer.parseInt(sb.toString());
                } catch (Exception e) {
                }
                editor.putInt("viewY", viewY);

                ed = (EditText) getActivity().findViewById(R.id.viewWidth);
                sb = (SpannableStringBuilder) ed.getText();
                int viewWidth = Integer.parseInt(sb.toString());
                if (viewWidth > 150) {
                    viewWidth = 150;
                    ed.setText(String.valueOf(viewWidth));
                }
                editor.putInt("viewWidth", viewWidth);

                ed = (EditText) getActivity().findViewById(R.id.viewHeight);
                sb = (SpannableStringBuilder) ed.getText();
                int viewHeight = Integer.parseInt(sb.toString());
                if (viewHeight > 150) {
                    viewHeight = 150;
                    ed.setText(String.valueOf(viewHeight));
                }
                editor.putInt("viewHeight", viewHeight);

                ed = (EditText) getActivity().findViewById(R.id.proxyHost);
                sb = (SpannableStringBuilder) ed.getText();
                editor.putString("proxyHost", sb.toString());

                ed = (EditText) getActivity().findViewById(R.id.proxyPort);
                sb = (SpannableStringBuilder) ed.getText();
                editor.putString("proxyPort", sb.toString());

                editor.commit();

                Intent intent = new Intent(getActivity(), LittleProxyServerService.class);
                getActivity().stopService(intent);
                intent = new Intent(getActivity(), SlantLauncher.class);
                getActivity().stopService(intent);
                intent = new Intent(getActivity(), LittleProxyServerService.class);
                getActivity().startService(intent);
                intent = new Intent(getActivity(), SlantLauncher.class);
                getActivity().startService(intent);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Toast.makeText(getActivity(), "起動 port:" + sp.getString("port", "8080"), Toast.LENGTH_SHORT).show();
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
}
