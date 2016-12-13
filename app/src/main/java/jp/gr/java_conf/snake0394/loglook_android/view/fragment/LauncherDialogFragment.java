package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;

/**
 * Created by snake0394 on 2016/12/07.
 */

public class LauncherDialogFragment extends android.support.v4.app.DialogFragment {

    public static LauncherDialogFragment newInstance() {
        LauncherDialogFragment fragment = new LauncherDialogFragment();

        Bundle args = new Bundle();
        //args.putInt("shipId", shipId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = getResources().getStringArray(R.array.launcherTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        }).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("usesLandscape", true);
                intent.putExtra("position", MainActivity.Fragment.toMainFragment(which + 2).getPosition());
                startActivity(intent);
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("dis,oss", "dismiss");
            }
        });
        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
