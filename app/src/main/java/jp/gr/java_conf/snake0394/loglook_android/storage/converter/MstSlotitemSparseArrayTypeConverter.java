package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstSlotitemSparseArrayTypeConverter extends TypeConverter<SparseArray<MstSlotitem>, String> {
    @Override
    public SparseArray<MstSlotitem> convertFromSupportedType(String s) {
        Type sparseArrayType = new TypeToken<SparseArray<MstSlotitem>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstSlotitem.class))
            .create();

        SparseArray<MstSlotitem> map = gson.fromJson(s, sparseArrayType);

        if (map == null) {
            map = new SparseArray<>();
        }

        return map;
    }

    @Override
    public String convertToSupportedType(SparseArray<MstSlotitem> map) {
        Type sparseArrayType = new TypeToken<SparseArray<MstSlotitem>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstSlotitem.class))
            .create();

        return gson.toJson(map);
    }
}
