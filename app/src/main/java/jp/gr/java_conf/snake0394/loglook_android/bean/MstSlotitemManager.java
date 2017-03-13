package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorageSpotRepository;

/**
 * 装備品idと装備品データを対応させます
 */
public enum MstSlotitemManager {
    INSTANCE;

    private SparseArray<MstSlotitem> idToMstSlotitemSparseArray;

     MstSlotitemManager() {
         MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
         this.idToMstSlotitemSparseArray = storage.mstSlotitemSparseArray;
    }

    public void put(MstSlotitem mstSlotitem) {
        idToMstSlotitemSparseArray.put(mstSlotitem.getId(), mstSlotitem);
    }

    public MstSlotitem getMstSlotitem(int id) {
        return idToMstSlotitemSparseArray.get(id);
    }

    public void serialize() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mstSlotitemSparseArray = this.idToMstSlotitemSparseArray;
        MstDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}

