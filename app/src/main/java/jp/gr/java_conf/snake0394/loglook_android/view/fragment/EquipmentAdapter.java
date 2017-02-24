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
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.R;
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
                if (id != -1 && MySlotItemManager.INSTANCE.contains(id)) {
                    mountedEquip.append(id, myShip.getId());
                }
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
        MySlotItem data = itemList.get(i).getMySlotItem();
        if (data != null) {
            sampleViewHolder.bind(data);
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<MySlotItem> newItemList) {

        //newItemListに含まれていない装備を削除
        for(int i = itemList.size() - 1; i >= 0; i--){
           MySlotItem  mySlotItem = itemList.get(i)
                                            .getMySlotItem();
            if (!newItemList.contains(mySlotItem)) {
                itemList.removeItemAt(i);
            }
        }

        for (MySlotItem mySlotItem : newItemList) {
            itemList.add(new ListItem(mySlotItem));
        }
    }

    private static class ListItem {
        private MySlotItem mySlotItem;

        public ListItem(MySlotItem mySlotItem) {
            this.mySlotItem = mySlotItem;
        }

        public int getId() {
            return this.mySlotItem.getId();
        }

        public MySlotItem getMySlotItem() {
            return mySlotItem;
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
            MySlotItem data1 = item1.getMySlotItem();
            MySlotItem data2 = item2.getMySlotItem();
            int result = 0;

            switch (this.sortType) {
                case "名前":
                    MstSlotitem mst1 = MstSlotitemManager.INSTANCE.getMstSlotitem(data1.getMstId());
                    MstSlotitem mst2 = MstSlotitemManager.INSTANCE.getMstSlotitem(data2.getMstId());
                    result = mst1.getName()
                                 .compareTo(mst2.getName());
                    break;
                case "改修度":
                    result = data1.getLevel() - data2.getLevel();
                    break;
                case "new":
                    result = data2.getId() - data1.getId();
                    break;
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
        public boolean areContentsTheSame(ListItem oldData, ListItem newData) {
            return oldData.getId() == newData.getId();
        }

        @Override
        public boolean areItemsTheSame(ListItem item1, ListItem item2) {
            return item1.getId() == item2.getId();
        }
    }

    public class EquipmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.equipment)
        TextView equipment;
        @BindView(R.id.equipIcon)
        ImageView equipIcon;
        @BindView(R.id.alv)
        TextView alv;
        @BindView(R.id.improvement)
        TextView improvement;
        @BindView(R.id.equipShipName)
        TextView equipShipName;
        @BindView(R.id.equipShipLv)
        TextView equipShipLv;

        public EquipmentViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }

        public void bind(@NonNull final MySlotItem mySlotItem) {
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

            equipment.setText(mstSlotitem.getName());

            equipIcon.setImageResource(EquipType3.toEquipType3(MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId())
                                                                                          .getType()
                                                                                          .get(3))
                                                 .getImageId());

            switch (mySlotItem.getAlv()) {
                case 0:
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
                improvement.setText("");
            } else {
                improvement.setText("★" + String.valueOf(mySlotItem.getLevel()));
            }

            int myShipId = mountedEquip.get(mySlotItem.getId(), -1);
            if (myShipId != -1) {
                MyShip myShip = MyShipManager.INSTANCE.getMyShip(myShipId);
                equipShipName.setText(myShip.getName());
                equipShipLv.setText("Lv" + myShip.getLv());
            } else {
                equipShipName.setText("");
                equipShipLv.setText("");
            }
        }
    }

}