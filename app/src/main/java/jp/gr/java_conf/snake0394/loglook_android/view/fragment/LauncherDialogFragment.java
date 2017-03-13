package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.DialogActivity;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;

/**
 * Created by snake0394 on 2016/12/07.
 */

public class LauncherDialogFragment extends android.support.v4.app.DialogFragment {

    public static LauncherDialogFragment newInstance() {
        LauncherDialogFragment fragment = new LauncherDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = getResources().getStringArray(R.array.launcherTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("usesLandscape", true);
                intent.putExtra("position", MainActivity.Fragment.toMainFragment(which + 2).getPosition());
                startActivity(intent);
            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog dialog = getDialog();

        //AttributeからLayoutParamsを求める
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        //display metricsでdpのもと(?)を作る
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LayoutParamsにdpを計算して適用(今回は横幅300dp)(※metrics.scaledDensityの返り値はfloat)
        float dialogWidth = 300 * metrics.scaledDensity;
        layoutParams.width = (int) dialogWidth;

        //LayoutParamsをセットする
        dialog.getWindow().setAttributes(layoutParams);
    }


    @Override
    public void onPause() {
        super.onPause();
        ((DialogActivity) getActivity()).onDialogDissmissed();
        dismiss();
    }
}
