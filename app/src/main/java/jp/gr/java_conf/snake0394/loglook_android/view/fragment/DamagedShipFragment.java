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

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.storage.DamagedShipFragmentPrefs;
import jp.gr.java_conf.snake0394.loglook_android.storage.DamagedShipFragmentPrefsSpotRepository;

public class DamagedShipFragment extends Fragment {

    private RecyclerView recyclerView;
    private DamagedShipFragmentPrefs prefs;

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

        this.prefs = DamagedShipFragmentPrefsSpotRepository.getEntity(getContext());
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
                Spinner type = (Spinner) parent;
                // 初回起動時の動作
                if (type.isFocusable() == false) {
                    type.setFocusable(true);
                    return;
                }
                prefs.sortType = (String) type.getSelectedItem();
                DamagedShipAdapter adapter = new DamagedShipAdapter(getFragmentManager(), prefs.sortType, prefs.order);
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
                DamagedShipAdapter adapter = new DamagedShipAdapter(getFragmentManager(), prefs.sortType, prefs.order);
                recyclerView.swapAdapter(adapter, false);
                initDataList();
            }
        });
        orderButton.setText(this.prefs.order);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (recyclerView.getAdapter() == null) {
            DamagedShipAdapter adapter = new DamagedShipAdapter(getFragmentManager(), prefs.sortType, prefs.order);
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
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        DamagedShipFragmentPrefsSpotRepository.putEntity(getContext(), this.prefs);
    }
}
