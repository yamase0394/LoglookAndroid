package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.R;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by snake0394 on 2017/02/07.
 */

public class MyShipListAddLabelDialog extends android.support.v4.app.DialogFragment {

    private static final String ARG_POSITION = "position";

    private OnFinishedListener listener;

    public static DialogFragment newInstance(int position) {
        DialogFragment dialogFragment = new MyShipListAddLabelDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<MyShipListRecyclerViewAdapter.Label> labelList = new Gson().fromJson(App.getInstance()
                                                                                          .getSharedPreferences()
                                                                                          .getString("labelList", null), new TypeToken<List<MyShipListRecyclerViewAdapter.Label>>() {
        }.getType());

        final Set<String> labelNameSet = new HashSet();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View addLabelView = View.inflate(getContext(), R.layout.dialog_add_my_ship_list_label, null);

        final View colorSampleView = addLabelView.findViewById(R.id.view_color_sample);
        addLabelView.findViewById(R.id.button_color_picker)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AmbilWarnaDialog(v.getContext(), Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                                @Override
                                public void onOk(AmbilWarnaDialog dialog, int color) {
                                    // color is the color selected by the user.
                                    colorSampleView.setBackgroundColor(color);
                                }

                                @Override
                                public void onCancel(AmbilWarnaDialog dialog) {
                                    // cancel was selected by the user
                                }
                            }).show();
                        }
                    });

        final TextInputLayout nameTextInputLayout = (TextInputLayout) addLabelView.findViewById(R.id.text_input_name);
        nameTextInputLayout.setErrorEnabled(true);
        nameTextInputLayout.setError("必須");
        nameTextInputLayout.getEditText()
                           .addTextChangedListener(new TextWatcher() {
                               @Override
                               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                               }

                               @Override
                               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                               }

                               @Override
                               public void afterTextChanged(Editable editable) {
                                   if (editable.toString()
                                               .length() == 0) {
                                       nameTextInputLayout.setErrorEnabled(true);
                                       nameTextInputLayout.setError("必須");
                                       return;
                                   } else if(labelNameSet.contains(editable.toString())){
                                       nameTextInputLayout.setErrorEnabled(true);
                                       nameTextInputLayout.setError("既に存在するラベル名です。ラベルの色が上書きされます。");
                                       return;
                                   }
                                   nameTextInputLayout.setErrorEnabled(false);
                               }
                           });

        final Spinner existingLabelSpinner = (Spinner) addLabelView.findViewById(R.id.spinner_existing_label);
        for (MyShipListRecyclerViewAdapter.Label label : labelList) {
            labelNameSet.add(label.getName());
        }
        existingLabelSpinner.setVisibility(View.GONE);

        final CheckBox usesExistingLabelCheck = (CheckBox) addLabelView.findViewById(R.id.check_uses_existsing_label);
        if (labelList.isEmpty()) {
            usesExistingLabelCheck.setVisibility(View.GONE);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(labelNameSet));
            existingLabelSpinner.setAdapter(adapter);
            existingLabelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedStr = String.valueOf(parent.getSelectedItem());
                    for (MyShipListRecyclerViewAdapter.Label label : labelList) {
                        if (label.getName()
                                 .equals(selectedStr)) {
                            colorSampleView.setBackgroundColor(label.getColor());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            usesExistingLabelCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        existingLabelSpinner.setVisibility(View.VISIBLE);
                        nameTextInputLayout.setVisibility(View.GONE);
                    } else {
                        nameTextInputLayout.setVisibility(View.VISIBLE);
                        existingLabelSpinner.setVisibility(View.GONE);
                    }
                }
            });

        }

        builder.setTitle("ラベルを追加")
               .setView(addLabelView)
               .setPositiveButton("追加", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if(usesExistingLabelCheck.isChecked()){
                           String selectedStr = String.valueOf(existingLabelSpinner.getSelectedItem());
                           for (MyShipListRecyclerViewAdapter.Label label : labelList) {
                               if (label.getName()
                                        .equals(selectedStr)) {
                                   View colorSampleView = addLabelView.findViewById(R.id.view_color_sample);
                                   ColorDrawable colorDrawable = (ColorDrawable) colorSampleView.getBackground();
                                   int colorInt = colorDrawable.getColor();
                                   listener.onAddLabelFinished(false, getArguments().getInt(ARG_POSITION),new MyShipListRecyclerViewAdapter.Label(label.getName(), colorInt));
                                   return;
                               }
                           }
                       }

                       TextInputLayout nameTextInputLayout = (TextInputLayout) addLabelView.findViewById(R.id.text_input_name);
                       String labelName = nameTextInputLayout.getEditText().getEditableText().toString();
                       if (labelName.length() == 0) {
                           listener.onAddLabelFinished(true, getArguments().getInt(ARG_POSITION), null);
                           return;
                       }
                       View colorSampleView = addLabelView.findViewById(R.id.view_color_sample);
                       ColorDrawable colorDrawable = (ColorDrawable) colorSampleView.getBackground();
                       int colorInt = colorDrawable.getColor();
                       MyShipListRecyclerViewAdapter.Label label = new MyShipListRecyclerViewAdapter.Label(labelName, colorInt);
                       listener.onAddLabelFinished(false, getArguments().getInt(ARG_POSITION), label);
                   }
               })
               .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       listener.onAddLabelFinished(true, getArguments().getInt(ARG_POSITION), null);
                   }
               });
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

    public interface OnFinishedListener {
        void onAddLabelFinished(boolean isCanceled, int position, @Nullable MyShipListRecyclerViewAdapter.Label label);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.listener = (OnFinishedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFinishedListener");
        }
    }
}