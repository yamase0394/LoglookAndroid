package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorageSpotRepository;

/**
 * Created by snake0394 on 2016/08/08.
 */
public enum MstUseitemManager {
    INSTANCE;

    private Map<Integer, MstUseitem> mstUseitemMap = new HashMap<>();

    MstUseitemManager() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        this.mstUseitemMap = storage.mstUseitemMap;
    }

    public void put(MstUseitem mstUseitem) {
        mstUseitemMap.put(mstUseitem.getId(), mstUseitem);
    }

    public MstUseitem getMstUseitem(int id) {
        return mstUseitemMap.get(id);
    }

    public void serialize() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mstUseitemMap = this.mstUseitemMap;
        MstDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}
