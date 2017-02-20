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
public class MstMissionNameMapTypeConverter extends TypeConverter<Map<String , MstMission>, String> {
    @Override
    public Map<String, MstMission> convertFromSupportedType(String s) {
        Map<String, MstMission> map = new Gson().fromJson(s, new TypeToken<Map<String, MstMission>>() {
        }.getType());
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    @Override
    public String convertToSupportedType(Map<String, MstMission> map) {
        return new Gson().toJson(map);
    }
}

