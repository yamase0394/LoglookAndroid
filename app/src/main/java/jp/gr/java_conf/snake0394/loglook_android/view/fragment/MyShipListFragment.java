package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.ShipType;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.storage.MyShipListFragmentPrefs;
import jp.gr.java_conf.snake0394.loglook_android.storage.MyShipListFragmentPrefsSpotRepository;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListRecyclerViewAdapter.Label;

/**
 * 艦娘一覧画面
 */
public class MyShipListFragment extends Fragment implements MyShipListRecyclerViewAdapter.OnRecyclerViewItemClickListener,
                                                            MyShipListAddLabelDialog.OnFinishedListener,
                                                            View.OnTouchListener {

    private static final List<String> SORT_TYPE_LIST = Arrays.asList("ID", "Lv", "cond", "砲撃戦火力", "雷撃戦火力", "夜戦火力", "対潜");

    private RecyclerView recyclerView;
    private SearchView searchView;
    private Spinner labelFilterSpinner;
    private List<MyShip> dataList;
    private MyShipListFragmentPrefs prefs;

    public MyShipListFragment() {
        // Required empty public constructor
    }

    public static MyShipListFragment newInstance() {
        MyShipListFragment fragment = new MyShipListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.prefs = MyShipListFragmentPrefsSpotRepository.getEntity(App.getInstance());

        //保存方法変更のため暫く以前の保存場所から取り出せるようにする
        if (prefs.toLabelMap.size() == 0) {
            prefs.toLabelMap = new Gson().fromJson(App.getInstance().getSharedPreferences().getString("toLabelMap", null), new TypeToken<Map<Integer, List<Label>>>() {}.getType());
        }
        if (prefs.labelList.size() == 0) {
            prefs.labelList = new Gson().fromJson(App.getInstance().getSharedPreferences().getString("labelList", null), new TypeToken<List<Label>>() {}.getType());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_my_ship_list, container, false);

        //ツールバーに検索ボックスを追加する
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_my_ship_list_fragment);
        this.searchView = (SearchView) toolbar.getMenu()
                                              .findItem(R.id.menu_search)
                                              .getActionView();
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDataList(newText);
                recyclerView.scrollToPosition(0);
                return true;
            }
        });
        final EditText searchPlate = (EditText) this.searchView.findViewById(R.id.search_src_text);
        searchPlate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchView.clearFocus();
                return false;
            }
        });
        searchPlate.setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN | EditorInfo.IME_ACTION_NONE);

        /*
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_call) {
                    android.support.v4.app.DialogFragment dialogFragment = MyShipListSortDialogFragment.newInstance();
                    dialogFragment.setTargetFragment(MyShipListFragment.this, 0);
                    dialogFragment.show(getFragmentManager(), "doalog");
                    return true;
                }
                return false;
            }
        });
        */

        //IMEを閉じる
        rootView.findViewById(R.id.layout_base)
                .setOnTouchListener(this);
        rootView.findViewById(R.id.sortAppBar)
                .setOnTouchListener(this);

        if (this.recyclerView == null) {
            this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //IMEを閉じる
            this.recyclerView.setOnTouchListener(this);
        }
        if (recyclerView.getAdapter() == null) {
            MyShipListRecyclerViewAdapter adapter = new MyShipListRecyclerViewAdapter(getFragmentManager(), prefs.sortType, prefs.order, this,prefs.toLabelMap,prefs.labelList);
            recyclerView.setAdapter(adapter);
        }

        Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
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

                MyShipListRecyclerViewAdapter adapter = new MyShipListRecyclerViewAdapter(getFragmentManager(), prefs.sortType, prefs.order, MyShipListFragment.this,prefs.toLabelMap,prefs.labelList);
                recyclerView.swapAdapter(adapter, false);
                initDataList();
                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        arrayAdapter.addAll((String[]) SORT_TYPE_LIST.toArray());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setFocusable(false);
        spinner.setSelection(SORT_TYPE_LIST.indexOf(prefs.sortType));
        spinner.setOnTouchListener(this);

        Button orderButton = (Button) rootView.findViewById(R.id.button_order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button orderButton = (Button) v;
                switch (String.valueOf(orderButton.getText())) {
                    case "降順":
                        orderButton.setText("昇順");
                        break;
                    case "昇順":
                        orderButton.setText("降順");
                        break;
                }

                prefs.order = orderButton.getText()
                                         .toString();

                MyShipListRecyclerViewAdapter adapter = new MyShipListRecyclerViewAdapter(getFragmentManager(), prefs.sortType, prefs.order, MyShipListFragment.this,prefs.toLabelMap,prefs.labelList);
                recyclerView.swapAdapter(adapter, false);
                initDataList();
                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }
        });
        orderButton.setText(prefs.order);

        spinner = (Spinner) rootView.findViewById(R.id.shipTypeFilterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner shipTypeFilterSpinner = (Spinner) parent;

                prefs.shipTypeFilter = (String) shipTypeFilterSpinner.getSelectedItem();

                filterDataList();
                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        arrayAdapter.add("すべて");
        for (ShipType shipType : ShipType.values()) {
            arrayAdapter.add(shipType.toString());
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        if (dataList == null) {
            try {
                ShipType displayShipType = ShipType.valueOf(prefs.shipTypeFilter);
                //"すべて"の分ずらす
                spinner.setSelection(displayShipType.ordinal() + 1);
            } catch (IllegalArgumentException e) {
                spinner.setSelection(0);
            }
        }
        spinner.setOnTouchListener(this);

        labelFilterSpinner = (Spinner) rootView.findViewById(R.id.spinner_filter_label);
        labelFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!labelFilterSpinner.isFocusable()) {
                    labelFilterSpinner.setFocusable(true);
                    return;
                }

                prefs.labelFilter = (String) labelFilterSpinner.getSelectedItem();

                filterDataList();

                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        arrayAdapter.add("すべて");
        for (Label label : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()) {
            arrayAdapter.add(label.getName());
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        labelFilterSpinner.setAdapter(arrayAdapter);
        MyShipListRecyclerViewAdapter recyclerViewAdapter = (MyShipListRecyclerViewAdapter) recyclerView.getAdapter();
        List<Label> labelList = recyclerViewAdapter.getLabelList();
        Label comparisionLabel = new Label(prefs.labelFilter, 0);
        if (labelList.contains(comparisionLabel)) {
            labelFilterSpinner.setSelection(labelList.indexOf(comparisionLabel) + 1);
        } else {
            labelFilterSpinner.setSelection(0);
        }
        labelFilterSpinner.setOnTouchListener(this);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        MyShipListRecyclerViewAdapter recyclerViewAdapter = (MyShipListRecyclerViewAdapter) recyclerView.getAdapter();
        prefs.toLabelMap = recyclerViewAdapter.getToLabelMap();
        prefs.labelList = recyclerViewAdapter.getLabelList();
        MyShipListFragmentPrefsSpotRepository.putEntity(App.getInstance(), prefs);
    }

    private void initDataList() {
        if (searchView.getQuery()
                      .length() > 0) {
            searchDataList(searchView.getQuery()
                                     .toString());
            return;
        }

        ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).setItems(dataList);
    }

    private void searchDataList(String searchWord) {
        searchWord = searchWord.toLowerCase();

        List<MyShip> filteredList = new ArrayList<>();
        for (MyShip myShip : dataList) {
            if (myShip.getName()
                      .toLowerCase()
                      .contains(searchWord)) {
                filteredList.add(myShip);
                continue;
            }

            MstShip mstShip = MstShipManager.INSTANCE.getMstShip(myShip.getShipId());
            if (mstShip.getYomi()
                       .toLowerCase()
                       .contains(searchWord)) {
                filteredList.add(myShip);
            }
        }

        ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).setItems(filteredList);
    }

    private void filterDataList() {
        try {
            dataList = new ArrayList<>();
            ShipType shipType = ShipType.valueOf(prefs.shipTypeFilter);
            for (MyShip myShip : MyShipManager.INSTANCE.getMyShips()) {
                if (myShip == null) {
                    continue;
                }

                MstShip mstShip = MstShipManager.INSTANCE.getMstShip(myShip.getShipId());

                if (shipType == ShipType.toShipType(mstShip.getStype())) {
                    dataList.add(myShip);
                }
            }
        } catch (IllegalArgumentException e) {
            dataList = new ArrayList<>(MyShipManager.INSTANCE.getMyShips());
        }

        initDataList();

        if (((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()
                                                                       .contains(new Label(prefs.labelFilter, 0))) {
            dataList = new ArrayList<>();
            for (MyShipListRecyclerViewAdapter.MyShipListItem item : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getItemList()) {
                if (item.getLabelList()
                        .contains(new Label(prefs.labelFilter, 0))) {
                    dataList.add(item.getMyShip());
                }
            }
        } else {
            return;
        }

        initDataList();
    }

    @Override
    public void onDestroyView() {
        //検索ボックスを削除
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu()
               .clear();

        super.onDestroyView();
    }

    @Override
    public void onRecyclerViewItemClicked(final int position) {
        if (searchView.hasFocus()) {
            searchView.clearFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items;
        final MyShipListRecyclerViewAdapter.MyShipListItem myShipListItem = ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);
        if (myShipListItem.getLabelList()
                          .size() > 0) {
            items = new String[]{"ラベルを追加", "ラベルを削除"};
        } else {
            items = new String[]{"ラベルを追加"};
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        DialogFragment dialogFragment = MyShipListAddLabelDialog.newInstance(position);
                        dialogFragment.setTargetFragment(MyShipListFragment.this, 0);
                        dialogFragment.show(getFragmentManager(), "addLabelDialog");
                        break;
                    case 1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        String[] items = new String[myShipListItem.getLabelList()
                                                                  .size()];
                        for (int i = 0; i < myShipListItem.getLabelList()
                                                          .size(); i++) {
                            items[i] = myShipListItem.getLabelList()
                                                     .get(i)
                                                     .getName();
                        }
                        builder.setTitle("ラベルを削除")
                               .setItems(items, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       MyShipListRecyclerViewAdapter adapter = (MyShipListRecyclerViewAdapter) recyclerView.getAdapter();
                                       adapter.removeLabel(position, myShipListItem.getLabelList()
                                                                                   .get(which));
                                       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                                       arrayAdapter.add("すべて");
                                       for (Label label : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()) {
                                           arrayAdapter.add(label.getName());
                                       }
                                       arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                       labelFilterSpinner.setAdapter(arrayAdapter);
                                       labelFilterSpinner.setFocusable(false);

                                       MyShipListRecyclerViewAdapter recyclerViewAdapter = (MyShipListRecyclerViewAdapter) recyclerView.getAdapter();
                                       List<Label> labelList = recyclerViewAdapter.getLabelList();
                                       Label comparisionLabel = new Label(prefs.labelFilter, 0);
                                       if (labelList.contains(comparisionLabel)) {
                                           labelFilterSpinner.setSelection(labelList.indexOf(comparisionLabel) + 1);
                                       } else {
                                           labelFilterSpinner.setSelection(0);
                                       }

                                       filterDataList();
                                   }
                               })
                               .setNegativeButton("キャンセル", null);
                        builder.show();
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onAddLabelFinished(boolean isCanceled, int position, @Nullable Label addedLabel) {
        if (isCanceled) {
            return;
        }

        MyShipListRecyclerViewAdapter adapter = (MyShipListRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.addLabel(position, addedLabel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        arrayAdapter.add("すべて");
        for (Label label : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()) {
            arrayAdapter.add(label.getName());
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        labelFilterSpinner.setAdapter(arrayAdapter);
        labelFilterSpinner.setFocusable(false);

        MyShipListRecyclerViewAdapter recyclerViewAdapter = (MyShipListRecyclerViewAdapter) recyclerView.getAdapter();
        List<Label> labelList = recyclerViewAdapter.getLabelList();
        Label comparisionLabel = new Label(prefs.labelFilter, 0);
        if (labelList.contains(comparisionLabel)) {
            labelFilterSpinner.setSelection(labelList.indexOf(comparisionLabel) + 1);
        } else {
            labelFilterSpinner.setSelection(0);
        }

        filterDataList();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.searchView.clearFocus();
        return false;
    }
}
