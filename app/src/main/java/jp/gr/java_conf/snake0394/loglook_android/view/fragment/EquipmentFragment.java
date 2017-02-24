package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.storage.EquipmentFragmentPrefs;
import jp.gr.java_conf.snake0394.loglook_android.storage.EquipmentFragmentPrefsSpotRepository;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3;

public class EquipmentFragment extends Fragment {

    private RecyclerView recyclerView;
    private EquipmentFragmentPrefs prefs;
    private List<MySlotItem> dataList;

    public EquipmentFragment() {
        // Required empty public constructor
    }

    public static EquipmentFragment newInstance() {
        EquipmentFragment fragment = new EquipmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.prefs = EquipmentFragmentPrefsSpotRepository.getEntity(getContext());
        this.dataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_equipment, container, false);
        if (recyclerView == null) {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        }

        Spinner spinner = (Spinner) rootView.findViewById(R.id.equipTypeFilterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner equipType = (Spinner) rootView.findViewById(R.id.equipTypeFilterSpinner);
                // 初回起動時の動作
                if (equipType.isFocusable() == false) {
                    equipType.setFocusable(true);
                    return;
                }
                EquipmentAdapter adapter = new EquipmentAdapter(prefs.sortType, prefs.order);
                recyclerView.swapAdapter(adapter, false);
                filterDataList();
                setDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("全装備");
        adapter.add("小口径主砲");
        adapter.add("中口径主砲");
        adapter.add("大口径主砲");
        adapter.add("副砲");
        adapter.add("魚雷");
        adapter.add("艦上戦闘機");
        adapter.add("艦上爆撃機");
        adapter.add("艦上攻撃機");
        adapter.add("艦上偵察機");
        adapter.add("水上機/飛行艇");
        adapter.add("電探");
        adapter.add("高角砲");
        adapter.add("機銃/高射装置");
        adapter.add("対潜");
        adapter.add("陸上機");
        adapter.add("その他");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setFocusable(false);
        spinner.setSelection(0);

        spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner type = (Spinner) parent;
                // 初回起動時の動作
                if (type.isFocusable() == false) {
                    type.setFocusable(true);
                    return;
                }
                prefs.sortType = (String) type.getSelectedItem();
                EquipmentAdapter recyclerAdapter = new EquipmentAdapter(prefs.sortType, prefs.order);
                recyclerView.swapAdapter(recyclerAdapter, false);
                setDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("名前");
        adapter.add("改修度");
        adapter.add("new");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setFocusable(false);
        spinner.setSelection(adapter.getPosition(prefs.sortType));

        Button orderButton = (Button) rootView.findViewById(R.id.button_order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button orderButton = (Button) v;
                String buttonText = String.valueOf(orderButton.getText());
                switch (buttonText) {
                    case "降順":
                        prefs.order = "昇順";
                        break;
                    case "昇順":
                        prefs.order = "降順";
                        break;
                }
                orderButton.setText(prefs.order);

                EquipmentAdapter recyclerAdapter = new EquipmentAdapter(prefs.sortType, prefs.order);
                recyclerView.swapAdapter(recyclerAdapter, false);
                setDataList();
            }
        });
        orderButton.setText(prefs.order);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (recyclerView.getAdapter() == null) {
            EquipmentAdapter adapter = new EquipmentAdapter(prefs.sortType, prefs.order);
            recyclerView.setAdapter(adapter);
            filterDataList();
            setDataList();
        }
    }

    private void filterDataList() {
        ArrayList<MySlotItem> newDataList = new ArrayList<>();
        Spinner equipType = (Spinner) getActivity().findViewById(R.id.equipTypeFilterSpinner);
        String showEquipType = (String) equipType.getSelectedItem();
        for (MySlotItem mySlotItem : MySlotItemManager.INSTANCE.getMySlotItems()) {
            if (mySlotItem == null) {
                continue;
            }
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

            EquipType3 type3 = EquipType3.toEquipType3(mstSlotitem.getType()
                                                                  .get(3));
            if (type3.toString()
                     .equals(showEquipType)) {
                newDataList.add(mySlotItem);
            } else {
                switch (showEquipType) {
                    case "全装備":
                        newDataList.add(mySlotItem);
                        break;
                    case "水上機/飛行艇":
                        switch (type3) {
                            case 水上機:
                            case 大型飛行艇:
                                newDataList.add(mySlotItem);
                        }
                        break;
                    case "機銃/高射装置":
                        switch (type3) {
                            case 対空機銃:
                            case 高射装置:
                                newDataList.add(mySlotItem);
                        }
                        break;
                    case "対潜":
                        switch (type3) {
                            case ソナー:
                            case 爆雷:
                            case 対潜哨戒機:
                            case オートジャイロ:
                                newDataList.add(mySlotItem);
                        }
                        break;
                    case "陸上機":
                        switch (type3) {
                            case 局地戦闘機:
                            case 陸上攻撃機:
                                newDataList.add(mySlotItem);
                        }
                        break;
                    case "その他":
                        switch (type3) {
                            case 特型内火艇:
                            case 補給物資:
                            case 戦闘糧食:
                            case 水上艦要員:
                            case 対地装備:
                            case 航空要員:
                            case 司令部施設:
                            case 照明弾:
                            case 艦艇修理施設:
                            case 簡易輸送部材:
                            case 探照灯:
                            case 追加装甲:
                            case 上陸用舟艇:
                            case 機関部強化:
                            case 応急修理要員:
                            case 対艦強化弾:
                            case 対空強化弾:
                            case 噴式戦闘爆撃機_噴式景雲改:
                            case 噴式戦闘爆撃機_橘花改:
                            case 輸送機材:
                            case 潜水艦装備:
                            case UNKNOWN:
                                newDataList.add(mySlotItem);
                        }
                        break;
                }
            }
        }

        dataList = newDataList;
    }

    private void setDataList() {
        ((EquipmentAdapter) recyclerView.getAdapter()).setItems(dataList);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EquipmentFragmentPrefsSpotRepository.putEntity(getContext(), this.prefs);
    }
}
