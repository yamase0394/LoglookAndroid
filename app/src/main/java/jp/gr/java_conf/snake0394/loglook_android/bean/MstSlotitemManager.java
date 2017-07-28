package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;

/**
 * 装備品idと装備品データを対応させます
 */
public enum MstSlotitemManager {
    INSTANCE;

    private SparseArray<MstSlotitem> idToMstSlotitemSparseArray;

     MstSlotitemManager() {
         MstDataStorage storage = new MstDataStorage(App.getInstance());
         this.idToMstSlotitemSparseArray = storage.getMstSlotitemSparseArray();
    }

    public void put(MstSlotitem mstSlotitem) {
        idToMstSlotitemSparseArray.put(mstSlotitem.getId(), mstSlotitem);
    }

    public MstSlotitem getMstSlotitem(int id) {
        return idToMstSlotitemSparseArray.get(id);
    }

    public void serialize() {
        MstDataStorage storage = new MstDataStorage(App.getInstance());
        storage.setMstSlotitemSparseArray(this.idToMstSlotitemSparseArray);
    }
}

