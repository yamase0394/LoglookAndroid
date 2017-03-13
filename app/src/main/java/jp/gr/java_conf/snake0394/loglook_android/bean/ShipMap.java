package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.HashMap;

/**
 * Created by snake0394 on 2016/08/04.
 */

public enum ShipMap {
    INSTANCE;

    private HashMap<Integer, MstShip> shipMap = new HashMap<>();

    private ShipMap() {
    }

    public void put(int id, MstShip mstShip) {
        shipMap.put(id, mstShip);
    }

    /**
     * 所有艦娘IDから対応する{@link MstShip}を返します
     *
     * @param id 所有艦娘ID
     * @return 所有艦娘IDに対応するMstShip
     */
    public MstShip getMstShip(int id) {
        return shipMap.get(id);
    }
}
