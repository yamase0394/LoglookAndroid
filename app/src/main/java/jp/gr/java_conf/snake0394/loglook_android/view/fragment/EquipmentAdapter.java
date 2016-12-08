package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;

/**
 * Created by snake0394 on 2016/12/08.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder> {

    private SortedList<MySlotItem> sortedList;
    private final FragmentManager fragmentManager;

    public EquipmentAdapter(FragmentManager fragmentManager, String sortType, String order) {
        sortedList = new SortedList<>(MySlotItem.class, new EquipmentCallback(this, sortType, order));
        this.fragmentManager = fragmentManager;
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_equipment_cardview, viewGroup, false);
        return new EquipmentViewHolder(itemView, fragmentManager);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder sampleViewHolder, int i) {
        MySlotItem data = sortedList.get(i);
        if (data != null) {
            sampleViewHolder.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void addDataOf(List<MySlotItem> dataList) {
        sortedList.addAll(dataList);
    }

    public void removeDataOf(List<MySlotItem> dataList) {
        sortedList.beginBatchedUpdates();
        for (MySlotItem data : dataList) {
            sortedList.remove(data);
        }
        sortedList.endBatchedUpdates();
    }

    public void clearData() {
        sortedList.clear();
    }

    public SortedList<MySlotItem> getList() {
        return sortedList;
    }

    public static class EquipmentViewHolder extends RecyclerView.ViewHolder {

        private FragmentManager fragmentManager;
        private TextView equipment;
        private ImageView equipIcon;
        private TextView alv;
        private TextView improvement;
        private TextView equipShipName;
        private TextView equipShipLv;

        public EquipmentViewHolder(View rootView, FragmentManager fragmentManager) {
            super(rootView);
            this.fragmentManager = fragmentManager;
            equipment = (TextView) rootView.findViewById(R.id.equipment);
            equipIcon = (ImageView) rootView.findViewById(R.id.equipIcon);
            alv = (TextView) rootView.findViewById(R.id.alv);
            improvement = (TextView) rootView.findViewById(R.id.improvement);
            equipShipName = (TextView) rootView.findViewById(R.id.equipShipName);
            equipShipLv = (TextView) rootView.findViewById(R.id.equipShipLv);
        }

        public void bind(@NonNull final MySlotItem mySlotItem) {
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

            equipment.setText(mstSlotitem.getName());

            equipIcon.setImageResource(EquipIconId.toEquipIconId(MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId()).getType().get(3)).getImageId());

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

            if (mySlotItem.getShipId() != -1) {
                MyShip myShip = MyShipManager.INSTANCE.getMyShip(mySlotItem.getShipId());
                equipShipName.setText(myShip.getName());
                equipShipLv.setText("Lv" + myShip.getLv());
            } else {
                equipShipName.setText("");
                equipShipLv.setText("");
            }
        }
    }

    private static class EquipmentCallback extends SortedList.Callback<MySlotItem> {

        private RecyclerView.Adapter adapter;
        private String sortType;
        private String order;

        EquipmentCallback(@NonNull RecyclerView.Adapter adapter, String sortType, String order) {
            this.adapter = adapter;
            this.sortType = sortType;
            this.order = order;
        }

        @Override
        public int compare(MySlotItem data1, MySlotItem data2) {
            int result = 0;
            switch (sortType) {
                case "名前":
                    MstSlotitem mstSlotitem1 = MstSlotitemManager.INSTANCE.getMstSlotitem(data1.getMstId());
                    MstSlotitem mstSlotitem2 = MstSlotitemManager.INSTANCE.getMstSlotitem(data2.getMstId());
                    result = mstSlotitem1.getName().compareTo(mstSlotitem2.getName());
                    if (result == 0) {
                        result = data1.getShipId() - data2.getShipId();
                    }
                    break;
                case "改修度":
                    mstSlotitem1 = MstSlotitemManager.INSTANCE.getMstSlotitem(data1.getMstId());
                    mstSlotitem2 = MstSlotitemManager.INSTANCE.getMstSlotitem(data2.getMstId());
                    result = mstSlotitem1.getName().compareTo(mstSlotitem2.getName());
                    if (result == 0) {
                        result = data1.getLevel() - data2.getLevel();
                    }
                    break;
                case "追加時期":
                    result = (int) (data1.getMstId() - data2.getMstId());
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
        public boolean areContentsTheSame(MySlotItem oldData, MySlotItem newData) {
            return false;
        }

        @Override
        public boolean areItemsTheSame(MySlotItem data1, MySlotItem data2) {
            return false;
        }
    }
}