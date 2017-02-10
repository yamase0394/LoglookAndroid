package jp.gr.java_conf.snake0394.loglook_android.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListRecyclerViewAdapter;

/**
 * Created by snake0394 on 2017/02/10.
 */

public class LabelMapTypeConverter extends TypeConverter<Map<Integer, List<MyShipListRecyclerViewAdapter.Label>>, String> {
    @Override
    public Map<Integer, List<MyShipListRecyclerViewAdapter.Label>> convertFromSupportedType(String s) {
        Map<Integer, List<MyShipListRecyclerViewAdapter.Label>> map = new Gson().fromJson(s, new TypeToken<Map<Integer, List<MyShipListRecyclerViewAdapter.Label>>>() {}.getType());
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    @Override
    public String convertToSupportedType(Map<Integer, List<MyShipListRecyclerViewAdapter.Label>> labelMap) {
        return new Gson().toJson(labelMap);
    }
}