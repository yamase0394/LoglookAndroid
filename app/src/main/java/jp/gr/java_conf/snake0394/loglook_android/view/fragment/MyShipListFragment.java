package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.ShipType;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;

import static jp.gr.java_conf.snake0394.loglook_android.R.id.shipTypeFilterSpinner;

/**
 * 艦娘一覧画面
 */
public class MyShipListFragment extends Fragment implements MyShipListSortDialogFragment.OnItemClickListener,
                                                            MyShipListRecyclerViewAdapter.OnRecyclerViewItemClickListener,
                                                            MyShipListAddLabelDialog.OnFinishedListener {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private Spinner labelFilterSpinner;
    private List<MyShip> dataList;

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        EditText searchPlate = (EditText) searchView.findViewById(R.id.search_src_text);
        searchPlate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //((InputMethodManager) (getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

        rootView.findViewById(R.id.layout_base).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
            }
        });
        rootView.findViewById(R.id.sortAppBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
            }
        });

        if (this.recyclerView == null) {
            this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //((InputMethodManager) (getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        searchView.clearFocus();
                    }
                    return false;
                }
            });
        }
        if (recyclerView.getAdapter() == null) {
            final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            MyShipListRecyclerViewAdapter adapter = new MyShipListRecyclerViewAdapter(getFragmentManager(), sp.getString("myShipListSortType", "Lv"), sp.getString("myShipListOrder", "降順"), this);
            recyclerView.setAdapter(adapter);
        }

        Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
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
                editor.putString("myShipListSortType", (String) type.getSelectedItem());
                editor.apply();
                MyShipListRecyclerViewAdapter adapter = new MyShipListRecyclerViewAdapter(getFragmentManager(),  sp.getString("myShipListSortType", "Lv"), sp.getString("myShipListOrder", "降順"), MyShipListFragment.this);
                recyclerView.swapAdapter(adapter, false);
                initDataList();
                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.addAll("ID", "Lv", "cond", "砲撃戦火力", "雷撃戦火力", "夜戦火力", "対潜");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setFocusable(false);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switch (sp.getString("myShipListSortType", "Lv")) {
            case "ID":
                spinner.setSelection(0);
                break;
            case "Lv":
                spinner.setSelection(1);
                break;
            case "cond":
                spinner.setSelection(2);
                break;
            case "砲撃戦火力":
                spinner.setSelection(3);
                break;
            case "雷撃戦火力":
                spinner.setSelection(4);
                break;
            case "夜戦火力":
                spinner.setSelection(5);
                break;
            case "対潜":
                spinner.setSelection(6);
                break;
        }
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.clearFocus();
                return false;
            }
        });

        Button orderButton = (Button) rootView.findViewById(R.id.button_order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final SharedPreferences.Editor editor = sp.edit();
                Button orderButton = (Button) v;
                if (String.valueOf(orderButton.getText())
                          .equals("降順")) {
                    orderButton.setText("昇順");
                } else {
                    orderButton.setText("降順");
                }
                editor.putString("myShipListOrder", String.valueOf(orderButton.getText()));
                editor.apply();
                MyShipListRecyclerViewAdapter adapter = new MyShipListRecyclerViewAdapter(getFragmentManager(), sp.getString("myShipListSortType", "Lv"), sp.getString("myShipListOrder", "降順"), MyShipListFragment.this);
                recyclerView.swapAdapter(adapter, false);
                initDataList();
                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }
        });
        orderButton.setText(sp.getString("myShipListOrder", "降順"));

        spinner = (Spinner) rootView.findViewById(shipTypeFilterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner shipTypeFilterSpinner = (Spinner) parent;

                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final SharedPreferences.Editor editor = sp.edit();
                editor.putString("myShipListShipTypeFilter", (String) shipTypeFilterSpinner.getSelectedItem());
                editor.apply();

                filterDataList();

                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("すべて");
        for (ShipType shipType : ShipType.values()) {
            adapter.add(shipType.toString());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (dataList == null) {
            try {
                ShipType displayShipType = ShipType.valueOf(sp.getString("myShipListShipTypeFilter", "すべて"));
                //"すべて"の分ずらす
                spinner.setSelection(displayShipType.ordinal() + 1);
            } catch (IllegalArgumentException e) {
                spinner.setSelection(0);
            }
        }
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.clearFocus();
                return false;
            }
        });

        labelFilterSpinner = (Spinner) rootView.findViewById(R.id.spinner_filter_label);
        labelFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!labelFilterSpinner.isFocusable()){
                    labelFilterSpinner.setFocusable(true);
                    return;
                }

                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final SharedPreferences.Editor editor = sp.edit();
                editor.putString("myShipListLabelFilter", (String) labelFilterSpinner.getSelectedItem());
                editor.apply();

                filterDataList();

                recyclerView.scrollToPosition(0);

                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("すべて");
        for (MyShipListRecyclerViewAdapter.Label label : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()) {
            adapter.add(label.getName());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        labelFilterSpinner.setAdapter(adapter);
        if (((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()
                                                                       .contains(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0))) {
            labelFilterSpinner.setSelection(((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()
                                                                                                       .indexOf(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0)) + 1);
        } else {
            switch (sp.getString("myShipListLabelFilter", "すべて")) {
                case "すべて":
                    labelFilterSpinner.setSelection(0);
                    break;
            }
        }
        labelFilterSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.clearFocus();
                return false;
            }
        });

        return rootView;
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

    private void filterDataList(){
        SharedPreferences sp = App.getInstance()
                                  .getSharedPreferences();

        try {
            dataList = new ArrayList<>();
            ShipType shipType = ShipType.valueOf(sp.getString("myShipListShipTypeFilter", "すべて"));
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
                                                                       .contains(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0))) {
            dataList = new ArrayList<>();
            for (MyShipListRecyclerViewAdapter.MyShipListItem item : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getItemList()) {
                if (item.getLabelList()
                        .contains(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0))) {
                    dataList.add(item.getMyShip());
                }
            }
        } else {
            switch (sp.getString("myShipListLabelFilter", "すべて")) {
                case "すべて":
                    return;
            }
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
    public void onItemClicked(String selectedStr) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString("myShipListSortType", selectedStr);
        editor.apply();
        MyShipListRecyclerViewAdapter adapter = new MyShipListRecyclerViewAdapter(getFragmentManager(), selectedStr, sp.getString("myShipListOrder", "降順"), this);
        recyclerView.swapAdapter(adapter, false);
        initDataList();
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
                                       for (MyShipListRecyclerViewAdapter.Label label : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()) {
                                           arrayAdapter.add(label.getName());
                                       }
                                       arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                       labelFilterSpinner.setAdapter(arrayAdapter);
                                       labelFilterSpinner.setFocusable(false);

                                       SharedPreferences sp = App.getInstance()
                                                                 .getSharedPreferences();
                                       if (((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()
                                                                                                      .contains(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0))) {
                                           labelFilterSpinner.setSelection(((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()
                                                                                                                                      .indexOf(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0)) + 1);
                                       } else {
                                           switch (sp.getString("myShipListLabelFilter", "すべて")) {
                                               case "すべて":
                                                   labelFilterSpinner.setSelection(0);
                                                   break;
                                               default:
                                                   labelFilterSpinner.setSelection(0);
                                           }
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
    public void onAddLabelFinished(boolean isCanceled, int position, @Nullable MyShipListRecyclerViewAdapter.Label adedLabel) {
        if (isCanceled) {
            return;
        }

        MyShipListRecyclerViewAdapter adapter = (MyShipListRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.addLabel(position, adedLabel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        arrayAdapter.add("すべて");
        for (MyShipListRecyclerViewAdapter.Label label : ((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()) {
            arrayAdapter.add(label.getName());
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        labelFilterSpinner.setAdapter(arrayAdapter);
        labelFilterSpinner.setFocusable(false);

        SharedPreferences sp = App.getInstance()
                                  .getSharedPreferences();
        if (((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()
                                                                       .contains(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0))) {
            labelFilterSpinner.setSelection(((MyShipListRecyclerViewAdapter) recyclerView.getAdapter()).getLabelList()
                                                                                                       .indexOf(new MyShipListRecyclerViewAdapter.Label(sp.getString("myShipListLabelFilter", "すべて"), 0)) + 1);
        } else {
            switch (sp.getString("myShipListLabelFilter", "すべて")) {
                case "すべて":
                    labelFilterSpinner.setSelection(0);
                    break;
                default:
                    labelFilterSpinner.setSelection(0);
            }
        }

        filterDataList();
    }
}