package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;

public class EquipmentFragment extends Fragment {

  private RecyclerView recyclerView;

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
        Spinner type = (Spinner) rootView.findViewById(R.id.sortSpinner);
        Spinner order = (Spinner) rootView.findViewById(R.id.orderSpinner);
        EquipmentAdapter adapter = new EquipmentAdapter(getFragmentManager());
        recyclerView.swapAdapter(adapter, false);
        initDataList();
        adapter.sort((String) type.getSelectedItem(), (String) order.getSelectedItem());
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
        Spinner type = (Spinner) rootView.findViewById(R.id.sortSpinner);
        // 初回起動時の動作
        if (type.isFocusable() == false) {
          type.setFocusable(true);
          return;
        }
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString("equipmentSortType", (String) type.getSelectedItem());
        editor.apply();
        Spinner order = (Spinner) rootView.findViewById(R.id.orderSpinner);
        ((EquipmentAdapter) recyclerView.getAdapter()).sort((String) type.getSelectedItem(), (String) order.getSelectedItem());
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
    adapter.add("名前");
    adapter.add("改修");
    adapter.add("追加時期");
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setFocusable(false);
    spinner.setSelection(0);

    spinner = (Spinner) rootView.findViewById(R.id.orderSpinner);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner order = (Spinner) rootView.findViewById(R.id.orderSpinner);
        // 初回起動時の動作
        if (order.isFocusable() == false) {
          order.setFocusable(true);
          return;
        }
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString("equipmentOrder", (String) order.getSelectedItem());
        editor.apply();
        Spinner type = (Spinner) rootView.findViewById(R.id.sortSpinner);
        ((EquipmentAdapter) recyclerView.getAdapter()).sort((String) type.getSelectedItem(), (String) order.getSelectedItem());
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
    adapter.add("昇順");
    adapter.add("降順");
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setFocusable(false);
    spinner.setSelection(0);

    return rootView;
  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    if (recyclerView.getAdapter() == null) {
      final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
      EquipmentAdapter adapter = new EquipmentAdapter(getFragmentManager());
      recyclerView.setAdapter(adapter);
      initDataList();
      adapter.sort(sp.getString("equipmentSortType", "全装備"), sp.getString("equipmentOrder", "昇順"));
    }
  }

  private void initDataList() {
    ((EquipmentAdapter) recyclerView.getAdapter()).clearData();
    ArrayList<MySlotItem> initialDataList = new ArrayList<>();
    Set<Integer> equipSet = new HashSet<>();
    for (MyShip myShip : MyShipManager.INSTANCE.getMyShips()) {
      if (myShip == null) {
        continue;
      }
      for (int id : myShip.getSlot()) {
        if (!MySlotItemManager.INSTANCE.contains(id) || id == -1) {
          break;
        }
        MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(id);
        mySlotItem.setShipId(myShip.getId());
        equipSet.add(id);
      }
    }

    Spinner equipType = (Spinner) getActivity().findViewById(R.id.equipTypeFilterSpinner);
    String showEquipType = (String) equipType.getSelectedItem();
    for (MySlotItem mySlotItem : MySlotItemManager.INSTANCE.getMySlotItems()) {
      MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
      switch (showEquipType) {
        case "全装備":
          initialDataList.add(mySlotItem);
          break;
        case "小口径主砲":
          if (mstSlotitem.getType().get(3) == EquipIconId.小口径主砲.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "中口径主砲":
          if (mstSlotitem.getType().get(3) == EquipIconId.中口径主砲.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "大口径主砲":
          if (mstSlotitem.getType().get(3) == EquipIconId.大口径主砲.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "副砲":
          if (mstSlotitem.getType().get(3) == EquipIconId.副砲.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "魚雷":
          if (mstSlotitem.getType().get(3) == EquipIconId.魚雷.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "艦上戦闘機":
          if (mstSlotitem.getType().get(3) == EquipIconId.艦上戦闘機.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "艦上爆撃機":
          if (mstSlotitem.getType().get(3) == EquipIconId.艦上爆撃機.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "艦上攻撃機":
          if (mstSlotitem.getType().get(3) == EquipIconId.艦上攻撃機.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "艦上偵察機":
          if (mstSlotitem.getType().get(3) == EquipIconId.艦上偵察機.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "水上機/飛行艇":
          if (mstSlotitem.getType().get(3) == EquipIconId.水上機.getId() || mstSlotitem.getType().get(3) == EquipIconId.大型飛行艇.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "電探":
          if (mstSlotitem.getType().get(3) == EquipIconId.電探.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "高角砲":
          if (mstSlotitem.getType().get(3) == EquipIconId.高角砲.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "機銃/高射装置":
          if (mstSlotitem.getType().get(3) == EquipIconId.対空機銃.getId() || mstSlotitem.getType().get(3) == EquipIconId.高射装置.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "対潜":
          switch (EquipIconId.toEquipIconId(mstSlotitem.getType().get(3))) {
            case ソナー:
            case 爆雷:
            case 対潜哨戒機:
            case オートジャイロ:
              initialDataList.add(mySlotItem);
          }
          break;
        case "陸上機":
          if (mstSlotitem.getType().get(3) == EquipIconId.局地戦闘機.getId() || mstSlotitem.getType().get(3) == EquipIconId.陸上攻撃機.getId()) {
            initialDataList.add(mySlotItem);
          }
          break;
        case "その他":
          switch (EquipIconId.toEquipIconId(mstSlotitem.getType().get(3))) {
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
              initialDataList.add(mySlotItem);
          }
          break;
      }
      if (!equipSet.contains(mySlotItem.getId())) {
        mySlotItem.setShipId(-1);
      }
    }
    ((EquipmentAdapter) recyclerView.getAdapter()).addDataOf(initialDataList);
  }

}
