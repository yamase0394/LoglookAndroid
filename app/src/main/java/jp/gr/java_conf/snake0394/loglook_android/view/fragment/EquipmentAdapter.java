package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.SlotItemUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3;

/**
 * Created by snake0394 on 2016/12/08.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder> {

    private SortedList<ListItem> itemList;
    private SparseIntArray mountedEquip;

    public EquipmentAdapter(String sortType, String order) {
        itemList = new SortedList<>(ListItem.class, new SortedListCallcack(this, sortType, order));

        mountedEquip = new SparseIntArray();

        for (MyShip myShip : MyShipManager.INSTANCE.getMyShips()) {
            for (int id : myShip.getSlot()) {
                if (MySlotItemManager.INSTANCE.contains(id)) {
                    mountedEquip.put(id, myShip.getId());
                }
            }
            if (MySlotItemManager.INSTANCE.contains(myShip.getSlotEx())) {
                mountedEquip.put(myShip.getSlotEx(), myShip.getId());
            }
        }
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.layout_equipment_cardview, viewGroup, false);
        return new EquipmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder sampleViewHolder, int i) {
        ListItem listItem = itemList.get(i);
        sampleViewHolder.bind(listItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<MySlotItem> newItemList) {

        //newItemListに含まれていない装備を削除
        /*
        itemList:
        for (int i = itemList.size() - 1; i >= 0; i--) {
            ListItem listItem = itemList.get(i);
            MySlotItem listSlot = listItem.mySlotItem;

            //newItemListに含まれているか
            for (MySlotItem newSlot : newItemList) {
                //マスターID、改修度、熟練度で比較
                if (Objects.equals(listSlot.getMstId(), newSlot.getMstId()) && Objects.equals(listSlot.getLevel(), newSlot.getLevel()) && Objects.equals(listSlot.getAlv(), newSlot.getAlv())) {
                    continue itemList;
                }
            }

            //個数で削除か数を減らすか判定
            if (listItem.num > 1) {
                int shipId = mountedEquip.get(listSlot.getId(), -1);
                listItem.equipShipSet.remove(shipId);
                listItem.num--;
            } else {
                itemList.removeItemAt(i);
            }
        }*/
        itemList.clear();
        //this.totalEquipNum = new SparseIntArray();

        newItemList:
        for (MySlotItem newSlot : newItemList) {
            for (int i = 0; i < itemList.size(); i++) {
                ListItem listItem = itemList.get(i);
                MySlotItem listSlot = listItem.mySlotItem;
                //マスターID、改修度、熟練度で比較
                if (Objects.equals(listSlot.getMstId(), newSlot.getMstId()) && Objects.equals(listSlot.getLevel(), newSlot.getLevel()) && Objects.equals(listSlot.getAlv(), newSlot.getAlv())) {
                    listItem.num++;

                    int shipId = mountedEquip.get(newSlot.getId(), -1);
                    if (!Objects.equals(shipId, -1)) {
                        listItem.putShipId(shipId);
                    }

                    continue newItemList;
                }
            }
            ListItem listItem = new ListItem(newSlot);
            int shipId = mountedEquip.get(newSlot.getId(), -1);
            if (!Objects.equals(shipId, -1)) {
                listItem.putShipId(shipId);
            }
            itemList.add(listItem);
        }
    }

    private static class ListItem {
        MySlotItem mySlotItem;
        int num;
        Multiset<Integer> equipShipSet;

        ListItem(MySlotItem mySlotItem) {
            this.mySlotItem = mySlotItem;
            this.num = 1;
        }

        void putShipId(int myShipId) {
            if (Objects.equals(this.equipShipSet, null)) {
                this.equipShipSet = TreeMultiset.create(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer data1, Integer data2) {
                        MyShip myShip1 = MyShipManager.INSTANCE.getMyShip(data1);
                        MyShip myShip2 = MyShipManager.INSTANCE.getMyShip(data2);
                        //レベルの降順
                        int result = myShip2.getLv() - myShip1.getLv();
                        if (result == 0) {
                            //マスターIDの降順
                            result = myShip2.getShipId() - myShip1.getShipId();
                            if (result == 0) {
                                //艦娘IDの昇順
                                result = myShip1.getId() - myShip2.getId();
                            }
                        }
                        return result;
                    }
                });
            }
            this.equipShipSet.add(myShipId);
        }
    }

    private static class SortedListCallcack extends SortedList.Callback<ListItem> {

        private RecyclerView.Adapter adapter;
        private String sortType;
        private String order;

        SortedListCallcack(@NonNull RecyclerView.Adapter adapter, String sortType, String order) {
            this.adapter = adapter;
            this.sortType = sortType;
            this.order = order;
        }

        @Override
        public int compare(ListItem item1, ListItem item2) {
            MySlotItem data1 = item1.mySlotItem;
            MySlotItem data2 = item2.mySlotItem;
            MstSlotitem mst1 = MstSlotitemManager.INSTANCE.getMstSlotitem(data1.getMstId());
            MstSlotitem mst2 = MstSlotitemManager.INSTANCE.getMstSlotitem(data2.getMstId());

            float result = 0;

            switch (this.sortType) {
                case "名前":
                    result = mst1.getName()
                                 .compareTo(mst2.getName());
                    break;
                case "改修度":
                    result = data1.getLevel() - data2.getLevel();
                    break;
                case "ID":
                    result = data1.getMstId() - data2.getMstId();
                    break;
                case "火力":
                    result = mst1.getHoug() + SlotItemUtility.getShellingImprovementFirepower(mst1, data1.getLevel()) - mst2.getHoug() - SlotItemUtility.getShellingImprovementFirepower(mst2, data2.getLevel());
                    break;
                case "雷装":
                    result = mst1.getRaig() + SlotItemUtility.getTorpedoSalvoImprovementPower(mst1, data1.getLevel()) - mst2.getRaig() - SlotItemUtility.getTorpedoSalvoImprovementPower(mst2, data2.getLevel());
                    break;
                case "爆装":
                    result = mst1.getBaku() + SlotItemUtility.getImprovementDivebomb(mst1, data1.getLevel()) - mst2.getBaku() - SlotItemUtility.getImprovementDivebomb(mst2, data2.getLevel());
                    break;
                case "対空":
                    result = mst1.getTyku() + SlotItemUtility.getImprovementAA(mst1, data1.getLevel()) - mst2.getTyku() - SlotItemUtility.getImprovementAA(mst2, data2.getLevel());
                    break;
                case "対潜":
                    result = mst1.getTais() + SlotItemUtility.getImprovementASW(mst1, data1.getLevel()) - mst2.getTais() - SlotItemUtility.getImprovementASW(mst2, data2.getLevel());
                    break;
                case "索敵":
                    result = mst1.getSaku() + SlotItemUtility.getImprovementLOS(mst1, data1.getLevel()) - mst2.getSaku() - SlotItemUtility.getImprovementLOS(mst2, data2.getLevel());
                    break;
                case "命中":
                    result = mst1.getHoum() + SlotItemUtility.getImprovementAccuracy(mst1, data1.getLevel()) - mst2.getHoum() - SlotItemUtility.getImprovementAccuracy(mst2, data2.getLevel());
                    break;
                case "回避":
                    result = mst1.getHouk() + SlotItemUtility.getImprovementEvation(mst1, data1.getLevel()) - mst2.getHouk() - SlotItemUtility.getImprovementEvation(mst2, data2.getLevel());
                    break;
                case "装甲":
                    result = mst1.getSouk() + SlotItemUtility.getImprovementArmor(mst1, data1.getLevel()) - mst2.getSouk() - SlotItemUtility.getImprovementArmor(mst2, data2.getLevel());
                    break;
                case "加重対空":
                    result = SlotItemUtility.getAdjustedAA(mst1) + SlotItemUtility.getImprovementAdjustedAA(mst1, data1.getLevel()) - SlotItemUtility.getAdjustedAA(mst2) - SlotItemUtility.getImprovementAdjustedAA(mst2, data2.getLevel());
                    break;
                case "艦隊対空":
                    result = SlotItemUtility.getAdjustedFleetAA(mst1) + SlotItemUtility.getImprovementAdjustedFleetAA(mst1, data1.getLevel()) - SlotItemUtility.getAdjustedFleetAA(mst2) - SlotItemUtility.getImprovementAdjustedFleetAA(mst2, data2.getLevel());
                    break;
            }

            switch (order) {
                case "降順":
                    result *= -1;
                    break;
            }

            if (result == 0) {
                if (data1.getMstId() - data2.getMstId() == 0) {
                    result = data2.getLevel() - data1.getLevel();
                    if (result == 0) {
                        result = data2.getAlv() - data1.getAlv();
                    }
                }
            }

            if (result > 0) {
                return 1;
            } else if (result == 0) {
                return 0;
            } else {
                return -1;
            }
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
        public boolean areContentsTheSame(ListItem oldData, ListItem newData) {
            return Objects.equals(oldData.mySlotItem.getMstId(), newData.mySlotItem.getMstId()) && Objects.equals(oldData.mySlotItem.getLevel(), newData.mySlotItem.getLevel()) && Objects.equals(oldData.mySlotItem.getAlv(), newData.mySlotItem.getAlv());
        }

        @Override
        public boolean areItemsTheSame(ListItem item1, ListItem item2) {
            return Objects.equals(item1.mySlotItem.getMstId(), item2.mySlotItem.getMstId()) && Objects.equals(item1.mySlotItem.getLevel(), item2.mySlotItem.getLevel()) && Objects.equals(item1.mySlotItem.getAlv(), item2.mySlotItem.getAlv());
        }
    }

    public static class EquipmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.equipment)
        TextView equipment;
        @BindView(R.id.equipIcon)
        ImageView equipIcon;
        @BindView(R.id.alv)
        TextView alv;
        @BindView(R.id.improvement)
        TextView improvement;
        @BindView(R.id.textview_num)
        TextView num;
        @BindView(R.id.layout_firepower)
        LinearLayout firepowerLayout;
        @BindView(R.id.text_firepower)
        TextView firepower;
        @BindView(R.id.text_firepower_improve)
        TextView firepowerImprove;
        @BindView(R.id.layout_torpedo)
        LinearLayout torpedoLayout;
        @BindView(R.id.text_torpedo)
        TextView torpedo;
        @BindView(R.id.text_torpedo_improve)
        TextView torpedoImprove;
        @BindView(R.id.layout_divebomb)
        LinearLayout divebombLayout;
        @BindView(R.id.text_divebomb)
        TextView divebomb;
        @BindView(R.id.text_divebomb_improve)
        TextView divebombImprove;
        @BindView(R.id.layout_aa)
        LinearLayout aaLayout;
        @BindView(R.id.text_aa)
        TextView aa;
        @BindView(R.id.text_aa_improve)
        TextView aaImprove;
        @BindView(R.id.layout_asw)
        LinearLayout aswLayout;
        @BindView(R.id.text_asw)
        TextView asw;
        @BindView(R.id.text_asw_improve)
        TextView aswImprove;
        @BindView(R.id.layout_los)
        LinearLayout losLayout;
        @BindView(R.id.text_los)
        TextView los;
        @BindView(R.id.text_los_improve)
        TextView losImprove;
        @BindView(R.id.layout_accuracy)
        LinearLayout accuracyLayout;
        @BindView(R.id.text_accuracy)
        TextView accuracy;
        @BindView(R.id.text_accuracy_improve)
        TextView accuracyImprove;
        @BindView(R.id.layout_evation)
        LinearLayout evationLayout;
        @BindView(R.id.text_evation)
        TextView evation;
        @BindView(R.id.text_evation_improve)
        TextView evationImprove;
        @BindView(R.id.layout_range)
        LinearLayout rangeLayout;
        @BindView(R.id.text_range)
        TextView range;
        @BindView(R.id.text_range_improve)
        TextView rangeImprove;
        @BindView(R.id.layout_armor)
        LinearLayout armorLayout;
        @BindView(R.id.text_armor)
        TextView armor;
        @BindView(R.id.text_armor_improve)
        TextView armorImprove;
        @BindView(R.id.layout_adjusted_aa)
        LinearLayout adjustedAALayout;
        @BindView(R.id.text_adjusted_aa)
        TextView adjustedAA;
        @BindView(R.id.text_adjusted_aa_improve)
        TextView adjustedAAImprove;
        @BindView(R.id.layout_adjusted_fleet_aa)
        LinearLayout adjustedFleetAALayout;
        @BindView(R.id.text_adjusted_fleet_aa)
        TextView adjustedFleetAA;
        @BindView(R.id.text_adjusted_fleet_aa_improve)
        TextView adjustedFleetAAImprove;
        @BindView(R.id.layout_ships)
        FlexboxLayout shipsLayout;

        public EquipmentViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }

        public void bind(@NonNull final ListItem listItem) {
            MySlotItem mySlotItem = listItem.mySlotItem;
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

            equipment.setText(mstSlotitem.getName());

            equipIcon.setImageResource(EquipType3.toEquipType3(mstSlotitem.getType()
                                                                          .get(3))
                                                 .getImageId());

            alv.setVisibility(View.VISIBLE);
            switch (mySlotItem.getAlv()) {
                case 0:
                    alv.setVisibility(View.GONE);
                    alv.setText("");
                    break;
                case 1:
                    alv.setText("|");
                    alv.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 2:
                    alv.setText("||");
                    alv.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 3:
                    alv.setText("|||");
                    alv.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 4:
                    alv.setText("\\");
                    alv.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 5:
                    alv.setText("\\\\");
                    alv.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 6:
                    alv.setText("\\\\\\");
                    alv.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 7:
                    alv.setText(">>");
                    alv.setTextColor(Color.rgb(243, 213, 26));
                    break;
            }

            if (mySlotItem.getLevel() == 0) {
                improvement.setVisibility(View.GONE);
            } else {
                improvement.setVisibility(View.VISIBLE);
                improvement.setText("★" + mySlotItem.getLevel());
            }

            if (Objects.equals(listItem.equipShipSet, null)) {
                num.setText(listItem.num + "/" + listItem.num);
            } else {
                num.setText((listItem.num - listItem.equipShipSet.size()) + "/" + listItem.num);
            }

            int level = mySlotItem.getLevel();
            int base = mstSlotitem.getHoug();
            if (base == 0) {
                firepower.setVisibility(View.GONE);
            } else {
                firepower.setVisibility(View.VISIBLE);
                if (base > 0) {
                    firepower.setText("+" + base);
                } else {
                    firepower.setText(String.valueOf(base));
                }
            }
            float improve = SlotItemUtility.getShellingImprovementFirepower(mstSlotitem, level);
            BigDecimal value = new BigDecimal(improve);
            BigDecimal roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                firepowerImprove.setVisibility(View.GONE);
            } else {
                firepowerImprove.setVisibility(View.VISIBLE);
                firepowerImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                firepowerLayout.setVisibility(View.GONE);
            } else {
                firepowerLayout.setVisibility(View.VISIBLE);
            }

            base = mstSlotitem.getRaig();
            if (base == 0) {
                torpedo.setVisibility(View.GONE);
            } else {
                torpedo.setVisibility(View.VISIBLE);
                if (base > 0) {
                    torpedo.setText("+" + base);
                } else {
                    torpedo.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getTorpedoSalvoImprovementPower(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                torpedoImprove.setVisibility(View.GONE);
            } else {
                torpedoImprove.setVisibility(View.VISIBLE);
                torpedoImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                torpedoLayout.setVisibility(View.GONE);
            } else {
                torpedoLayout.setVisibility(View.VISIBLE);
            }

            base = mstSlotitem.getBaku();
            if (base == 0) {
                divebomb.setVisibility(View.GONE);
            } else {
                divebomb.setVisibility(View.VISIBLE);
                if (base > 0) {
                    divebomb.setText("+" + base);
                } else {
                    divebomb.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getImprovementDivebomb(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                divebombImprove.setVisibility(View.GONE);
            } else {
                divebombImprove.setVisibility(View.VISIBLE);
                divebombImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                divebombLayout.setVisibility(View.GONE);
            } else {
                divebombLayout.setVisibility(View.VISIBLE);
            }

            base = mstSlotitem.getTyku();
            if (base == 0) {
                aa.setVisibility(View.GONE);
            } else {
                aa.setVisibility(View.VISIBLE);
                if (base > 0) {
                    aa.setText("+" + base);
                } else {
                    aa.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getImprovementAA(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                aaImprove.setVisibility(View.GONE);
            } else {
                aaImprove.setVisibility(View.VISIBLE);
                aaImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                aaLayout.setVisibility(View.GONE);
            } else {
                aaLayout.setVisibility(View.VISIBLE);
            }

            base = mstSlotitem.getTais();
            if (base == 0) {
                asw.setVisibility(View.GONE);
            } else {
                asw.setVisibility(View.VISIBLE);
                if (base > 0) {
                    asw.setText("+" + base);
                } else {
                    asw.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getImprovementASW(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                aswImprove.setVisibility(View.GONE);
            } else {
                aswImprove.setVisibility(View.VISIBLE);
                aswImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                aswLayout.setVisibility(View.GONE);
            } else {
                aswLayout.setVisibility(View.VISIBLE);
            }

            base = mstSlotitem.getSaku();
            if (base == 0) {
                los.setVisibility(View.GONE);
            } else {
                los.setVisibility(View.VISIBLE);
                if (base > 0) {
                    los.setText("+" + base);
                } else {
                    los.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getImprovementLOS(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                losImprove.setVisibility(View.GONE);
            } else {
                losImprove.setVisibility(View.VISIBLE);
                losImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                losLayout.setVisibility(View.GONE);
            } else {
                losLayout.setVisibility(View.VISIBLE);
            }

            base = mstSlotitem.getHoum();
            if (base == 0) {
                accuracy.setVisibility(View.GONE);
            } else {
                accuracy.setVisibility(View.VISIBLE);
                if (base > 0) {
                    accuracy.setText("+" + base);
                } else {
                    accuracy.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getImprovementAccuracy(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                accuracyImprove.setVisibility(View.GONE);
            } else {
                accuracyImprove.setVisibility(View.VISIBLE);
                accuracyImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                accuracyLayout.setVisibility(View.GONE);
            } else {
                accuracyLayout.setVisibility(View.VISIBLE);
            }

            base = mstSlotitem.getHouk();
            if (base == 0) {
                evation.setVisibility(View.GONE);
            } else {
                evation.setVisibility(View.VISIBLE);
                if (base > 0) {
                    evation.setText("+" + base);
                } else {
                    evation.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getImprovementEvation(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                evationImprove.setVisibility(View.GONE);
            } else {
                evationImprove.setVisibility(View.VISIBLE);
                evationImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                evationLayout.setVisibility(View.GONE);
            } else {
                evationLayout.setVisibility(View.VISIBLE);
            }

            rangeLayout.setVisibility(View.VISIBLE);
            switch (mstSlotitem.getLeng()) {
                case 0:
                    rangeLayout.setVisibility(View.GONE);
                    break;
                case 1:
                    range.setText("短");
                    break;
                case 2:
                    range.setText("中");
                    break;
                case 3:
                    range.setText("長");
                    break;
                case 4:
                    range.setText("超長");
                    break;
            }
            rangeImprove.setVisibility(View.GONE);

            base = mstSlotitem.getSouk();
            if (base == 0) {
                armor.setVisibility(View.GONE);
            } else {
                armor.setVisibility(View.VISIBLE);
                if (base > 0) {
                    armor.setText("+" + base);
                } else {
                    armor.setText(String.valueOf(base));
                }
            }
            improve = SlotItemUtility.getImprovementArmor(mstSlotitem, level);
            value = new BigDecimal(improve);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (improve == 0) {
                armorImprove.setVisibility(View.GONE);
            } else {
                armorImprove.setVisibility(View.VISIBLE);
                armorImprove.setText("+" + roundDown);
            }
            if (base == 0 && improve == 0) {
                armorLayout.setVisibility(View.GONE);
            } else {
                armorLayout.setVisibility(View.VISIBLE);
            }

            float adjusted = SlotItemUtility.getAdjustedAA(mstSlotitem);
            if (adjusted == 0) {
                adjustedAA.setVisibility(View.GONE);
            } else {
                adjustedAA.setVisibility(View.VISIBLE);
                value = new BigDecimal(adjusted);
                roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
                roundDown = roundDown.stripTrailingZeros();
                adjustedAA.setText("+" + roundDown.toPlainString());
            }
            float adjustedImprove = SlotItemUtility.getImprovementAdjustedAA(mstSlotitem, mySlotItem.getLevel());
            value = new BigDecimal(adjustedImprove);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (adjustedImprove == 0) {
                adjustedAAImprove.setVisibility(View.GONE);
            } else {
                adjustedAAImprove.setVisibility(View.VISIBLE);
                adjustedAAImprove.setText("+" + roundDown);
            }
            if (adjusted == 0 && adjustedImprove == 0) {
                adjustedAALayout.setVisibility(View.GONE);
            } else {
                adjustedAALayout.setVisibility(View.VISIBLE);
            }

            float adjustedFleet = SlotItemUtility.getAdjustedFleetAA(mstSlotitem);
            if (adjustedFleet == 0) {
                adjustedFleetAA.setVisibility(View.GONE);
            } else {
                adjustedFleetAA.setVisibility(View.VISIBLE);
                value = new BigDecimal(adjustedFleet);
                roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
                roundDown = roundDown.stripTrailingZeros();
                adjustedFleetAA.setText("+" + roundDown.toPlainString());
            }
            float adjustedFleetImprove = SlotItemUtility.getImprovementAdjustedFleetAA(mstSlotitem, mySlotItem.getLevel());
            value = new BigDecimal(adjustedFleetImprove);
            roundDown = value.setScale(2, BigDecimal.ROUND_DOWN);
            roundDown = roundDown.stripTrailingZeros();
            if (adjustedFleetImprove == 0) {
                adjustedFleetAAImprove.setVisibility(View.GONE);
            } else {
                adjustedFleetAAImprove.setVisibility(View.VISIBLE);
                adjustedFleetAAImprove.setText("+" + roundDown);
            }
            if (adjustedFleet == 0 && adjustedFleetImprove == 0) {
                adjustedFleetAALayout.setVisibility(View.GONE);
            } else {
                adjustedFleetAALayout.setVisibility(View.VISIBLE);
            }

            shipsLayout.removeAllViews();
            if (!Objects.equals(listItem.equipShipSet, null)) {

                shipsLayout.setVisibility(View.VISIBLE);

                for (Multiset.Entry<Integer> entry : listItem.equipShipSet.entrySet()) {
                    MyShip myShip = MyShipManager.INSTANCE.getMyShip(entry.getElement());
                    LinearLayout equippedShipLayout = (LinearLayout) View.inflate(shipsLayout.getContext(), R.layout.view_equipped_ship, null);

                    TextView name = ButterKnife.findById(equippedShipLayout, R.id.name);
                    name.setText(myShip.getName());

                    TextView lv = ButterKnife.findById(equippedShipLayout, R.id.lv);
                    lv.setText("Lv" + myShip.getLv());

                    TextView shipNum = ButterKnife.findById(equippedShipLayout, R.id.ship_num);
                    if (entry.getCount() > 1) {
                        shipNum.setText("×" + entry.getCount());
                    } else {
                        shipNum.setVisibility(View.GONE);
                    }

                    shipsLayout.addView(equippedShipLayout);
                }
            } else {
                shipsLayout.setVisibility(View.GONE);
            }
        }
    }

}