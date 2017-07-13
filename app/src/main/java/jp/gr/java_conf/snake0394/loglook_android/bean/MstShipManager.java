package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorageSpotRepository;

/**
 * Created by snake0394 on 2016/08/04.
 */
public enum MstShipManager {
    INSTANCE;

    private SparseArray<MstShip> idToMstShipSparseArray;

    MstShipManager() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        this.idToMstShipSparseArray = storage.mstShipSparseArray;
    }

    public void put(MstShip mstShip) {
        idToMstShipSparseArray.put(mstShip.getId(), mstShip);
    }

    /**
     * id=-1に対応するオブジェクトが何故か入ってる
     * @param id
     * @return
     */
    public MstShip getMstShip(int id) {
        return idToMstShipSparseArray.get(id);
    }

    public boolean contains(int id) {
        return idToMstShipSparseArray.indexOfKey(id) >= 0;
    }

    public void serialize() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mstShipSparseArray = this.idToMstShipSparseArray;
        MstDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}
