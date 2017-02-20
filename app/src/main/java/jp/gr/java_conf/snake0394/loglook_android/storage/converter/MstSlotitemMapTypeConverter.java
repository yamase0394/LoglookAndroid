package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstSlotitemMapTypeConverter extends TypeConverter<Map<Integer, MstSlotitem>, String> {
    @Override
    public Map<Integer, MstSlotitem> convertFromSupportedType(String s) {
        Map<Integer, MstSlotitem> map = new Gson().fromJson(s, new TypeToken<Map<Integer, MstSlotitem>>() {}.getType());
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    @Override
    public String convertToSupportedType(Map<Integer, MstSlotitem> map) {
        return new Gson().toJson(map);
    }
}
