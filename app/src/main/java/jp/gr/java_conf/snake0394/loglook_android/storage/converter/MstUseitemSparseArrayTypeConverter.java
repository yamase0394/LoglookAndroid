package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstUseitemSparseArrayTypeConverter extends TypeConverter<SparseArray<MstUseitem>, String> {
    @Override
    public SparseArray<MstUseitem> convertFromSupportedType(String s) {
        Type sparseArrayType = new TypeToken<SparseArray<MstUseitem>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstUseitem.class))
            .create();

        SparseArray<MstUseitem> map = gson.fromJson(s, sparseArrayType);

        if (map == null) {
            map = new SparseArray<>();
        }

        return map;
    }

    @Override
    public String convertToSupportedType(SparseArray<MstUseitem> map) {
        Type sparseArrayType = new TypeToken<SparseArray<MstUseitem>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstUseitem.class))
            .create();

        return gson.toJson(map);
    }
}
