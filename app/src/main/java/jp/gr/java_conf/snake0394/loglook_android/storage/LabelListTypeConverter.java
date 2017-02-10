package jp.gr.java_conf.snake0394.loglook_android.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListRecyclerViewAdapter;

/**
 * Created by snake0394 on 2017/02/10.
 */
public class LabelListTypeConverter extends TypeConverter<List<MyShipListRecyclerViewAdapter.Label>, String> {
    @Override
    public List<MyShipListRecyclerViewAdapter.Label> convertFromSupportedType(String s) {
        List<MyShipListRecyclerViewAdapter.Label> list = new Gson().fromJson(s, new TypeToken<List<MyShipListRecyclerViewAdapter.Label>>() {}.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public String convertToSupportedType(List<MyShipListRecyclerViewAdapter.Label> labelList) {
        return new Gson().toJson(labelList);
    }
}