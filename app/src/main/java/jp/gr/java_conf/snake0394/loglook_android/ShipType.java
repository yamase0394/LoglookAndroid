package jp.gr.java_conf.snake0394.loglook_android;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by snake0394 on 2016/10/29.
 */

public enum ShipType {
    海防艦(1),
    駆逐艦(2),
    軽巡洋艦(3),
    重雷装巡洋艦(4),
    重巡洋艦(5),
    航空巡洋艦(6),
    軽空母(7),
    巡洋戦艦(8),
    戦艦(9),
    航空戦艦(10),
    正規空母(11),
    超弩級戦艦(12),
    潜水艦(13),
    潜水空母(14),
    輸送艦(15),
    水上機母艦(16),
    揚陸艦(17),
    装甲空母(18),
    工作艦(19),
    潜水母艦(20),
    練習巡洋艦(21);

    private int id;

    private static Map<Integer, ShipType> toShipTypeMap = new HashMap<>();

    static {
        for (ShipType st : values()) {
            toShipTypeMap.put(st.id, st);
        }
    }

    ShipType(int id) {
        this.id = id;
    }

    public static ShipType toShipType(int id) {
        return toShipTypeMap.get(id);
    }

}
