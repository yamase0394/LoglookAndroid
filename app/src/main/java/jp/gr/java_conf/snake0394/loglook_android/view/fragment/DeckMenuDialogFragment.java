package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;

import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.ShipUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;

/**
 * Created by snake0394 on 2016/12/07.
 */

public class DeckMenuDialogFragment extends android.support.v4.app.DialogFragment {

    private Realm realm;

    public static DeckMenuDialogFragment newInstance(int deckId) {
        DeckMenuDialogFragment fragment = new DeckMenuDialogFragment();

        Bundle args = new Bundle();
        args.putInt("deckId", deckId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();

        final Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_fragment_deck_menu, null);
        final Deck deck = DeckManager.INSTANCE.getDeck(getArguments().getInt("deckId"));

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
                TextView text;
                for (int i = 1; i <= deck.getShipId().size(); i++) {
                    int shipId = deck.getShipId().get(i - 1);

                    MyShip myShip = realm.where(MyShip.class).equalTo("id", shipId).findFirst();
                    if (myShip == null || shipId == -1) {
                        break;
                    }

                    String name = "proportional" + i;
                    int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                    text = (TextView) rootView.findViewById(strId);
                    //text.setVisibility(View.VISIBLE);
                    //小数第二を四捨五入
                    BigDecimal value = new BigDecimal(ShipUtility.getAdjustedAA(myShip) / 400 * 100);
                    BigDecimal roundHalfUp = value.setScale(1, BigDecimal.ROUND_HALF_UP);
                    text.setText(roundHalfUp.toString());

                    name = "fixed" + i;
                    strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                    text = (TextView) rootView.findViewById(strId);
                    //text.setVisibility(View.VISIBLE);
                    text.setText(String.valueOf(ShipUtility.getFixedAirDefense(myShip, deck, formation, AACIModefier)));
                }
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
        String formation = (String) spinner.getSelectedItem();

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

                    TextView text;
                    for (int i = 1; i <= deck.getShipId().size(); i++) {
                        int shipId = deck.getShipId().get(i - 1);

                        MyShip myShip = realm.where(MyShip.class).equalTo("id", shipId).findFirst();
                        if (myShip == null || shipId == -1) {
                            break;
                        }

                        String name = "proportional" + i;
                        int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                        text = (TextView) rootView.findViewById(strId);
                        //text.setVisibility(View.VISIBLE);
                        //小数第二を四捨五入
                        BigDecimal value = new BigDecimal(ShipUtility.getAdjustedAA(myShip) / 400 * 100);
                        BigDecimal roundHalfUp = value.setScale(1, BigDecimal.ROUND_HALF_UP);
                        text.setText(roundHalfUp.toString());

                        name = "fixed" + i;
                        strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                        text = (TextView) rootView.findViewById(strId);
                        //text.setVisibility(View.VISIBLE);
                        text.setText(String.valueOf(ShipUtility.getFixedAirDefense(myShip, deck, formation, AACIModefier)));
                    }
                    handled = true;
                }
                return handled;
            }
        });
        Editable getText = editText.getText();
        float AACIModefier = 1f;
        try {
            AACIModefier = Float.parseFloat(getText.toString());
        } catch (NumberFormatException e) {
            editText.setText("1.0");
        }

        TextView text;
        for (int i = 1; i <= 7; i++) {
            int shipId;
            if(i > deck.getShipId().size()) {
                shipId = -1;
            } else {
                shipId = deck.getShipId().get(i - 1 + 100);
            }

            MyShip myShip = realm.where(MyShip.class).equalTo("id", shipId).findFirst();
            if (myShip == null || shipId == -1) {

                String name = "name" + i;
                int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "proportional" + i;
                strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "fixed" + i;
                strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                continue;
            }

            String name = "name" + i;
            int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            //text.setVisibility(View.VISIBLE);
            MstShip mstShip = realm.where(MstShip.class).equalTo("id", myShip.getShipId()).findFirst();
            text.setText(mstShip.getName());

            name = "proportional" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            //text.setVisibility(View.VISIBLE);
            //小数第二を四捨五入
            BigDecimal value = new BigDecimal(ShipUtility.getAdjustedAA(myShip) / 400 * 100);
            BigDecimal roundHalfUp = value.setScale(1, BigDecimal.ROUND_HALF_UP);
            text.setText(roundHalfUp.toString());

            name = "fixed" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            //text.setVisibility(View.VISIBLE);
            text.setText(String.valueOf(ShipUtility.getFixedAirDefense(myShip, deck, formation, AACIModefier)));
        }

        builder.setView(rootView).setTitle("第" + getArguments().getInt("deckId") + "艦隊防空情報");
        
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }
}
