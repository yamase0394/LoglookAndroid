package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.MstDataStorageSpotRepository;

/**
 * Created by snake0394 on 2016/08/08.
 */
public enum MstMissionManager {
    INSTANCE;

    private Map<Integer, MstMission> mstMissionIdMap = new HashMap<>();
    private Map<String, MstMission> mstMissionNameMap = new HashMap<>();

    MstMissionManager() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        this.mstMissionIdMap = storage.mstMissionIdMap;
        this.mstMissionNameMap = storage.mstMissionNameMap;
    }

    public void put(MstMission mstMission) {
        mstMissionIdMap.put(mstMission.getId(), mstMission);
        mstMissionNameMap.put(mstMission.getName(), mstMission);
    }

    public MstMission getMstMission(int id) {
        return mstMissionIdMap.get(id);
    }

    public MstMission getMstMission(String name) {
        return mstMissionNameMap.get(name);
    }

    public void serialize() {
        MstDataStorage storage = MstDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mstMissionIdMap = this.mstMissionIdMap;
        storage.mstMissionNameMap = this.mstMissionNameMap;
        MstDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}
