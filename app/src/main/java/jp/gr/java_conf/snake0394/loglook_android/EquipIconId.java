package jp.gr.java_conf.snake0394.loglook_android;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by snake0394 on 2016/10/29.
 */

public enum EquipIconId {
    小口径主砲(1, R.drawable.red_gun_light),
    中口径主砲(2, R.drawable.red_gun_medium),
    大口径主砲(3, R.drawable.red_gun_heavy),
    副砲(4, R.drawable.yellow_gun),
    魚雷(5, R.drawable.torpedo),
    艦上戦闘機(6, R.drawable.green_plane),
    艦上爆撃機(7, R.drawable.red_plane),
    艦上攻撃機(8, R.drawable.bule_plane),
    艦上偵察機(9, R.drawable.yellow_plane),
    水上機(10, R.drawable.seaplane),
    電探(11, R.drawable.rader),
    対空強化弾(12, R.drawable.green_ammo),
    対艦強化弾(13, R.drawable.red_ammo),
    応急修理要員(14, R.drawable.emergency_repair),
    対空機銃(15, R.drawable.green_gun_mg),
    高角砲(16, R.drawable.green_gun_dp),
    爆雷(17, R.drawable.depth_charge),
    ソナー(18, R.drawable.sonar),
    機関部強化(19, R.drawable.turbine),
    上陸用舟艇(20, R.drawable.landing_craft),
    オートジャイロ(21, R.drawable.heli),
    対潜哨戒機(22, R.drawable.subplane),
    追加装甲(23, R.drawable.bulge),
    探照灯(24, R.drawable.searchlight),
    簡易輸送部材(25, R.drawable.drum),
    艦艇修理施設(26, R.drawable.facility),
    照明弾(27, R.drawable.flare),
    司令部施設(28, R.drawable.command),
    航空要員(29, R.drawable.ap),
    高射装置(30, R.drawable.aafd),
    対地装備(31, R.drawable.agat),
    水上艦要員(32, R.drawable.ssp),
    大型飛行艇(33, R.drawable.large_flying_boat),
    戦闘糧食(34, R.drawable.combat_provision),
    補給物資(35, R.drawable.supply),
    特型内火艇(36, R.drawable.special_amphibious_tank),
    陸上攻撃機(37, R.drawable.land_based_attack_aircraft),
    局地戦闘機(38, R.drawable.interceptor_fighter),
    UNKNOWN(300, R.drawable.unknown),
    EMPTY(301, R.drawable.empty),
    NOT_AVAILABLE(302, R.drawable.not_available);

    private int id;
    private int imageId;

    private static Map<Integer, EquipIconId> toEquipIconIdMap = new HashMap<>();

    static {
        for (EquipIconId et : values()) {
            toEquipIconIdMap.put(et.id, et);
        }
    }

    EquipIconId(int id, int imageId) {
        this.id = id;
        this.imageId = imageId;
    }

    public static EquipIconId toEquipIconId(int id) {
        if (toEquipIconIdMap.containsKey(id)) {
            return toEquipIconIdMap.get(id);
        }
        return UNKNOWN;
    }

    public int getImageId() {
        return imageId;
    }
}
