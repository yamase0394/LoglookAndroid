package jp.gr.java_conf.snake0394.loglook_android.storage;

import java.util.List;
import java.util.Map;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefString;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.LabelListTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.LabelMapTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListRecyclerViewAdapter;

/**
 * Created by snake0394 on 2017/02/10.
 */

@Pref(name = "pref_my_ship_list_fragment")
public class MyShipListFragmentPrefs {

    @PrefString(name = "labelFilter", defaultValue = "すべて")
    public String labelFilter;

    @PrefString(name = "shipTypeFilter", defaultValue = "すべて")
    public String shipTypeFilter;

    @PrefString(name = "sortType", defaultValue = "Lv")
    public String sortType;

    @PrefString(name = "order", defaultValue = "降順")
    public String order;

    @PrefString(name = "toLabelMap", converter = LabelMapTypeConverter.class)
    public Map<Integer, List<MyShipListRecyclerViewAdapter.Label>> toLabelMap;

    @PrefString(name = "labelList", converter = LabelListTypeConverter.class)
    public List<MyShipListRecyclerViewAdapter.Label> labelList;

    public MyShipListFragmentPrefs() {
        //Empty constructor is needed
    }
}