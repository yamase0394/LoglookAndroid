package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ScreenCaptureActivity;

/**
 * Created by snake0394 on 2016/12/07.
 */

public class LauncherDialogFragment extends android.support.v4.app.DialogFragment {

    private static final String[] launcherItems = new String[]{"スクショ", "艦隊", "遠征", "入渠", "損傷艦", "艦娘一覧", "装備一覧", "戦況"};

    public static LauncherDialogFragment newInstance() {
        LauncherDialogFragment fragment = new LauncherDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(launcherItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0 ) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent(getActivity(), ScreenCaptureActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        getContext().startActivity(intent);
                    }
                    String packageName = "com.dmm.dmmlabo.kancolle";
                    PackageManager pm = getActivity().getPackageManager();
                    Intent sendIntent = pm.getLaunchIntentForPackage(packageName);
                    startActivity(sendIntent);
                    dismiss();
                    return;
                }

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("usesLandscape", true);
                intent.putExtra("position", MainActivity.Screen.toScreen(launcherItems[which]).getPosition());
                startActivity(intent);
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog dialog = getDialog();

        //AttributeからLayoutParamsを求める
        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                                                        .getAttributes();

        //display metricsでdpのもと(?)を作る
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager()
                     .getDefaultDisplay()
                     .getMetrics(metrics);

        //LayoutParamsにdpを計算して適用(今回は横幅300dp)(※metrics.scaledDensityの返り値はfloat)
        float dialogWidth = 300 * metrics.scaledDensity;
        layoutParams.width = (int) dialogWidth;

        //LayoutParamsをセットする
        dialog.getWindow()
              .setAttributes(layoutParams);
    }


    @Override
    public void onPause() {
        Logger.d("LauncherDialogFragment", "onPause");
        super.onPause();
        //((DialogActivity) getActivity()).onDialogDissmissed();
        dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
}
