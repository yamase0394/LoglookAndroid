package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;

public class DamagedShipFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppBarLayout sortAppBar;

    public DamagedShipFragment() {
        // Required empty public constructor
    }

    public static DamagedShipFragment newInstance() {
        DamagedShipFragment fragment = new DamagedShipFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_damaged_ship, container, false);
        if (recyclerView == null) {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
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
                editor.putString("damagedShipSortType", (String) type.getSelectedItem());
                editor.apply();
                Spinner order = (Spinner) rootView.findViewById(R.id.orderSpinner);
                DamagedShipAdapter adapter = new DamagedShipAdapter(getFragmentManager(), (String) type.getSelectedItem(), (String) order.getSelectedItem());
                recyclerView.swapAdapter(adapter, false);
                initDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("修復時間");
        adapter.add("損傷度");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setFocusable(false);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switch (sp.getString("damagedShipSortType", "修復時間")) {
            case "修復時間":
                spinner.setSelection(0);
                break;
            case "損傷度":
                spinner.setSelection(1);
                break;
        }

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
                editor.putString("damagedShipOrder", (String) order.getSelectedItem());
                editor.apply();
                Spinner type = (Spinner) rootView.findViewById(R.id.sortSpinner);
                DamagedShipAdapter adapter = new DamagedShipAdapter(getFragmentManager(), (String) type.getSelectedItem(), (String) order.getSelectedItem());
                recyclerView.swapAdapter(adapter, false);
                initDataList();
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
        switch (sp.getString("damagedShipOrder", "降順")) {
            case "昇順":
                spinner.setSelection(0);
                break;
            case "降順":
                spinner.setSelection(1);
                break;
        }

        sortAppBar = (AppBarLayout) rootView.findViewById(R.id.sortAppBar);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (recyclerView.getAdapter() == null) {
            final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            DamagedShipAdapter adapter = new DamagedShipAdapter(getFragmentManager(), sp.getString("damagedShipSortType", "修復時間"), sp.getString("damagedShipOrder", "降順"));
            recyclerView.setAdapter(adapter);
            initDataList();
        }
    }

    private void initDataList() {
        ((DamagedShipAdapter) recyclerView.getAdapter()).clearData();
        ArrayList<MyShip> initialDataList = new ArrayList<>();
        for (MyShip myShip : MyShipManager.INSTANCE.getMyShips()) {
            if (myShip == null) {
                continue;
            }
            if (myShip.getNdockTime() > 0) {
                initialDataList.add(myShip);
            }
        }
        ((DamagedShipAdapter) recyclerView.getAdapter()).addDataOf(initialDataList);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBar);
        appBarLayout.setExpanded(true, true);

        sortAppBar.setExpanded(true, true);
    }
}
