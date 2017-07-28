package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;

/**
 * Created by snake0394 on 2016/08/08.
 */
public enum MstUseitemManager {
    INSTANCE;

    private SparseArray<MstUseitem> idToMstUseitemSparseArray;

    MstUseitemManager() {
        MstDataStorage storage = new MstDataStorage(App.getInstance());
        this.idToMstUseitemSparseArray = storage.getMstUseitemSparseArray();
    }

    public void put(MstUseitem mstUseitem) {
        idToMstUseitemSparseArray.put(mstUseitem.getId(), mstUseitem);
    }

    public MstUseitem getMstUseitem(int id) {
        return idToMstUseitemSparseArray.get(id);
    }

    public void serialize() {
        MstDataStorage storage = new MstDataStorage(App.getInstance());
        storage.setMstUseitemSparseArray(this.idToMstUseitemSparseArray);
    }
}
