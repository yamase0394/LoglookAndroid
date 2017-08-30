package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.EquipType2;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.storage.EquipmentFragmentPrefs;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3;

public class EquipmentFragment extends Fragment implements EquipmentDrawerRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.layout_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.recyclerview_right_drawer)
    RecyclerView rightDrawerRecycler;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.equipTypeFilterSpinner)
    Spinner equipTypeFilterSpinner;
    @BindView(R.id.sortSpinner)
    Spinner sortTypeSpinner;
    @BindView(R.id.button_order)
    Button orderButton;

    private static final List<String> equipTypeFilterList = Arrays.asList("全装備", "小口径主砲", "中口径主砲", "大口径主砲", "副砲", "魚雷", "艦戦", "艦爆/艦攻", "艦偵", "水上機/飛行艇", "電探", "機銃/高射装置", "高角砲", "対潜", "陸上機", "その他");

    private EquipmentFragmentPrefs prefs;
    private List<MySlotItem> dataList;
    private Unbinder unbinder;
    private Realm realm;

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

        this.realm = Realm.getDefaultInstance();
        this.prefs = new EquipmentFragmentPrefs(getContext());
        this.dataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_equipment, container, false);

        this.unbinder = ButterKnife.bind(this, rootView);

        this.rightDrawerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        EquipmentDrawerRecyclerAdapter drawerRecyclerAdapter = new EquipmentDrawerRecyclerAdapter(this);
        drawerRecyclerAdapter.setItems(equipTypeFilterList);
        this.rightDrawerRecycler.setAdapter(drawerRecyclerAdapter);
        this.rightDrawerRecycler.addItemDecoration(new EquipmentDrawerRecyclerAdapter.MyItemDecoration(getContext()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        if (recyclerView.getAdapter() == null) {
            EquipmentAdapter adapter = new EquipmentAdapter(prefs.getSortType(), prefs.getOrder());
            recyclerView.setAdapter(adapter);
        }

        equipTypeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 初回起動時の動作
                if (!equipTypeFilterSpinner.isFocusable()) {
                    equipTypeFilterSpinner.setFocusable(true);
                    return;
                }
                EquipmentAdapter adapter = new EquipmentAdapter(prefs.getSortType(), prefs.getOrder());
                recyclerView.swapAdapter(adapter, false);
                filterDataList();
                setDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, equipTypeFilterList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipTypeFilterSpinner.setAdapter(adapter);
        equipTypeFilterSpinner.setFocusable(false);
        equipTypeFilterSpinner.setSelection(0);

        sortTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 初回起動時の動作
                if (!sortTypeSpinner.isFocusable()) {
                    sortTypeSpinner.setFocusable(true);
                    return;
                }
                prefs.setSortType((String) sortTypeSpinner.getSelectedItem());
                /*
                EquipmentAdapter recyclerAdapter = new EquipmentAdapter(prefs.getSortType(), prefs.getOrder());
                recyclerView.swapAdapter(recyclerAdapter, false);
                setDataList();
                */
                ((EquipmentAdapter) recyclerView.getAdapter()).sortData(prefs.getSortType());
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.addAll("名前", "改修度", "ID", "火力", "雷装", "爆装", "対空", "対潜", "索敵", "命中", "回避", "装甲", "加重対空", "艦隊対空");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortTypeSpinner.setAdapter(adapter);
        sortTypeSpinner.setFocusable(false);
        sortTypeSpinner.setSelection(adapter.getPosition(prefs.getSortType()));

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = String.valueOf(orderButton.getText());
                switch (buttonText) {
                    case "降順":
                        prefs.setOrder("昇順");
                        break;
                    case "昇順":
                        prefs.setOrder("降順");
                        break;
                }
                orderButton.setText(prefs.getOrder());

                /*
                EquipmentAdapter recyclerAdapter = new EquipmentAdapter(prefs.getSortType(), prefs.getOrder());
                recyclerView.swapAdapter(recyclerAdapter, false);
                setDataList();
                */
                ((EquipmentAdapter) recyclerView.getAdapter()).sortData(prefs.getSortType());
                recyclerView.scrollToPosition(0);
            }
        });
        orderButton.setText(prefs.getOrder());

        filterDataList();
        setDataList();

        return rootView;
    }

    /**
     * {@link #dataList}を{@link #equipTypeFilterSpinner}の選択項目でフィルターします
     */
    private void filterDataList() {
        ArrayList<MySlotItem> newDataList = new ArrayList<>();
        String showEquipType = String.valueOf(equipTypeFilterSpinner.getSelectedItem());

        for (MySlotItem mySlotItem : realm.where(MySlotItem.class).findAll()) {
            try {
                MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();

                EquipType3 type3 = EquipType3.toEquipType3(mstSlotitem.getType()
                                                                      .get(3).getValue());
                if (type3.toString()
                         .equals(showEquipType)) {
                    newDataList.add(mySlotItem);
                } else {
                    switch (showEquipType) {
                        case "小口径主砲":
                            if (type3 == EquipType3.高角砲 && EquipType2.toEquipType2(mstSlotitem.getType()
                                                                                              .get(2).getValue()) == EquipType2.小口径主砲) {
                                newDataList.add(mySlotItem);
                            }
                            break;
                        case "副砲":
                            if (type3 == EquipType3.高角砲 && EquipType2.toEquipType2(mstSlotitem.getType()
                                                                                              .get(2).getValue()) == EquipType2.副砲) {
                                newDataList.add(mySlotItem);
                            }
                            break;
                        case "全装備":
                            newDataList.add(mySlotItem);
                            break;
                        case "艦戦":
                            switch (type3) {
                                case 艦上戦闘機:
                                    newDataList.add(mySlotItem);
                            }
                            break;
                        case "艦爆/艦攻":
                            switch (type3) {
                                case 艦上爆撃機:
                                case 艦上攻撃機:
                                    newDataList.add(mySlotItem);
                            }
                            break;
                        case "艦偵":
                            switch (type3) {
                                case 艦上偵察機:
                                    newDataList.add(mySlotItem);
                            }
                            break;
                        case "水上機/飛行艇":
                            switch (type3) {
                                case 水上機:
                                case 大型飛行艇:
                                case 水上戦闘機:
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
                                case 陸軍戦闘機:
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
            } catch (Exception e) {
                //mySlotItem.getIdとmySlotItem.getMstIdが0を返す
                e.printStackTrace();
            }
        }

        ((EquipmentDrawerRecyclerAdapter) rightDrawerRecycler.getAdapter()).setHighlight(equipTypeFilterList.indexOf(showEquipType));

        dataList = newDataList;
    }

    /**
     * {@link #recyclerView}のアダプタに{@link #dataList}を渡します
     */
    private void setDataList() {
        ((EquipmentAdapter) recyclerView.getAdapter()).setItems(dataList);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.realm.close();
        this.unbinder.unbind();
    }

    @Override
    public void onItemClick(String itemName) {
        equipTypeFilterSpinner.setSelection(equipTypeFilterList.indexOf(itemName));
        drawerLayout.closeDrawers();
    }
}
