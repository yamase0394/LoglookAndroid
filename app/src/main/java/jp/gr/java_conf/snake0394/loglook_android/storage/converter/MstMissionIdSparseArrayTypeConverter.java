package jp.gr.java_conf.snake0394.loglook_android.storage.converter;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.kobakei.spot.converter.TypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;

/**
 * Created by snake0394 on 2017/02/20.
 */
public class MstMissionIdSparseArrayTypeConverter extends TypeConverter<SparseArray<MstMission>, String> {
    @Override
    public SparseArray<MstMission> convertFromSupportedType(String s) {
        Type sparseArrayType = new TypeToken<SparseArray<MstMission>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstMission.class))
            .create();

        SparseArray<MstMission> map = gson.fromJson(s, sparseArrayType);

        if (map == null) {
            map = new SparseArray<>();
        }

        return map;
    }

    @Override
    public String convertToSupportedType(SparseArray<MstMission> map) {
        Type sparseArrayType = new TypeToken<SparseArray<MstMission>>() {}.getType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(sparseArrayType, new SparseArrayTypeAdapter<>(MstMission.class))
            .create();

        return gson.toJson(map);
    }
}
