package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstMissionIdMapTypeConverter extends TypeConverter<Map<Integer, MstMission>, String> {
    @Override
    public Map<Integer, MstMission> convertFromSupportedType(String s) {
        Map<Integer, MstMission> map = new Gson().fromJson(s, new TypeToken<Map<Integer, MstMission>>() {
        }.getType());
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    @Override
    public String convertToSupportedType(Map<Integer, MstMission> map) {
        return new Gson().toJson(map);
    }
}

