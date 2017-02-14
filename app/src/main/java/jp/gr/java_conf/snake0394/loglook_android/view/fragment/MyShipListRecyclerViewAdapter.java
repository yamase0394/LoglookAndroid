package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.ShipUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3;

import static butterknife.ButterKnife.findById;

/**
 * Created by snake0394 on 2016/12/08.
 */
public class MyShipListRecyclerViewAdapter extends RecyclerView.Adapter<MyShipListRecyclerViewAdapter.MyShipListRecyclerViewHolder> {

    private SortedList<MyShipListItem> sortedList;
    private final FragmentManager fragmentManager;
    private Map<Integer, List<Label>> toLabelMap;
    private List<Label> labelList;
    private OnRecyclerViewItemClickListener listener;

    public MyShipListRecyclerViewAdapter(FragmentManager fragmentManager, String sortType, String order, OnRecyclerViewItemClickListener listener, Map<Integer, List<Label>> toLabelMap, List<Label> labelList) {
        sortedList = new SortedList<>(MyShipListItem.class, new SortedListCallcack(this, sortType, order));
        this.fragmentManager = fragmentManager;
        this.toLabelMap = toLabelMap;
        this.labelList = labelList;
        this.listener = listener;
    }

    @Override
    public MyShipListRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.cardview_ship, viewGroup, false);
        return new MyShipListRecyclerViewHolder(itemView, fragmentManager, listener);
    }

    @Override
    public void onBindViewHolder(MyShipListRecyclerViewHolder sampleViewHolder, int i) {
        MyShipListItem data = sortedList.get(i);
        if (data != null) {
            sampleViewHolder.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public MyShipListItem getItem(int position) {
        return sortedList.get(position);
    }

    public void setItems(List<MyShip> newItemList) {

        //newItemListにないものを削除
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            final MyShip myShip = sortedList.get(i)
                                            .getMyShip();

            if (!newItemList.contains(myShip)) {
                sortedList.removeItemAt(i)
                          .getMyShip()
                          .getId();
            }
        }

        for (MyShip newItem : newItemList) {
            if (toLabelMap.containsKey(newItem.getId())) {
                sortedList.add(new MyShipListItem(newItem, toLabelMap.get(newItem.getId())));
                continue;
            }
            sortedList.add(new MyShipListItem(newItem));
        }
    }

    public void addLabel(int position, Label label) {
        MyShipListItem myShipListItem = sortedList.get(position);

        if (labelList.contains(label)) {
            labelList.set(labelList.indexOf(label), label);

            for (Map.Entry<Integer, List<Label>> entry : toLabelMap.entrySet()) {
                if (entry.getValue()
                         .contains(label)) {
                    entry.getValue()
                         .set(entry.getValue()
                                   .indexOf(label), label);
                }
            }
            notifyDataSetChanged();
        } else {
            labelList.add(label);
        }

        if (myShipListItem.getLabelList()
                          .contains(label)) {
            myShipListItem.getLabelList()
                          .set(myShipListItem.getLabelList()
                                             .indexOf(label), label);
        } else {
            myShipListItem.getLabelList()
                          .add(label);
        }

        List<Label> newLabelList = new ArrayList<>();
        for (Label l : labelList) {
            if (myShipListItem.getLabelList()
                              .contains(l)) {
                newLabelList.add(l);
            }
        }
        myShipListItem.getLabelList()
                      .clear();
        myShipListItem.getLabelList()
                      .addAll(newLabelList);

        toLabelMap.put(myShipListItem.getMyShip()
                                     .getId(), myShipListItem.getLabelList());

        notifyItemChanged(position);
    }


    public void removeLabel(int position, Label label) {
        MyShipListItem myShipListItem = sortedList.get(position);

        myShipListItem.getLabelList()
                      .remove(label);

        if (myShipListItem.getLabelList()
                          .isEmpty()) {
            toLabelMap.remove(myShipListItem.getMyShip()
                                            .getId());
        } else {
            toLabelMap.put(myShipListItem.getMyShip()
                                         .getId(), myShipListItem.getLabelList());
        }

        //削除されたラベルが他のshipIdにも関連付けるられていない場合labelSetから削除する
        Set<Label> existingLabelSet = new HashSet<>();
        for (List<Label> list : toLabelMap.values()) {
            existingLabelSet.addAll(list);
        }
        labelList.retainAll(existingLabelSet);

        notifyItemChanged(position);
    }

    public List<Label> getLabelList() {
        return labelList;
    }

    public Map<Integer, List<Label>> getToLabelMap() {
        return toLabelMap;
    }

    public List<MyShipListItem> getItemList() {
        List<MyShipListItem> list = new ArrayList<>();
        for (int i = 0; i < sortedList.size(); i++) {
            list.add(sortedList.get(i));
        }
        return list;
    }

    public static class MyShipListItem {
        private MyShip myShip;
        private List<Label> labelList;

        public MyShipListItem(MyShip myShip) {
            this.myShip = myShip;
            labelList = new ArrayList<>();
        }

        public MyShipListItem(MyShip myShip, List<Label> label) {
            this(myShip);
            this.labelList = label;
        }

        public MyShip getMyShip() {
            return myShip;
        }

        public List<Label> getLabelList() {
            return labelList;
        }

    }

    /**
     * Labelの同一性はnameフィールドで判定されます
     */
    public static class Label {
        private String name;
        private int color;

        Label(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Label label = (Label) o;

            return new EqualsBuilder().append(name, label.name)
                                      .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(name)
                                              .toHashCode();
        }
    }

    private static class SortedListCallcack extends SortedList.Callback<MyShipListItem> {

        private RecyclerView.Adapter adapter;
        private String sortType;
        private String order;

        SortedListCallcack(@NonNull RecyclerView.Adapter adapter, String sortType, String order) {
            this.adapter = adapter;
            this.sortType = sortType;
            this.order = order;
        }

        @Override
        public int compare(MyShipListItem item1, MyShipListItem item2) {
            MyShip data1 = item1.getMyShip();
            MyShip data2 = item2.getMyShip();
            int result;
            switch (sortType) {
                case "ID":
                    result = data1.getId() - data2.getId();
                    break;
                case "Lv":
                    result = data1.getLv() - data2.getLv();
                    break;
                case "cond":
                    result = data1.getCond() - data2.getCond();
                    break;
                case "砲撃戦火力":
                    result = (int) (ShipUtility.getShellingBasicAttackPower(data1) - ShipUtility.getShellingBasicAttackPower(data2));
                    break;
                case "雷撃戦火力":
                    result = (int) (ShipUtility.getTorpedoSalvoBasicAttackPower(data1) - ShipUtility.getTorpedoSalvoBasicAttackPower(data2));
                    break;
                case "夜戦火力":
                    result = (int) (ShipUtility.getNightBattleBasicAttackPower(data1) - ShipUtility.getNightBattleBasicAttackPower(data2));
                    break;
                case "対潜":
                    result = data1.getTaisen()
                                  .get(0) - data2.getTaisen()
                                                 .get(0);
                    break;
                default:
                    return 0;
            }

            switch (order) {
                case "降順":
                    result *= -1;
                    break;
            }
            return result;
        }

        @Override
        public void onInserted(int position, int count) {
            adapter.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            adapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            adapter.notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(MyShipListItem oldData, MyShipListItem newData) {
            return oldData.getMyShip()
                          .getId() == newData.getMyShip()
                                             .getId();
        }

        @Override
        public boolean areItemsTheSame(MyShipListItem data1, MyShipListItem data2) {
            return data1.getMyShip()
                        .getId() == data2.getMyShip()
                                         .getId();
        }
    }

    static class MyShipListRecyclerViewHolder extends RecyclerView.ViewHolder {
        private FragmentManager fragmentManager;
        private final OnRecyclerViewItemClickListener listener;

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.lv)
        TextView lv;
        @BindView(R.id.state)
        TextView state;
        private List<ImageView> slotList;
        @BindView(R.id.imageview_slot_ex)
        ImageView slotEx;
        @BindView(R.id.equipments)
        LinearLayout equipments;
        @BindView(R.id.hpBar)
        ProgressBar hpBar;
        @BindView(R.id.hp)
        TextView hp;
        @BindView(R.id.cond)
        TextView cond;
        @BindView(R.id.text__shelling_basic_attack_power)
        TextView shellingBasicAttackPowerTextView;
        @BindView(R.id.text_torpedo_basic_attack_power)
        TextView torpedoBasicAttackPowerTextView;
        @BindView(R.id.text_night_battle_basic_attack_power)
        TextView nightBattleBasicAttackPowerTextView;
        @BindView(R.id.text_ship_asw)
        TextView shipASWTextView;
        @BindView(R.id.layout_label)
        LinearLayout labelLayout;

        public MyShipListRecyclerViewHolder(View rootView, FragmentManager fragmentManager, OnRecyclerViewItemClickListener listener) {
            super(rootView);
            this.fragmentManager = fragmentManager;
            this.listener = listener;
            ButterKnife.bind(this, rootView);
            slotList = new ArrayList<>();
            slotList.add((ImageView) findById(rootView, R.id.slot1));
            slotList.add((ImageView) findById(rootView, R.id.slot2));
            slotList.add((ImageView) findById(rootView, R.id.slot3));
            slotList.add((ImageView) findById(rootView, R.id.slot4));
        }

        public void bind(@NonNull final MyShipListRecyclerViewAdapter.MyShipListItem myShipListItem) {
            final MyShip myShip = myShipListItem.getMyShip();

            name.setText(myShip.getName());

            Deck deck = null;
            //Deckに入っているか
            for (int i = 1; i <= DeckManager.INSTANCE.getDeckNum(); i++) {
                Deck temp = DeckManager.INSTANCE.getDeck(i);
                if (temp.getShipId()
                        .contains(myShip.getId())) {
                    deck = temp;
                    break;
                }
            }

            if (deck != null) {
                final int deckId = deck.getId();
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment dialogFragment = ShipParamDialogFragment.newInstance(myShip.getId(), deckId);
                        dialogFragment.show(fragmentManager, "ShipParamDialog");
                    }
                });
            } else {
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment dialogFragment = ShipParamDialogFragment.newInstance(myShip.getId(), 0);
                        dialogFragment.show(fragmentManager, "ShipParamDialog");
                    }
                });
            }

            lv.setText("Lv:" + String.valueOf(myShip.getLv()));

            if (deck != null && (deck.getMission()
                                     .get(0) == 1 || deck.getMission()
                                                         .get(0) == 3)) {
                state.setText("遠征");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.expedition));
            } else if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                state.setText("退避");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.escort));
            } else if (DockTimer.INSTANCE.getShipId(1) == myShip.getId() || DockTimer.INSTANCE.getShipId(2) == myShip.getId() || DockTimer.INSTANCE.getShipId(3) == myShip.getId() || DockTimer.INSTANCE.getShipId(4) == myShip.getId()) {
                state.setText("入渠");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.docking));
            } else if (myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                state.setText("大破");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.heavy_damage));
            } else if (myShip.getNowhp() <= myShip.getMaxhp() / 2) {
                state.setText("中破");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.moderate_damage));
            } else if (myShip.getNowhp() <= myShip.getMaxhp() * 3 / 4) {
                state.setText("小破");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.minor_damage));
            } else if (myShip.getNowhp() < myShip.getMaxhp()) {
                state.setText("健在");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.good_health));
            } else {
                state.setText("無傷");
                state.setBackgroundColor(ContextCompat.getColor(App.getInstance(), R.color.undamaged));
            }

            for (int i = 0; i < slotList.size(); i++) {
                ImageView slotImage = slotList.get(i);
                if (i > myShip.getSlotnum() - 1) {
                    slotImage.setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
                } else if (myShip.getSlot()
                                 .get(i) == -1) {
                    slotImage.setImageResource(EquipType3.EMPTY.getImageId());
                } else {
                    MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlot()
                                                                                           .get(i));
                    if (mySlotItem != null) {
                        MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                        slotImage.setImageResource(EquipType3.toEquipType3(mstSlotitem.getType()
                                                                                      .get(3))
                                                             .getImageId());
                    } else {
                        slotImage.setImageResource(EquipType3.UNKNOWN.getImageId());
                    }
                }
            }

            if (MySlotItemManager.INSTANCE.contains(myShip.getSlotEx())) {
                MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlotEx());
                MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                slotEx.setImageResource(EquipType3.toEquipType3(mstSlotitem.getType()
                                                                           .get(3))
                                                  .getImageId());
            } else {
                if (myShip.getSlotEx() == 0) {
                    slotEx.setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
                } else if (myShip.getSlotEx() == -1) {
                    slotEx.setImageResource(EquipType3.EMPTY.getImageId());
                }
            }

            equipments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.DialogFragment dialogFragment = EquipmentDialogFragment.newInstance(myShip.getId());
                    dialogFragment.show(fragmentManager, "fragment_dialog");
                }
            });

            hpBar.setMax(myShip.getMaxhp());
            hpBar.setProgress(myShip.getNowhp());

            hp.setText(Integer.toString(myShip.getNowhp()) + "/" + Integer.toString(myShip.getMaxhp()));

            cond.setText(Integer.toString(myShip.getCond()));
            //condの値で色分けする
            if (myShip.getCond() >= 50) {
                //緑
                cond.setTextColor(ContextCompat.getColor(App.getInstance(), R.color.high_morale));
            } else if (myShip.getCond() >= 40) {
                //グレー
                cond.setTextColor(ContextCompat.getColor(App.getInstance(), R.color.normal_cond));
            } else if (myShip.getCond() >= 30) {
                //黄色
                cond.setTextColor(ContextCompat.getColor(App.getInstance(), R.color.slightly_fatigued));
            } else if (myShip.getCond() >= 20) {
                //オレンジ
                cond.setTextColor(ContextCompat.getColor(App.getInstance(), R.color.moderately_fatigued));
            } else {
                cond.setTextColor(ContextCompat.getColor(App.getInstance(), R.color.seriously_fatigued));
            }

            shellingBasicAttackPowerTextView.setText(String.valueOf((int) ShipUtility.getShellingBasicAttackPower(myShip)));

            torpedoBasicAttackPowerTextView.setText(String.valueOf((int) ShipUtility.getTorpedoSalvoBasicAttackPower(myShip)));

            nightBattleBasicAttackPowerTextView.setText(String.valueOf((int) ShipUtility.getNightBattleBasicAttackPower(myShip)));

            shipASWTextView.setText(String.valueOf(myShip.getTaisen()
                                                         .get(0)));

            labelLayout.removeAllViews();
            for (MyShipListRecyclerViewAdapter.Label label : myShipListItem.getLabelList()) {
                LinearLayout labelView = (LinearLayout) View.inflate(labelLayout.getContext(), R.layout.view_my_ship_list_label, null);
                TextView labelTextView = (TextView) labelView.findViewById(R.id.text_label_name);
                labelTextView.setText(label.getName());
                GradientDrawable bgShape = (GradientDrawable) labelTextView.getBackground();
                bgShape.setColor(label.getColor());
                labelLayout.addView(labelView);
            }
        }

        @OnLongClick({R.id.cardview, R.id.name, R.id.equipments})
        boolean onLongClick() {
            listener.onRecyclerViewItemClicked(getAdapterPosition());
            return false;
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClicked(int position);
    }
}