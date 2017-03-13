package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorageSpotRepository;

/**
 * Created by snake0394 on 2016/08/08.
 */
public enum MstUseitemManager {
    INSTANCE;

    private SparseArray<MstUseitem> idToMstUseitemSparseArray;

    MstUseitemManager() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        this.idToMstUseitemSparseArray = storage.mstUseitemSparseArray;
    }

    public void put(MstUseitem mstUseitem) {
        idToMstUseitemSparseArray.put(mstUseitem.getId(), mstUseitem);
    }

    public MstUseitem getMstUseitem(int id) {
        return idToMstUseitemSparseArray.get(id);
    }

    public void serialize() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mstUseitemSparseArray = this.idToMstUseitemSparseArray;
        MstDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}
