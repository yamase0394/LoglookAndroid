package jp.gr.java_conf.snake0394.loglook_android.storage;

import java.util.List;
import java.util.Map;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefField;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.LabelListTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.LabelMapTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListRecyclerViewAdapter;

/**
 * {@link jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListFragment}
 */
@Pref(name = "pref_my_ship_list_fragment")
public class MyShipListFragmentPrefs {

    @PrefField(name = "labelFilter")
    public String labelFilter = "すべて";

    @PrefField(name = "shipTypeFilter")
    public String shipTypeFilter = "すべて";

    @PrefField(name = "sortType")
    public String sortType = "Lv";

    @PrefField(name = "order")
    public String order = "降順";

    @PrefField(name = "toLabelMap", converter = LabelMapTypeConverter.class)
    public Map<Integer, List<MyShipListRecyclerViewAdapter.Label>> toLabelMap;

    @PrefField(name = "labelList", converter = LabelListTypeConverter.class)
    public List<MyShipListRecyclerViewAdapter.Label> labelList;

    public MyShipListFragmentPrefs() {
        //Empty constructor is needed
    }
}