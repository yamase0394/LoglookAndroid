package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by snake0394 on 2017/02/07.
 */

public class OptionsDialog extends DialogFragment {
    
    private static final String ARG_OPTIONS = "options";
    
    private OnItemSelectedListener listener;
    
    public static DialogFragment newInstance(String[] options) {
        DialogFragment dialogFragment = new OptionsDialog();
        Bundle args = new Bundle();
        args.putStringArray(ARG_OPTIONS, options);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] options = getArguments().getStringArray(ARG_OPTIONS);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onItemSelected(i);
            }
        }).setNegativeButton("キャンセル", null);
        
        return builder.create();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        Dialog dialog = getDialog();
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
    
    public interface OnItemSelectedListener {
        void onItemSelected(int witch);
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        try {
            this.listener = (OnItemSelectedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnItemSelectedListener");
        }
    }
}