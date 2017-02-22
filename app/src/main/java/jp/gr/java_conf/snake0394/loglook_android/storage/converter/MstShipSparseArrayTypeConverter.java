package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstShipSparseArrayTypeConverter extends TypeConverter<SparseArray<MstShip>, String> {
    @Override
    public SparseArray<MstShip> convertFromSupportedType(String s) {
        Type sparseArrayType = new TypeToken<SparseArray<MstShip>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstShip.class))
            .create();

        SparseArray<MstShip> map = gson.fromJson(s, sparseArrayType);

        if (map == null) {
            map = new SparseArray<>();
        }

        return map;
    }

    @Override
    public String convertToSupportedType(SparseArray<MstShip> map) {
        Type sparseArrayType = new TypeToken<SparseArray<MstShip>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstShip.class))
            .create();

        return gson.toJson(map);
    }
}
