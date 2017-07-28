package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;

/**
 * Created by snake0394 on 2016/08/08.
 */
public enum MstMissionManager {
    INSTANCE;

    private SparseArray<MstMission> idToMstMissionSparseArray;
    private Map<String, MstMission> mstMissionNameMap;

    MstMissionManager() {
        MstDataStorage storage = new MstDataStorage(App.getInstance());
        this.idToMstMissionSparseArray = storage.getMstMissionIdSparseArray();
        this.mstMissionNameMap = storage.getMstMissionNameMap();
    }

    public void put(MstMission mstMission) {
        idToMstMissionSparseArray.put(mstMission.getId(), mstMission);
        mstMissionNameMap.put(mstMission.getName(), mstMission);
    }

    public MstMission getMstMission(int id) {
        return idToMstMissionSparseArray.get(id);
    }

    public MstMission getMstMission(String name) {
        return mstMissionNameMap.get(name);
    }

    public void serialize() {
        MstDataStorage storage = new MstDataStorage(App.getInstance());
        storage.setMstMissionIdSparseArray(this.idToMstMissionSparseArray);
        storage.setMstMissionNameMap(this.mstMissionNameMap);
    }
}
