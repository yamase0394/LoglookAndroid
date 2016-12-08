package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;

/**
 * Created by snake0394 on 2016/12/07.
 */

public class EquipmentDialogFragment extends android.support.v4.app.DialogFragment {

    public static EquipmentDialogFragment newInstance(int shipId) {
        EquipmentDialogFragment fragment = new EquipmentDialogFragment();

        Bundle args = new Bundle();
        args.putInt("shipId", shipId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_fragment_equipment, null);
        Log.d("shipId", String.valueOf(getArguments().getInt("shipId")));
        final MyShip myShip = MyShipManager.INSTANCE.getMyShip(getArguments().getInt("shipId"));
        List<MySlotItem> slotItemList = new ArrayList<>();
        for (int slotItemId : myShip.getSlot()) {
            if (slotItemId != -1) {
                slotItemList.add(MySlotItemManager.INSTANCE.getMySlotItem(slotItemId));
            }
        }

        TextView text;
        for (int i = 1; i <= 4; i++) {
            int slotItemId = myShip.getSlot().get(i - 1);

            if (slotItemId == -1) {
                String name = "space" + i;
                int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "equipIcon" + i;
                strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                ImageView image = (ImageView) rootView.findViewById(strId);
                if (i <= myShip.getSlotnum()) {
                    image.setImageResource(EquipIconId.EMPTY.getImageId());
                } else {
                    image.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
                }

                name = "equipment" + i;
                strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "alv" + i;
                strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "improvement" + i;
                strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                continue;
            }

            MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(slotItemId);

            String name = "space" + i;
            int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            text.setVisibility(View.VISIBLE);
            if (myShip.getOnslot().get(i - 1) == 0) {
                text.setText("");
            } else {
                text.setText(String.valueOf(myShip.getOnslot().get(i - 1)));
            }

            name = "equipIcon" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            ImageView image = (ImageView) rootView.findViewById(strId);
            image.setImageResource(EquipIconId.toEquipIconId(MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId()).getType().get(3)).getImageId());

            name = "equipment" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            text.setVisibility(View.VISIBLE);
            text.setText(MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId()).getName());

            name = "alv" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            text.setVisibility(View.VISIBLE);
            switch (mySlotItem.getAlv()) {
                case 0:
                    text.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    text.setText("|");
                    text.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 2:
                    text.setText("||");
                    text.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 3:
                    text.setText("|||");
                    text.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 4:
                    text.setText("\\");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 5:
                    text.setText("\\\\");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 6:
                    text.setText("\\\\\\");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 7:
                    text.setText(">>");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
            }

            name = "improvement" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            text.setVisibility(View.VISIBLE);
            if (mySlotItem.getLevel() == 0) {
                text.setText("");
            } else {
                text.setText("★" + String.valueOf(mySlotItem.getLevel()));
            }
        }

        ImageView image = (ImageView) rootView.findViewById(R.id.extraSlotIcon);
        text = (TextView) rootView.findViewById(R.id.extraSlot);

        if (MySlotItemManager.INSTANCE.contains(myShip.getSlotEx())) {
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlotEx()).getMstId());
            image.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType().get(3)).getImageId());
            text.setText(mstSlotitem.getName());
        } else {
            if (myShip.getSlotEx() == 0) {
                image.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
            } else if (myShip.getSlotEx() == -1) {
                image.setImageResource(EquipIconId.EMPTY.getImageId());
            }

            text.setText("");
        }

        builder.setView(rootView).setTitle(myShip.getName() + "(Lv" + myShip.getLv() + ")");
        return builder.create();
    }
}
