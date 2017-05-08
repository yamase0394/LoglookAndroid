package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.R;

/**
 * Created by snake0394 on 2017/05/08.
 */

public class MaterialChartFilterDialogFragment extends android.support.v4.app.DialogFragment {
    
    private OnCheckFinishedListener listener;
    
    public static MaterialChartFilterDialogFragment newInstance() {
        MaterialChartFilterDialogFragment fragment = new MaterialChartFilterDialogFragment();
        return fragment;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        
        final View rootView = LayoutInflater.from(getContext())
                                            .inflate(R.layout.dialog_filter_material_chart, null);
        builder.setView(rootView)
               .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       List<String> checkedList = new ArrayList<>();
                
                       CheckBox checkBox = ButterKnife.findById(rootView, R.id.check_fuel);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       checkBox = ButterKnife.findById(rootView, R.id.check_bullet);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       checkBox = ButterKnife.findById(rootView, R.id.check_steel);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       checkBox = ButterKnife.findById(rootView, R.id.check_bauxite);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       checkBox = ButterKnife.findById(rootView, R.id.check_bucket);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       checkBox = ButterKnife.findById(rootView, R.id.check_instant_construction);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       checkBox = ButterKnife.findById(rootView, R.id.check_development_material);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       checkBox = ButterKnife.findById(rootView, R.id.check_improvement_material);
                       if (checkBox.isChecked()) {
                           checkedList.add(String.valueOf(checkBox.getText()));
                       }
                
                       listener.onCheckFinished(checkedList);
                   }
               })
               .setNegativeButton("キャンセル", null);
        
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
        super.onPause();
        dismiss();
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        try {
            this.listener = (OnCheckFinishedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCheckFinishedListener");
        }
    }
    
    
    interface OnCheckFinishedListener {
        void onCheckFinished(List<String> checkedList);
    }
}