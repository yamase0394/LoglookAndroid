package jp.gr.java_conf.snake0394.loglook_android.storage;

import java.util.Map;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefString;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstMissionIdMapTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstMissionNameMapTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstShipMapTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstSlotitemMapTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MstUseitemMapTypeConverter;

@Pref(name = "storage_mst_data")
public class MstDataStorage {

    @PrefString(name = "mstShipMap", converter = MstShipMapTypeConverter.class)
    public Map<Integer, MstShip> mstShipMap;

    @PrefString(name = "mstMissionIdMap", converter = MstMissionIdMapTypeConverter.class)
    public Map<Integer, MstMission> mstMissionIdMap;

    @PrefString(name = "mstMissionNameMap", converter = MstMissionNameMapTypeConverter.class)
    public Map<String , MstMission> mstMissionNameMap;

    @PrefString(name = "mstSlotitemMap", converter = MstSlotitemMapTypeConverter.class)
    public Map<Integer, MstSlotitem> mstSlotitemMap;

    @PrefString(name = "mstUseitemMap", converter = MstUseitemMapTypeConverter.class)
    public Map<Integer, MstUseitem> mstUseitemMap;

    public MstDataStorage() {
        //Empty constructor is needed
    }
}