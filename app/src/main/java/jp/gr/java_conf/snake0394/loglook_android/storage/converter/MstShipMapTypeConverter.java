package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstShipMapTypeConverter extends TypeConverter<Map<Integer, MstShip>, String> {
    @Override
    public Map<Integer, MstShip> convertFromSupportedType(String s) {
        Map<Integer, MstShip> mstShipMap = new Gson().fromJson(s, new TypeToken<Map<Integer, MstShip>>() {}.getType());
        if (mstShipMap == null) {
            mstShipMap = new HashMap<>();
        }
        return mstShipMap;
    }

    @Override
    public String convertToSupportedType(Map<Integer, MstShip> mstShipMap) {
        return new Gson().toJson(mstShipMap);
    }
}
