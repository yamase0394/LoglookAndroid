package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstUseitemMapTypeConverter extends TypeConverter<Map<Integer, MstUseitem>, String> {
    @Override
    public Map<Integer, MstUseitem> convertFromSupportedType(String s) {
        Map<Integer, MstUseitem> map = new Gson().fromJson(s, new TypeToken<Map<Integer, MstUseitem>>() {}.getType());
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    @Override
    public String convertToSupportedType(Map<Integer, MstUseitem> map) {
        return new Gson().toJson(map);
    }
}
