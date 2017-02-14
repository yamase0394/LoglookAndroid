package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.DeckUtility;
import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ShipDetailActivity;

import static butterknife.ButterKnife.findById;

/**
 * Created by snake0394 on 2016/12/08.
 */

public class DeckTabsRecyclerViewAdapter extends RecyclerView.Adapter<DeckTabsRecyclerViewAdapter.DeckTabsRecyclerViewHolder> {

    private List<Deck> deckList;
    private OnRecyclerViewClickListener listener;

    public DeckTabsRecyclerViewAdapter(OnRecyclerViewClickListener listener) {
        deckList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public DeckTabsRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.fragment_deck, viewGroup, false);
        return new DeckTabsRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeckTabsRecyclerViewHolder viewHolder, int i) {
        viewHolder.bind(deckList.get(i));
    }

    @Override
    public int getItemCount() {
        return deckList.size();
    }

    public void setItems(List<Deck> deckList) {
        this.deckList = deckList;
    }

    public interface OnRecyclerViewClickListener {
        void onRecyclerViewClicked(DialogFragment dialogFragment, Intent intent);
    }

    class DeckTabsRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView[] names;
        private TextView[] conds;
        private TextView[] lvs;
        private TextView[] states;
        private ProgressBar[] progressBars;
        private TextView[] hps;
        private TextView[] fuelDescriptions;
        private TextView[] fuels;
        private TextView[] bullDescriptions;
        private TextView[] bulls;
        private LinearLayout[] equipmentLayouts;
        private ImageView[][] slots;
        private TextView[] slotExDescriptions;
        private ImageView[] slotExIcons;
        private Button[] detailButtons;

        private TextView seiku;
        private TextView touchStartRate;
        private TextView sakuteki33;
        private EditText junction;
        private TextView levelSum;
        private TextView condRecoveryTime;

        public DeckTabsRecyclerViewHolder(View rootView) {
            super(rootView);

            final int maxShipNum = 6;
            final int maxSlotNum = 4;

            names = new TextView[maxShipNum];
            conds = new TextView[maxShipNum];
            lvs = new TextView[maxShipNum];
            states = new TextView[maxShipNum];
            progressBars = new ProgressBar[maxShipNum];
            hps = new TextView[maxShipNum];
            fuelDescriptions = new TextView[maxShipNum];
            fuels = new TextView[maxShipNum];
            bullDescriptions = new TextView[maxShipNum];
            bulls = new TextView[maxShipNum];
            equipmentLayouts = new LinearLayout[maxShipNum];
            slots = new ImageView[maxShipNum][maxSlotNum];
            slotExDescriptions = new TextView[maxShipNum];
            slotExIcons = new ImageView[maxShipNum];
            detailButtons = new Button[maxShipNum];

            final Resources res = rootView.getResources();
            final String packageName = App.getInstance()
                                          .getPackageName();
            for (int i = 1; i <= maxShipNum; i++) {
                String name = "name" + i;
                int strId = res.getIdentifier(name, "id", packageName);
                names[i - 1] = findById(rootView, strId);

                name = "cond" + i;
                strId = res.getIdentifier(name, "id", packageName);
                conds[i - 1] = findById(rootView, strId);

                name = "lv" + i;
                strId = res.getIdentifier(name, "id", packageName);
                lvs[i - 1] = findById(rootView, strId);

                name = "state" + i;
                strId = res.getIdentifier(name, "id", packageName);
                states[i - 1] = findById(rootView, strId);

                name = "progressBar" + i;
                strId = res.getIdentifier(name, "id", packageName);
                progressBars[i - 1] = findById(rootView, strId);

                name = "hp" + i;
                strId = res.getIdentifier(name, "id", packageName);
                hps[i - 1] = findById(rootView, strId);

                name = "fuelText" + i;
                strId = res.getIdentifier(name, "id", packageName);
                fuelDescriptions[i - 1] = findById(rootView, strId);

                name = "fuel" + i;
                strId = res.getIdentifier(name, "id", packageName);
                fuels[i - 1] = findById(rootView, strId);

                name = "bullText" + i;
                strId = res.getIdentifier(name, "id", packageName);
                bullDescriptions[i - 1] = findById(rootView, strId);

                name = "bull" + i;
                strId = res.getIdentifier(name, "id", packageName);
                bulls[i - 1] = findById(rootView, strId);

                name = "equipments" + i;
                strId = res.getIdentifier(name, "id", packageName);
                equipmentLayouts[i - 1] = findById(rootView, strId);

                for (int j = 1; j <= 4; j++) {
                    name = "slot" + i + j;
                    strId = res.getIdentifier(name, "id", packageName);
                    slots[i - 1][j - 1] = findById(rootView, strId);
                }

                name = "slotEx" + i;
                strId = res.getIdentifier(name, "id", packageName);
                slotExDescriptions[i - 1] = findById(rootView, strId);

                name = "slotExIcon" + i;
                strId = res.getIdentifier(name, "id", packageName);
                slotExIcons[i - 1] = findById(rootView, strId);

                name = "detail" + i;
                strId = res.getIdentifier(name, "id", packageName);
                detailButtons[i - 1] = findById(rootView, strId);
            }

            seiku = findById(rootView, R.id.seiku);
            touchStartRate = findById(rootView, R.id.touchStartRate);
            sakuteki33 = findById(rootView, R.id.sakuteki33);
            junction = findById(rootView, R.id.junction);
            levelSum = findById(rootView, R.id.levelSum);
            condRecoveryTime = findById(rootView, R.id.condRecoveryTime);
        }

        public void bind(@NonNull final Deck deck) {
            try {
                final List<Integer> shipId = deck.getShipId();

                for (int i = 0; i < shipId.size(); i++) {
                    //空きの場合
                    if (shipId.get(i) == -1) {
                        names[i].setVisibility(View.INVISIBLE);
                        conds[i].setVisibility(View.INVISIBLE);
                        lvs[i].setVisibility(View.INVISIBLE);
                        states[i].setVisibility(View.INVISIBLE);
                        progressBars[i].setVisibility(View.INVISIBLE);
                        hps[i].setVisibility(View.INVISIBLE);
                        fuelDescriptions[i].setVisibility(View.INVISIBLE);
                        fuels[i].setVisibility(View.INVISIBLE);
                        bullDescriptions[i].setVisibility(View.INVISIBLE);
                        bulls[i].setVisibility(View.INVISIBLE);
                        for (int j = 0; j < 4; j++) {
                            slots[i][j].setVisibility(View.INVISIBLE);
                        }
                        slotExDescriptions[i].setVisibility(View.INVISIBLE);
                        slotExIcons[i].setVisibility(View.INVISIBLE);
                        detailButtons[i].setVisibility(View.GONE);
                        continue;
                    }

                    final int id = shipId.get(i);
                    final MyShip myShip = MyShipManager.INSTANCE.getMyShip(id);

                    names[i].setVisibility(View.VISIBLE);
                    names[i].setText(myShip.getName());
                    names[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.support.v4.app.DialogFragment dialogFragment = ShipParamDialogFragment.newInstance(id, deck.getId());
                            listener.onRecyclerViewClicked(dialogFragment, null);
                        }
                    });

                    lvs[i].setVisibility(View.VISIBLE);
                    lvs[i].setText("Lv:" + String.valueOf(myShip.getLv()));

                    states[i].setVisibility(View.VISIBLE);
                    if (deck.getMission()
                            .get(0) == 1 || deck.getMission()
                                                .get(0) == 3) {
                        states[i].setText("遠征");
                        //インディゴ
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.expedition));
                    } else if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                        states[i].setText("退避");
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.escort));
                    } else if (DockTimer.INSTANCE.getShipId(1) == myShip.getId() || DockTimer.INSTANCE.getShipId(2) == myShip.getId() || DockTimer.INSTANCE.getShipId(3) == myShip.getId() || DockTimer.INSTANCE.getShipId(4) == myShip.getId()) {
                        states[i].setText("入渠");
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.docking));
                    } else if (myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                        states[i].setText("大破");
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.heavy_damage));
                    } else if (myShip.getNowhp() <= myShip.getMaxhp() / 2) {
                        states[i].setText("中破");
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.moderate_damage));
                    } else if (myShip.getNowhp() <= myShip.getMaxhp() * 3 / 4) {
                        states[i].setText("小破");
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.minor_damage));
                    } else if (myShip.getNowhp() < myShip.getMaxhp()) {
                        states[i].setText("健在");
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.good_health));
                    } else {
                        states[i].setText("無傷");
                        states[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.undamaged));
                    }

                    MySlotItem mySlotItem;
                    MstSlotitem mstSlotitem;
                    for (int j = 0; j < 4; j++) {
                        slots[i][j].setVisibility(View.VISIBLE);

                        if (j >= myShip.getSlotnum()) {
                            slots[i][j].setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
                            continue;
                        }

                        if (myShip.getSlot().get(j) == -1) {
                            slots[i][j].setImageResource(EquipIconId.EMPTY.getImageId());
                            continue;
                        }

                        mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlot().get(j));
                        mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

                        slots[i][j].setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType().get(3)).getImageId());
                    }

                    slotExDescriptions[i].setVisibility(View.VISIBLE);

                    slotExIcons[i].setVisibility(View.VISIBLE);
                    if (MySlotItemManager.INSTANCE.contains(myShip.getSlotEx())) {
                        mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlotEx());
                        mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                        slotExIcons[i].setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType().get(3)).getImageId());
                    } else {
                        if (myShip.getSlotEx() == 0) {
                            slotExIcons[i].setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
                        } else if (myShip.getSlotEx() == -1) {
                            slotExIcons[i].setImageResource(EquipIconId.EMPTY.getImageId());
                        }
                    }

                    equipmentLayouts[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.support.v4.app.DialogFragment dialogFragment = EquipmentDialogFragment.newInstance(id);
                            listener.onRecyclerViewClicked(dialogFragment, null);
                        }
                    });

                    progressBars[i].setVisibility(View.VISIBLE);
                    progressBars[i].setMax(myShip.getMaxhp());
                    progressBars[i].setProgress(myShip.getNowhp());

                    hps[i].setVisibility(View.VISIBLE);
                    hps[i].setText(Integer.toString(myShip.getNowhp()) + "/" + Integer.toString(myShip.getMaxhp()));

                    fuelDescriptions[i].setVisibility(View.VISIBLE);

                    fuels[i].setVisibility(View.VISIBLE);
                    MstShip mstShip = MstShipManager.INSTANCE.getMstShip(myShip.getShipId());
                    int fuelMax = mstShip.getFuelMax();
                    int fuelNow = myShip.getFuel();
                    if (fuelNow == fuelMax) {
                        fuels[i].setText("||||||||||");
                        //緑
                        fuels[i].setTextColor(Color.rgb(59, 175, 117));
                    } else if (fuelNow >= fuelMax * 8 / 9) {
                        fuels[i].setText("|||||||||");
                        //黄色
                        fuels[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (fuelNow >= fuelMax * 7 / 9) {
                        fuels[i].setText("||||||||");
                        //黄色
                        fuels[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (fuelNow >= fuelMax * 6 / 9) {
                        fuels[i].setText("|||||||");
                        //オレンジ
                        fuels[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 5 / 9) {
                        fuels[i].setText("||||||");
                        //オレンジ
                        fuels[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 4 / 9) {
                        fuels[i].setText("|||||");
                        //オレンジ
                        fuels[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 3 / 9) {
                        fuels[i].setText("||||");
                        //オレンジ
                        fuels[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 2 / 9) {
                        fuels[i].setText("|||");
                        fuels[i].setTextColor(Color.RED);
                    } else if (fuelNow >= fuelMax * 1 / 9) {
                        fuels[i].setText("||");
                        fuels[i].setTextColor(Color.RED);
                    } else if (fuelNow > 0) {
                        fuels[i].setText("|");
                        fuels[i].setTextColor(Color.RED);
                    } else {
                        fuels[i].setText("-");
                        //グレー
                        fuels[i].setTextColor(Color.rgb(118, 118, 118));
                    }

                    bullDescriptions[i].setVisibility(View.VISIBLE);

                    bulls[i].setVisibility(View.VISIBLE);
                    int bullMax = mstShip.getBullMax();
                    int bullNow = myShip.getBull();
                    if (bullNow == bullMax) {
                        bulls[i].setText("||||||||||");
                        //緑
                        bulls[i].setTextColor(Color.rgb(59, 175, 117));
                    } else if (bullNow >= bullMax * 8 / 9) {
                        bulls[i].setText("|||||||||");
                        //黄色
                        bulls[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (bullNow >= bullMax * 7 / 9) {
                        bulls[i].setText("||||||||");
                        //黄色
                        bulls[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (bullNow >= bullMax * 6 / 9) {
                        bulls[i].setText("|||||||");
                        //オレンジ
                        bulls[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 5 / 9) {
                        bulls[i].setText("||||||");
                        //オレンジ
                        bulls[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 4 / 9) {
                        bulls[i].setText("|||||");
                        //オレンジ
                        bulls[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 3 / 9) {
                        bulls[i].setText("||||");
                        //オレンジ
                        bulls[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 2 / 9) {
                        bulls[i].setText("|||");
                        bulls[i].setTextColor(Color.RED);
                    } else if (bullNow >= bullMax * 1 / 9) {
                        bulls[i].setText("||");
                        bulls[i].setTextColor(Color.RED);
                    } else if (bullNow > 0) {
                        bulls[i].setText("|");
                        bulls[i].setTextColor(Color.RED);
                    } else {
                        bulls[i].setText("-");
                        //グレー
                        bulls[i].setTextColor(Color.rgb(118, 118, 118));
                    }

                    conds[i].setVisibility(View.VISIBLE);
                    conds[i].setText("cond:" + Integer.toString(myShip.getCond()));
                    //condの値で色分けする
                    if (myShip.getCond() >= 50) {
                        conds[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.high_morale));
                    } else if (myShip.getCond() >= 40) {
                        conds[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.normal_cond));
                    } else if (myShip.getCond() >= 30) {
                        conds[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.slightly_fatigued));
                    } else if (myShip.getCond() >= 20) {
                        conds[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.moderately_fatigued));
                    } else {
                        conds[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.seriously_fatigued));
                    }

                    detailButtons[i].setVisibility(View.VISIBLE);
                    detailButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(App.getInstance(), ShipDetailActivity.class);
                            intent.putExtra("shipId", id);
                            intent.putExtra("deckId", deck.getId());

                            Configuration config = App.getInstance().getResources().getConfiguration();
                            switch (config.orientation) {
                                case Configuration.ORIENTATION_PORTRAIT:
                                    intent.putExtra("usesLandscape", false);
                                    break;
                                case Configuration.ORIENTATION_LANDSCAPE:
                                    intent.putExtra("usesLandscape", true);
                                    break;
                            }

                            listener.onRecyclerViewClicked(null, intent);
                        }
                    });
                }

                seiku.setText(String.valueOf(DeckUtility.getSeiku(deck)));
                touchStartRate.setText(String.valueOf(DeckUtility.getTouchStartRate(deck)) + "%");
                sakuteki33.setText(String.valueOf(DeckUtility.getSakuteki33(deck, 1)));
                junction.setText("1");
                junction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            //IMEを閉じる
                            InputMethodManager inputMethodManager = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            //索敵(33)
                            Editable getText = junction.getText();
                            float junctionFloat = 1f;
                            try {
                                junctionFloat = Float.parseFloat(getText.toString());
                            } catch (NumberFormatException e) {
                                junction.setText("1");
                            }
                            sakuteki33.setText(String.valueOf(DeckUtility.getSakuteki33(deck, junctionFloat)));
                        }
                        return true;
                    }
                });
                levelSum.setText(String.valueOf(deck.getLevelSum()));
                condRecoveryTime.setText(deck.getCondRecoveryTime());

            } catch (Exception e) {
                ErrorLogger.writeLog(e);
                e.printStackTrace();
            }

        }
    }

}