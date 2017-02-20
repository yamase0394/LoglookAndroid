package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorageSpotRepository;

/**
 * 装備品idと装備品データを対応させます
 */
public enum MstSlotitemManager {
    INSTANCE;

    private Map<Integer, MstSlotitem> mstSlotitemMap = new HashMap<>();

     MstSlotitemManager() {
         MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
         this.mstSlotitemMap = storage.mstSlotitemMap;
    }

    public void put(MstSlotitem mstSlotitem) {
        mstSlotitemMap.put(mstSlotitem.getId(), mstSlotitem);
    }

    public MstSlotitem getMstSlotitem(int id) {
        return mstSlotitemMap.get(id);
    }

    public void serialize() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mstSlotitemMap = this.mstSlotitemMap;
        MstDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}

