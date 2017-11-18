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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.DeckUtility;
import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ShipDetailActivity;

import static butterknife.ButterKnife.findById;

/**
 * Created by snake0394 on 2016/12/08.
 */

public class DeckTabsRecyclerViewAdapter extends RecyclerView.Adapter<DeckTabsRecyclerViewAdapter.DeckTabsRecyclerViewHolder> {

    static final int VIEW_TYPE_NORMAL = 0;
    static final int VIEW_TYPE_COMBINED = 1;
    static final int VIEW_TYPE_7_SHIP_FLEET = 2;

    private List<Deck> deckList;
    private OnRecyclerViewClickListener listener;
    private Realm realm;

    public DeckTabsRecyclerViewAdapter(OnRecyclerViewClickListener listener) {
        realm = Realm.getDefaultInstance();
        deckList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public DeckTabsRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_deck, viewGroup, false);
        } else if (viewType == VIEW_TYPE_COMBINED) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_deck_combined, viewGroup, false);
        } else if (viewType == VIEW_TYPE_7_SHIP_FLEET) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_deck_7ship_fleet, viewGroup, false);
        } else {
            throw new IllegalArgumentException("invalid view type:" + viewType);
        }
        return new DeckTabsRecyclerViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(DeckTabsRecyclerViewHolder viewHolder, int position) {
        viewHolder.bind(deckList.get(position), viewHolder.getItemViewType());
    }

    @Override
    public int getItemViewType(int position) {
        if (deckList.get(position) == null) {
            return VIEW_TYPE_COMBINED;
        } else if (position == 2 && deckList.get(position).getShipId().get(6) != -1) {
            return VIEW_TYPE_7_SHIP_FLEET;
        } else {
            return VIEW_TYPE_NORMAL;
        }
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

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        realm.close();
    }

    class DeckTabsRecyclerViewHolder extends RecyclerView.ViewHolder {
        final int maxShipNum;

        //通常艦隊
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

        //第二艦隊
        private TextView[] names2;
        private TextView[] conds2;
        private TextView[] lvs2;
        private TextView[] states2;
        private ProgressBar[] progressBars2;
        private TextView[] hps2;
        private TextView[] fuelDescriptions2;
        private TextView[] fuels2;
        private TextView[] bullDescriptions2;
        private TextView[] bulls2;
        private LinearLayout[] equipmentLayouts2;
        private ImageView[][] slots2;
        private TextView[] slotExDescriptions2;
        private ImageView[] slotExIcons2;
        private Button[] detailButtons2;

        //共通
        private TextView seiku;
        private TextView touchStartRate;
        private TextView sakuteki33;
        private EditText junction;
        private TextView levelSum;
        private TextView condRecoveryTime;

        private DeckTabsRecyclerViewHolder(View rootView, int viewType) {
            super(rootView);

            if (viewType == VIEW_TYPE_7_SHIP_FLEET) {
                maxShipNum = 7;
            } else {
                maxShipNum = 6;
            }
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
            final String packageName = App.getInstance().getPackageName();

            for (int i = 1; i <= maxShipNum; i++) {
                String name = "layout_ship" + i;
                int strId = res.getIdentifier(name, "id", packageName);
                ViewGroup shipLayout = findById(rootView, strId);

                names[i - 1] = findById(shipLayout, R.id.name);
                conds[i - 1] = findById(shipLayout, R.id.cond);
                lvs[i - 1] = findById(shipLayout, R.id.lv);
                states[i - 1] = findById(shipLayout, R.id.state);
                progressBars[i - 1] = findById(shipLayout, R.id.progressBar);
                hps[i - 1] = findById(shipLayout, R.id.hp);
                fuelDescriptions[i - 1] = findById(shipLayout, R.id.fuelText);
                fuels[i - 1] = findById(shipLayout, R.id.fuel);
                bullDescriptions[i - 1] = findById(shipLayout, R.id.bullText);
                bulls[i - 1] = findById(shipLayout, R.id.bull);
                equipmentLayouts[i - 1] = findById(shipLayout, R.id.equipments);

                for (int j = 1; j <= 4; j++) {
                    name = "slot" + j;
                    strId = res.getIdentifier(name, "id", packageName);
                    slots[i - 1][j - 1] = findById(shipLayout, strId);
                }

                slotExDescriptions[i - 1] = findById(shipLayout, R.id.slotEx);
                slotExIcons[i - 1] = findById(shipLayout, R.id.slotExIcon);
                detailButtons[i - 1] = findById(shipLayout, R.id.detail);
            }

            //共通
            seiku = findById(rootView, R.id.seiku);
            touchStartRate = findById(rootView, R.id.touchStartRate);
            sakuteki33 = findById(rootView, R.id.sakuteki33);
            junction = findById(rootView, R.id.junction);
            levelSum = findById(rootView, R.id.levelSum);
            condRecoveryTime = findById(rootView, R.id.condRecoveryTime);

            if (viewType != VIEW_TYPE_COMBINED) {
                return;
            }

            names2 = new TextView[maxShipNum];
            conds2 = new TextView[maxShipNum];
            lvs2 = new TextView[maxShipNum];
            states2 = new TextView[maxShipNum];
            progressBars2 = new ProgressBar[maxShipNum];
            hps2 = new TextView[maxShipNum];
            fuelDescriptions2 = new TextView[maxShipNum];
            fuels2 = new TextView[maxShipNum];
            bullDescriptions2 = new TextView[maxShipNum];
            bulls2 = new TextView[maxShipNum];
            equipmentLayouts2 = new LinearLayout[maxShipNum];
            slots2 = new ImageView[maxShipNum][maxSlotNum];
            slotExDescriptions2 = new TextView[maxShipNum];
            slotExIcons2 = new ImageView[maxShipNum];
            detailButtons2 = new Button[maxShipNum];

            for (int i = 1; i <= maxShipNum; i++) {
                String name = "layout_ship2" + i;
                int strId = res.getIdentifier(name, "id", packageName);
                ViewGroup shipLayout = findById(rootView, strId);

                names2[i - 1] = findById(shipLayout, R.id.name);
                conds2[i - 1] = findById(shipLayout, R.id.cond);
                lvs2[i - 1] = findById(shipLayout, R.id.lv);
                states2[i - 1] = findById(shipLayout, R.id.state);
                progressBars2[i - 1] = findById(shipLayout, R.id.progressBar);
                hps2[i - 1] = findById(shipLayout, R.id.hp);
                fuelDescriptions2[i - 1] = findById(shipLayout, R.id.fuelText);
                fuels2[i - 1] = findById(shipLayout, R.id.fuel);
                bullDescriptions2[i - 1] = findById(shipLayout, R.id.bullText);
                bulls2[i - 1] = findById(shipLayout, R.id.bull);
                equipmentLayouts2[i - 1] = findById(shipLayout, R.id.equipments);

                for (int j = 1; j <= 4; j++) {
                    name = "slot" + j;
                    strId = res.getIdentifier(name, "id", packageName);
                    slots2[i - 1][j - 1] = findById(shipLayout, strId);
                }

                slotExDescriptions2[i - 1] = findById(shipLayout, R.id.slotEx);
                slotExIcons2[i - 1] = findById(shipLayout, R.id.slotExIcon);
                detailButtons2[i - 1] = findById(shipLayout, R.id.detail);
            }
        }

        private void bind(Deck deck, int viewType) {
            final boolean isCombined = (viewType == VIEW_TYPE_COMBINED);

            final Deck deck1;
            if (isCombined) {
                deck1 = DeckManager.INSTANCE.getDeck(1);
            } else {
                deck1 = deck;
            }

            List<Integer> shipId = deck1.getShipId();

            for (int i = 0; i < maxShipNum; i++) {
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
                final MyShip myShip = realm.where(MyShip.class)
                        .equalTo("id", id)
                        .findFirst();

                names[i].setVisibility(View.VISIBLE);
                MstShip mstShip = realm.where(MstShip.class)
                        .equalTo("id", myShip.getShipId())
                        .findFirst();
                names[i].setText(mstShip.getName());
                names[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v4.app.DialogFragment dialogFragment = ShipParamDialogFragment.newInstance(id, deck1.getId());
                        listener.onRecyclerViewClicked(dialogFragment, null);
                    }
                });

                lvs[i].setVisibility(View.VISIBLE);
                lvs[i].setText("Lv:" + String.valueOf(myShip.getLv()));

                states[i].setVisibility(View.VISIBLE);
                if (deck1.getMission()
                        .get(0) == 1 || deck1.getMission()
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
                        slots[i][j].setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
                        continue;
                    }

                    if (myShip.getSlot()
                            .get(j)
                            .getValue() == -1) {
                        slots[i][j].setImageResource(EquipType3.EMPTY.getImageId());
                        continue;
                    }

                    mySlotItem = realm.where(MySlotItem.class)
                            .equalTo("id", myShip.getSlot()
                                    .get(j)
                                    .getValue())
                            .findFirst();
                    mstSlotitem = realm.where(MstSlotitem.class)
                            .equalTo("id", mySlotItem.getMstId())
                            .findFirst();

                    slots[i][j].setImageResource(EquipType3.toEquipType3(mstSlotitem.getType()
                            .get(3)
                            .getValue())
                            .getImageId());
                }

                slotExDescriptions[i].setVisibility(View.VISIBLE);

                slotExIcons[i].setVisibility(View.VISIBLE);
                mySlotItem = realm.where(MySlotItem.class)
                        .equalTo("id", myShip.getSlotEx())
                        .findFirst();
                if (mySlotItem != null) {
                    mstSlotitem = realm.where(MstSlotitem.class)
                            .equalTo("id", mySlotItem.getMstId())
                            .findFirst();
                    slotExIcons[i].setImageResource(EquipType3.toEquipType3(mstSlotitem.getType()
                            .get(3)
                            .getValue())
                            .getImageId());
                } else {
                    if (myShip.getSlotEx() == 0) {
                        slotExIcons[i].setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
                    } else if (myShip.getSlotEx() == -1) {
                        slotExIcons[i].setImageResource(EquipType3.EMPTY.getImageId());
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
                        intent.putExtra("deckId", deck1.getId());

                        Configuration config = App.getInstance()
                                .getResources()
                                .getConfiguration();
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


            final Deck deck2;
            if (viewType != VIEW_TYPE_COMBINED) {
                deck2 = null;
            } else {
                //空きの場合
                deck2 = DeckManager.INSTANCE.getDeck(2);
                List<Integer> shipId2 = deck2.getShipId();
                Logger.d("shipIdSize", String.valueOf(shipId2.size()));

                for (int i = 0; i < 6; i++) {
                    if (shipId2.get(i) == -1) {
                        names2[i].setVisibility(View.INVISIBLE);
                        conds2[i].setVisibility(View.INVISIBLE);
                        lvs2[i].setVisibility(View.INVISIBLE);
                        states2[i].setVisibility(View.INVISIBLE);
                        progressBars2[i].setVisibility(View.INVISIBLE);
                        hps2[i].setVisibility(View.INVISIBLE);
                        fuelDescriptions2[i].setVisibility(View.INVISIBLE);
                        fuels2[i].setVisibility(View.INVISIBLE);
                        bullDescriptions2[i].setVisibility(View.INVISIBLE);
                        bulls2[i].setVisibility(View.INVISIBLE);
                        for (int j = 0; j < 4; j++) {
                            slots2[i][j].setVisibility(View.INVISIBLE);
                        }
                        slotExDescriptions2[i].setVisibility(View.INVISIBLE);
                        slotExIcons2[i].setVisibility(View.INVISIBLE);
                        detailButtons2[i].setVisibility(View.GONE);
                        continue;
                    }

                    final int id = shipId2.get(i);
                    final MyShip myShip = realm.where(MyShip.class)
                            .equalTo("id", id)
                            .findFirst();

                    names2[i].setVisibility(View.VISIBLE);
                    MstShip mstShip = realm.where(MstShip.class)
                            .equalTo("id", myShip.getShipId())
                            .findFirst();
                    names2[i].setText(mstShip.getName());
                    names2[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.support.v4.app.DialogFragment dialogFragment = ShipParamDialogFragment.newInstance(id, deck2.getId());
                            listener.onRecyclerViewClicked(dialogFragment, null);
                        }
                    });

                    lvs2[i].setVisibility(View.VISIBLE);
                    lvs2[i].setText("Lv:" + String.valueOf(myShip.getLv()));

                    states2[i].setVisibility(View.VISIBLE);
                    if (deck2.getMission()
                            .get(0) == 1 || deck2.getMission()
                            .get(0) == 3) {
                        states2[i].setText("遠征");
                        //インディゴ
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.expedition));
                    } else if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                        states2[i].setText("退避");
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.escort));
                    } else if (DockTimer.INSTANCE.getShipId(1) == myShip.getId() || DockTimer.INSTANCE.getShipId(2) == myShip.getId() || DockTimer.INSTANCE.getShipId(3) == myShip.getId() || DockTimer.INSTANCE.getShipId(4) == myShip.getId()) {
                        states2[i].setText("入渠");
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.docking));
                    } else if (myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                        states2[i].setText("大破");
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.heavy_damage));
                    } else if (myShip.getNowhp() <= myShip.getMaxhp() / 2) {
                        states2[i].setText("中破");
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.moderate_damage));
                    } else if (myShip.getNowhp() <= myShip.getMaxhp() * 3 / 4) {
                        states2[i].setText("小破");
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.minor_damage));
                    } else if (myShip.getNowhp() < myShip.getMaxhp()) {
                        states2[i].setText("健在");
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.good_health));
                    } else {
                        states2[i].setText("無傷");
                        states2[i].setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.undamaged));
                    }

                    MySlotItem mySlotItem;
                    MstSlotitem mstSlotitem;
                    for (int j = 0; j < 4; j++) {
                        slots2[i][j].setVisibility(View.VISIBLE);

                        if (j >= myShip.getSlotnum()) {
                            slots2[i][j].setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
                            continue;
                        }

                        if (myShip.getSlot()
                                .get(j)
                                .getValue() == -1) {
                            slots2[i][j].setImageResource(EquipType3.EMPTY.getImageId());
                            continue;
                        }

                        mySlotItem = realm.where(MySlotItem.class)
                                .equalTo("id", myShip.getSlot()
                                        .get(j)
                                        .getValue())
                                .findFirst();
                        mstSlotitem = realm.where(MstSlotitem.class)
                                .equalTo("id", mySlotItem.getMstId())
                                .findFirst();

                        slots2[i][j].setImageResource(EquipType3.toEquipType3(mstSlotitem.getType()
                                .get(3)
                                .getValue())
                                .getImageId());
                    }

                    slotExDescriptions2[i].setVisibility(View.VISIBLE);

                    slotExIcons2[i].setVisibility(View.VISIBLE);
                    mySlotItem = realm.where(MySlotItem.class)
                            .equalTo("id", myShip.getSlotEx())
                            .findFirst();
                    if (mySlotItem != null) {
                        mstSlotitem = realm.where(MstSlotitem.class)
                                .equalTo("id", mySlotItem.getMstId())
                                .findFirst();
                        slotExIcons2[i].setImageResource(EquipType3.toEquipType3(mstSlotitem.getType()
                                .get(3)
                                .getValue())
                                .getImageId());
                    } else {
                        if (myShip.getSlotEx() == 0) {
                            slotExIcons2[i].setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
                        } else if (myShip.getSlotEx() == -1) {
                            slotExIcons2[i].setImageResource(EquipType3.EMPTY.getImageId());
                        }
                    }

                    equipmentLayouts2[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.support.v4.app.DialogFragment dialogFragment = EquipmentDialogFragment.newInstance(id);
                            listener.onRecyclerViewClicked(dialogFragment, null);
                        }
                    });

                    progressBars2[i].setVisibility(View.VISIBLE);
                    progressBars2[i].setMax(myShip.getMaxhp());
                    progressBars2[i].setProgress(myShip.getNowhp());

                    hps2[i].setVisibility(View.VISIBLE);
                    hps2[i].setText(Integer.toString(myShip.getNowhp()) + "/" + Integer.toString(myShip.getMaxhp()));

                    fuelDescriptions2[i].setVisibility(View.VISIBLE);

                    fuels2[i].setVisibility(View.VISIBLE);
                    int fuelMax = mstShip.getFuelMax();
                    int fuelNow = myShip.getFuel();
                    if (fuelNow == fuelMax) {
                        fuels2[i].setText("||||||||||");
                        //緑
                        fuels2[i].setTextColor(Color.rgb(59, 175, 117));
                    } else if (fuelNow >= fuelMax * 8 / 9) {
                        fuels2[i].setText("|||||||||");
                        //黄色
                        fuels2[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (fuelNow >= fuelMax * 7 / 9) {
                        fuels2[i].setText("||||||||");
                        //黄色
                        fuels2[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (fuelNow >= fuelMax * 6 / 9) {
                        fuels2[i].setText("|||||||");
                        //オレンジ
                        fuels2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 5 / 9) {
                        fuels2[i].setText("||||||");
                        //オレンジ
                        fuels2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 4 / 9) {
                        fuels2[i].setText("|||||");
                        //オレンジ
                        fuels2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 3 / 9) {
                        fuels2[i].setText("||||");
                        //オレンジ
                        fuels2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (fuelNow >= fuelMax * 2 / 9) {
                        fuels2[i].setText("|||");
                        fuels2[i].setTextColor(Color.RED);
                    } else if (fuelNow >= fuelMax * 1 / 9) {
                        fuels2[i].setText("||");
                        fuels2[i].setTextColor(Color.RED);
                    } else if (fuelNow > 0) {
                        fuels2[i].setText("|");
                        fuels2[i].setTextColor(Color.RED);
                    } else {
                        fuels2[i].setText("-");
                        //グレー
                        fuels2[i].setTextColor(Color.rgb(118, 118, 118));
                    }

                    bullDescriptions2[i].setVisibility(View.VISIBLE);

                    bulls2[i].setVisibility(View.VISIBLE);
                    int bullMax = mstShip.getBullMax();
                    int bullNow = myShip.getBull();
                    if (bullNow == bullMax) {
                        bulls2[i].setText("||||||||||");
                        //緑
                        bulls2[i].setTextColor(Color.rgb(59, 175, 117));
                    } else if (bullNow >= bullMax * 8 / 9) {
                        bulls2[i].setText("|||||||||");
                        //黄色
                        bulls2[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (bullNow >= bullMax * 7 / 9) {
                        bulls2[i].setText("||||||||");
                        //黄色
                        bulls2[i].setTextColor(Color.rgb(237, 185, 24));
                    } else if (bullNow >= bullMax * 6 / 9) {
                        bulls2[i].setText("|||||||");
                        //オレンジ
                        bulls2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 5 / 9) {
                        bulls2[i].setText("||||||");
                        //オレンジ
                        bulls2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 4 / 9) {
                        bulls2[i].setText("|||||");
                        //オレンジ
                        bulls2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 3 / 9) {
                        bulls2[i].setText("||||");
                        //オレンジ
                        bulls2[i].setTextColor(Color.rgb(255, 140, 0));
                    } else if (bullNow >= bullMax * 2 / 9) {
                        bulls2[i].setText("|||");
                        bulls2[i].setTextColor(Color.RED);
                    } else if (bullNow >= bullMax * 1 / 9) {
                        bulls2[i].setText("||");
                        bulls2[i].setTextColor(Color.RED);
                    } else if (bullNow > 0) {
                        bulls2[i].setText("|");
                        bulls2[i].setTextColor(Color.RED);
                    } else {
                        bulls2[i].setText("-");
                        //グレー
                        bulls2[i].setTextColor(Color.rgb(118, 118, 118));
                    }

                    conds2[i].setVisibility(View.VISIBLE);
                    conds2[i].setText("cond:" + Integer.toString(myShip.getCond()));
                    //condの値で色分けする
                    if (myShip.getCond() >= 50) {
                        conds2[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.high_morale));
                    } else if (myShip.getCond() >= 40) {
                        conds2[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.normal_cond));
                    } else if (myShip.getCond() >= 30) {
                        conds2[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.slightly_fatigued));
                    } else if (myShip.getCond() >= 20) {
                        conds2[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.moderately_fatigued));
                    } else {
                        conds2[i].setTextColor(ContextCompat.getColor(App.getInstance(), R.color.seriously_fatigued));
                    }

                    detailButtons2[i].setVisibility(View.VISIBLE);
                    detailButtons2[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(App.getInstance(), ShipDetailActivity.class);
                            intent.putExtra("shipId", id);
                            intent.putExtra("deckId", deck2.getId());

                            Configuration config = App.getInstance()
                                    .getResources()
                                    .getConfiguration();
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
            }

            int seikuValue = DeckUtility.INSTANCE.getSeiku(deck1);
            int touchStartRateVal = DeckUtility.INSTANCE.getTouchStartRate(deck1);
            int levelSumVal = deck1.getLevelSum();
            if (isCombined) {
                seiku.setText(seikuValue + "(" + (seikuValue + DeckUtility.INSTANCE.getSeiku(deck2)) + ")");
                touchStartRate.setText(touchStartRateVal + "(" + (touchStartRateVal + DeckUtility.INSTANCE.getTouchStartRate(deck2)) + ")" + "%");
                levelSum.setText(String.valueOf(levelSumVal + deck2.getLevelSum()));
                try {
                    if (deck1.getCondRecoveryTime().isEmpty()) {
                        if (deck2.getCondRecoveryTime().isEmpty()) {
                            condRecoveryTime.setText("");
                        } else {
                            condRecoveryTime.setText(deck2.getCondRecoveryTime());
                        }
                    } else if (deck2.getCondRecoveryTime().isEmpty()) {
                        condRecoveryTime.setText(deck1.getCondRecoveryTime());
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        Date date1 = sdf.parse(deck1.getCondRecoveryTime());
                        Date date2 = sdf.parse(deck2.getCondRecoveryTime());
                        if (date1.after(date2)) {
                            condRecoveryTime.setText(deck1.getCondRecoveryTime());
                        } else {
                            condRecoveryTime.setText(deck2.getCondRecoveryTime());
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                seiku.setText(String.valueOf(seikuValue));
                touchStartRate.setText(touchStartRateVal + "%");
                levelSum.setText(String.valueOf(levelSumVal));
                condRecoveryTime.setText(deck1.getCondRecoveryTime());
            }

            sakuteki33.setText(String.valueOf(DeckUtility.INSTANCE.getSakuteki33(deck1, 1)));
            junction.setText("1");
            junction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        //IMEを閉じる
                        InputMethodManager inputMethodManager = (InputMethodManager) App.getInstance()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        //索敵(33)
                        Editable getText = junction.getText();
                        float junctionFloat = 1f;
                        try {
                            junctionFloat = Float.parseFloat(getText.toString());
                        } catch (NumberFormatException e) {
                            junction.setText("1");
                        }
                        sakuteki33.setText(String.valueOf(DeckUtility.INSTANCE.getSakuteki33(deck1, junctionFloat)));
                    }
                    return true;
                }
            });
        }
    }
}