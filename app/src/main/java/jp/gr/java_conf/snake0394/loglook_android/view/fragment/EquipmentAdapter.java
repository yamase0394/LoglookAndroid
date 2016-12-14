package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private List<MySlotItem> mySlotItemList;
    private final FragmentManager fragmentManager;

    public EquipmentAdapter(FragmentManager fragmentManager) {
        mySlotItemList = new ArrayList<>();
        this.fragmentManager = fragmentManager;
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_equipment_cardview, viewGroup, false);
        return new EquipmentViewHolder(itemView, fragmentManager);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder sampleViewHolder, int i) {
        MySlotItem data = mySlotItemList.get(i);
        if (data != null) {
            sampleViewHolder.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        return mySlotItemList.size();
    }

    public void addDataOf(List<MySlotItem> dataList) {
        this.mySlotItemList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void removeDataOf(List<MySlotItem> dataList) {
        for (MySlotItem data : dataList) {
            mySlotItemList.remove(data);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        mySlotItemList.clear();
        notifyDataSetChanged();
    }

    public List<MySlotItem> getList() {
        return mySlotItemList;
    }

    public void sort(String sortType, String order) {
        switch (sortType) {
            case "名前":
                Collections.sort(mySlotItemList, new NameComparator(order));
                break;
            case "改修度":
                Collections.sort(mySlotItemList, new ImprovementComparator(order));
                break;
            case "入手":
                Collections.sort(mySlotItemList, new GetTimeComparator(order));
                break;
        }
        notifyDataSetChanged();
    }

    private class NameComparator implements Comparator<MySlotItem> {
        private String order;

        public NameComparator(String order) {
            super();
            this.order = order;
        }

        public int compare(MySlotItem a, MySlotItem b) {
            MstSlotitem mstSlotitemA = MstSlotitemManager.INSTANCE.getMstSlotitem(a.getMstId());
            MstSlotitem mstSlotitemB = MstSlotitemManager.INSTANCE.getMstSlotitem(b.getMstId());
            int result = mstSlotitemA.getName().compareTo(mstSlotitemB.getName());
            if (order.equals("降順")) {
                result *= -1;
            }
            return result;
        }
    }

    private class ImprovementComparator implements Comparator<MySlotItem> {
        private String order;

        public ImprovementComparator(String order) {
            super();
            this.order = order;
        }

        public int compare(MySlotItem a, MySlotItem b) {
            MstSlotitem mstSlotitemA = MstSlotitemManager.INSTANCE.getMstSlotitem(a.getMstId());
            MstSlotitem mstSlotitemB = MstSlotitemManager.INSTANCE.getMstSlotitem(b.getMstId());
            int result = mstSlotitemA.getName().compareTo(mstSlotitemB.getName());

            //同じ装備の場合は改修度でソート
            if (result == 0) {
                result = a.getLevel() - b.getLevel();
                if (order.equals("降順")) {
                    result *= -1;
                }
                //違う装備の場合
                //安定ソートにするため0を返す
            } else {
                result = 0;
            }

            return result;
        }
    }

    private class GetTimeComparator implements Comparator<MySlotItem> {
        private String order;

        public GetTimeComparator(String order) {
            super();
            this.order = order;
        }

        public int compare(MySlotItem a, MySlotItem b) {
            int result = a.getId() - b.getId();
            if (order.equals("降順")) {
                result *= -1;
            }
            return result;
        }
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

}