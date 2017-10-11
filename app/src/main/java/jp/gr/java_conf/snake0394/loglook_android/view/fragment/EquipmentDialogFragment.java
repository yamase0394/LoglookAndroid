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

import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmInt;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3;

/**
 * Created by snake0394 on 2016/12/07.
 */

public class EquipmentDialogFragment extends android.support.v4.app.DialogFragment {

    private Realm realm;

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
        realm = Realm.getDefaultInstance();
        final MyShip myShip = realm.where(MyShip.class).equalTo("id", getArguments().getInt("shipId")).findFirst();
        List<MySlotItem> slotItemList = new ArrayList<>();
        for (RealmInt slotItemId : myShip.getSlot()) {
            if (slotItemId.getValue() > 0) {
                MySlotItem mySlotItem = realm.where(MySlotItem.class).equalTo("id", slotItemId.getValue()).findFirst();
                slotItemList.add(mySlotItem);
            }
        }

        TextView text;
        for (int i = 1; i <= 4; i++) {
            int slotItemId = myShip.getSlot().get(i - 1).getValue();

            if (slotItemId == -1) {
                String name = "space" + i;
                int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "equipIcon" + i;
                strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
                ImageView image = (ImageView) rootView.findViewById(strId);
                if (i <= myShip.getSlotnum()) {
                    image.setImageResource(EquipType3.EMPTY.getImageId());
                } else {
                    image.setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
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

            MySlotItem mySlotItem = realm.where(MySlotItem.class).equalTo("id", slotItemId).findFirst();

            String name = "space" + i;
            int strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            text.setVisibility(View.VISIBLE);
            if (myShip.getOnslot().get(i - 1).getValue() == 0) {
                text.setText("");
            } else {
                text.setText(String.valueOf(myShip.getOnslot().get(i - 1).getValue()));
            }

            name = "equipIcon" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            ImageView image = (ImageView) rootView.findViewById(strId);
            MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();
            image.setImageResource(EquipType3.toEquipType3(mstSlotitem.getType().get(3).getValue()).getImageId());

            name = "equipment" + i;
            strId = getResources().getIdentifier(name, "id", getContext().getPackageName());
            text = (TextView) rootView.findViewById(strId);
            text.setVisibility(View.VISIBLE);
            mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();
            text.setText(mstSlotitem.getName());

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

        if (myShip.getSlotEx() > 0) {
            MySlotItem mySlotItem = realm.where(MySlotItem.class).equalTo("id", myShip.getSlotEx()).findFirst();
            MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();
            image.setImageResource(EquipType3.toEquipType3(mstSlotitem.getType().get(3).getValue()).getImageId());
            text.setText(mstSlotitem.getName());
        } else {
            if (myShip.getSlotEx() == 0) {
                image.setImageResource(EquipType3.NOT_AVAILABLE.getImageId());
            } else if (myShip.getSlotEx() == -1) {
                image.setImageResource(EquipType3.EMPTY.getImageId());
            }

            text.setText("");
        }

        MstShip mstShip = realm.where(MstShip.class).equalTo("id", myShip.getShipId()).findFirst();
        builder.setView(rootView).setTitle(mstShip.getName() + "(Lv" + myShip.getLv() + ")").setNegativeButton("閉じる", null);
        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
