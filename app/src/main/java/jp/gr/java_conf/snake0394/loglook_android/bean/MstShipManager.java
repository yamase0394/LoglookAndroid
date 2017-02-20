package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorageSpotRepository;

/**
 * Created by snake0394 on 2016/08/04.
 */
public enum MstShipManager {
    INSTANCE;

    private Map<Integer, MstShip> mstShipMap = new HashMap<>();

    MstShipManager() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        this.mstShipMap = storage.mstShipMap;
    }

    public void put(MstShip mstShip) {
        mstShipMap.put(mstShip.getId(), mstShip);
    }

    public MstShip getMstShip(int id) {
        return mstShipMap.get(id);
    }

    public boolean contains(int id) {
        return mstShipMap.containsKey(id);
    }

    public void serialize() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mstShipMap = this.mstShipMap;
        MstDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}
