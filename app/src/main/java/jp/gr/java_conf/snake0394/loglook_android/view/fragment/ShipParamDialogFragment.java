package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.ShipUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;

/**
 * Created by snake0394 on 2016/12/07.
 */

public class ShipParamDialogFragment extends android.support.v4.app.DialogFragment {
    private static final String ARG_SHIP_ID = "shipId";
    private static final String ARG_DECK_ID = "deckId";

    private int shipId;
    private int deckId;

    public static ShipParamDialogFragment newInstance(int shipId, int deckId) {
        ShipParamDialogFragment fragment = new ShipParamDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SHIP_ID, shipId);
        args.putInt(ARG_DECK_ID, deckId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shipId = getArguments().getInt(ARG_SHIP_ID);
            deckId = getArguments().getInt(ARG_DECK_ID);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_fragment_ship_param, null);
        final MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId);
        final Deck deck = DeckManager.INSTANCE.getDeck(deckId);

        TextView text = (TextView) rootView.findViewById(R.id.shellingBasicAttackPower);
        int power = (int) ShipUtility.getShellingBasicAttackPower(myShip);
        text.setText(String.valueOf(power));

        text = (TextView) rootView.findViewById(R.id.nightBattleBasicAttackPower);
        power = (int) ShipUtility.getNightBattleBasicAttackPower(myShip);
        text.setText(String.valueOf(power));

        text = (TextView) rootView.findViewById(R.id.proportionalAirDefence);
        //小数第二を四捨五入
        BigDecimal value = new BigDecimal(ShipUtility.getAdjustedAA(myShip) / 400 * 100);
        BigDecimal roundHalfUp = value.setScale(1, BigDecimal.ROUND_HALF_UP);
        text.setText(roundHalfUp + "%");

        Spinner spinner = (Spinner) rootView.findViewById(R.id.formationSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) rootView.findViewById(R.id.formationSpinner);
                // 初回起動時の動作
                if (spinner.isFocusable() == false) {
                    spinner.setFocusable(true);
                    return;
                }
                //固定撃墜を更新
                String formation = (String) spinner.getSelectedItem();
                EditText editText = (EditText) rootView.findViewById(R.id.AACIModifier);
                Editable getText = editText.getText();
                float AACIModefier = 1f;
                try {
                    AACIModefier = Float.parseFloat(getText.toString());
                } catch (NumberFormatException e) {
                    editText.setText("1.0");
                }
                TextView text = (TextView) rootView.findViewById(R.id.fixedAirDefence);
                text.setText(ShipUtility.getFixedAirDefense(myShip, deck, formation, AACIModefier) + "機");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("単縦陣/梯形陣/単横陣");
        adapter.add("複縦陣");
        adapter.add("輪形陣");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setFocusable(false);

        EditText editText = (EditText) rootView.findViewById(R.id.AACIModifier);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //IMEを閉じる
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    //固定撃墜を更新
                    Spinner spinner = (Spinner) rootView.findViewById(R.id.formationSpinner);
                    String formation = (String) spinner.getSelectedItem();
                    EditText editText = (EditText) rootView.findViewById(R.id.AACIModifier);
                    Editable getText = editText.getText();
                    float AACIModefier = 1f;
                    try {
                        AACIModefier = Float.parseFloat(getText.toString());
                    } catch (NumberFormatException e) {
                        editText.setText("1.0");
                    }
                    TextView text = (TextView) rootView.findViewById(R.id.fixedAirDefence);
                    text.setText(ShipUtility.getFixedAirDefense(myShip, deck, formation, AACIModefier) + "機");
                    handled = true;
                }
                return handled;
            }
        });

        text = (TextView) rootView.findViewById(R.id.fixedAirDefence);
        text.setText(ShipUtility.getFixedAirDefense(myShip, deck, "単縦陣", 1) + "機");

        builder.setView(rootView).setTitle(myShip.getName() + "(Lv" + myShip.getLv() + ")");
        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
