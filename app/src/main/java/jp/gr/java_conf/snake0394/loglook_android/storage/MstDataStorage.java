package jp.gr.java_conf.snake0394.loglook_android.storage;

import android.util.SparseArray;

import java.util.Map;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefField;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstMissionIdSparseArrayTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstMissionNameMapTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstShipSparseArrayTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstSlotitemSparseArrayTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstUseitemSparseArrayTypeConverter;

@Pref(name = "storage_mst_data")
public class MstDataStorage {

    @PrefField(name = "mstShipSparseArray", converter = MstShipSparseArrayTypeConverter.class)
    public SparseArray<MstShip> mstShipSparseArray;

    @PrefField(name = "mstMissionIdSparseArray", converter = MstMissionIdSparseArrayTypeConverter.class)
    public SparseArray<MstMission> mstMissionIdSparseArray;

    @PrefField(name = "mstMissionNameMap", converter = MstMissionNameMapTypeConverter.class)
    public Map<String , MstMission> mstMissionNameMap;

    @PrefField(name = "mstSlotitemSparseArray", converter = MstSlotitemSparseArrayTypeConverter.class)
    public SparseArray<MstSlotitem> mstSlotitemSparseArray;

    @PrefField(name = "mstUseitemSparseArray", converter = MstUseitemSparseArrayTypeConverter.class)
    public SparseArray<MstUseitem> mstUseitemSparseArray;

    public MstDataStorage() {
        //Empty constructor is needed
    }
}