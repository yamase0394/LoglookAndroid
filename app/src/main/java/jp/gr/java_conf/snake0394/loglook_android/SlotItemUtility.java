package jp.gr.java_conf.snake0394.loglook_android;

import java.util.Objects;

import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType3;

/**
 * Created by snake0394 on 2016/10/29.
 */
public class SlotItemUtility {

    /**
     * 改修によって上昇した分の砲撃戦火力を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する砲撃戦火力
     */
    public static float getShellingImprovementFirepower(MstSlotitem mstSlotitem, int improvementLevel) {
        //改修していない
        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 小口径主砲:
            case 中口径主砲:
            case 副砲:
            case 対艦強化弾:
            case 高射装置:
            case 探照灯:
            case 大型探照灯:
            case 対空機銃:
            case 上陸用舟艇:
            case 特型内火艇:
                return (float) (1 * Math.sqrt(improvementLevel));
            case 大口径主砲:
            case 大口径主砲Ⅱ:
                return (float) (1.5 * Math.sqrt(improvementLevel));
            case 爆雷:
            case ソナー:
                return (float) (0.75 * Math.sqrt(improvementLevel));
        }
        return 0;
    }

    /**
     * 改修によって上昇した分の雷撃戦火力を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する雷撃戦火力
     */
    public static float getTorpedoSalvoImprovementPower(MstSlotitem mstSlotitem, int improvementLevel) {
        //改修していない
        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 魚雷:
            case 潜水艦魚雷:
            case 対空機銃:
                return (float) (1.2 * Math.sqrt(improvementLevel));
        }
        return 0;
    }

    /**
     * 改修によって上昇した分の夜戦火力を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する夜戦火力
     */
    public static float getNightBattleImprovementFirepower(MstSlotitem mstSlotitem, int improvementLevel) {
        //改修していない
        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 小口径主砲:
            case 中口径主砲:
            case 大口径主砲:
            case 大口径主砲Ⅱ:
            case 副砲:
            case 対艦強化弾:
            case 高射装置:
            case 探照灯:
            case 大型探照灯:
            case 魚雷:
            case 潜水艦魚雷:
            case 上陸用舟艇:
            case 特型内火艇:
                return (float) (1 * Math.sqrt(improvementLevel));
        }
        return 0;
    }

    /**
     * 改修によって上昇した分の対潜火力を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する対潜火力
     */
    public static float getImprovementASW(MstSlotitem mstSlotitem, int improvementLevel) {
        //改修していない
        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 爆雷:
            case ソナー:
                return (float) (1 * Math.sqrt(improvementLevel));
        }

        return 0;
    }

    /**
     * 改修によって上昇した分の命中を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する命中
     */
    public static float getImprovementAccuracy(MstSlotitem mstSlotitem, int improvementLevel) {
        //改修していない
        if (improvementLevel == 0) {
            return 0;
        }

        /*
        switch (EquipType2.toEquipType2(mstSlotitem.getType().get(2))) {
            case 小口径主砲:
            case 中口径主砲:
            case 大口径主砲:
            case 副砲:
            case 対艦強化弾:
                return (float) (1 * Math.sqrt(improvementLevel));
        }
        */

        return 0;
    }

    /**
     * 改修によって上昇した分の索敵を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する索敵
     */
    public static float getImprovementLOS(MstSlotitem mstSlotitem, int improvementLevel) {

        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 小型電探:
                return (float) (1.25 * Math.sqrt(improvementLevel));
            case 大型電探:
                return (float) (1.4 * Math.sqrt(improvementLevel));
            case 艦上偵察機:
            case 艦上偵察機Ⅱ:
            case 水上偵察機:
                return (float) (1.2 * Math.sqrt(improvementLevel));
            case 水上爆撃機:
                return (float) (1.15 * Math.sqrt(improvementLevel));
            default:
                return 0;
        }
    }

    /**
     * 改修によって上昇した分の対空を返します
     * 制空値計算時のみ有効
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する対空
     */
    public static float getImprovementAA(MstSlotitem mstSlotitem, int improvementLevel) {

        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 艦上戦闘機:
            case 水上戦闘機:
                return 0.2f * improvementLevel;
            case 艦上爆撃機:
                if (mstSlotitem.getTyku() > 0) {
                    return 0.25f * improvementLevel;
                } else {
                    return 0;
                }
            default:
                return 0;
        }
    }

    /**
     * 改修によって上昇した分の爆装を返します
     * 航空戦のみ有効
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する爆装
     */
    public static float getImprovementDivebomb(MstSlotitem mstSlotitem, int improvementLevel) {
        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 水上爆撃機:
                return 0.2f * improvementLevel;
            default:
                return 0;
        }
    }

    /**
     * 改修によって上昇した分の回避を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する回避
     */
    public static float getImprovementEvation(MstSlotitem mstSlotitem, int improvementLevel) {
        return 0;
    }

    /**
     * 改修によって上昇した分の装甲を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修で上昇する装甲
     */
    public static float getImprovementArmor(MstSlotitem mstSlotitem, int improvementLevel) {
        return 0;
    }

    /**
     * @param mstSlotitem
     * @return 装備倍率(加重対空) × 装備対空値
     */
    public static float getAdjustedAA(MstSlotitem mstSlotitem) {

        int slotitemBasicAA = mstSlotitem.getTyku();

        //装備倍率
        switch (EquipType3.toEquipType3(mstSlotitem.getType()
                .get(3).getValue())) {
            case 高角砲:
            case 高射装置:
                return slotitemBasicAA * 4;
            case 対空機銃:
                return slotitemBasicAA * 6;
            case 電探:
                return slotitemBasicAA * 3;
            default:
                return 0;
        }
    }

    /**
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修係数(加重対空) × √(★改修値)
     */
    public static float getImprovementAdjustedAA(MstSlotitem mstSlotitem, int improvementLevel) {

        if (Objects.equals(improvementLevel, 0)) {
            return 0;
        }

        switch (EquipType3.toEquipType3(mstSlotitem.getType()
                .get(3).getValue())) {
            case 高角砲:
                return (float) (Math.sqrt(improvementLevel) * 3);
            case 対空機銃:
                return (float) (Math.sqrt(improvementLevel) * 4);
            default:
                return 0;
        }
    }

    /**
     * @param mstSlotitem
     * @return 装備倍率(艦隊防空) × 装備対空値
     */
    public static float getAdjustedFleetAA(MstSlotitem mstSlotitem) {

        int slotitemBasicAA = mstSlotitem.getTyku();

        //装備倍率
        switch (EquipType3.toEquipType3(mstSlotitem.getType()
                .get(3).getValue())) {
            case 小口径主砲:
            case 中口径主砲:
            case 大口径主砲:
            case 副砲:
            case 対空機銃:
            case 艦上戦闘機:
            case 艦上爆撃機:
            case 水上機:
                return slotitemBasicAA * 0.2f;
            case 高角砲:
            case 高射装置:
                return slotitemBasicAA * 0.35f;
            case 電探:
                return slotitemBasicAA * 0.4f;
            case 対空強化弾:
                return slotitemBasicAA * 0.6f;
            default:
                return 0;
        }
    }

    /**
     * @param mstSlotitem
     * @param improvementLevel 改修度
     * @return 改修係数(艦隊防空) × √(★改修値)
     */
    public static float getImprovementAdjustedFleetAA(MstSlotitem mstSlotitem, int improvementLevel) {

        if (Objects.equals(improvementLevel, 0)) {
            return 0;
        }

        switch (EquipType3.toEquipType3(mstSlotitem.getType().get(3).getValue())) {
            case 高角砲:
                if (hasAADirector(mstSlotitem)) {
                    return (float) (Math.sqrt(improvementLevel) * 3);
                } else {
                    return (float) (Math.sqrt(improvementLevel) * 2);
                }
            case 高射装置:
                return (float) (Math.sqrt(improvementLevel) * 2);
            case 電探:
                return (float) (Math.sqrt(improvementLevel) * 1.5);
            default:
                return 0;
        }
    }

    /**
     * 熟練度、改修による上昇を含む制空値を返します
     *
     * @param mstSlotitem
     * @param slotNum          搭載機数
     * @param improvementLevel 改修レベル
     * @param alv              熟練度
     * @return 熟練度、改修による上昇を含む制空値
     */
    public static int getFighterPower(MstSlotitem mstSlotitem, int slotNum, int improvementLevel, int alv) {

        switch (EquipType2.toEquipType2(mstSlotitem.getType()
                .get(2).getValue())) {
            case 艦上戦闘機:
            case 水上戦闘機:
            case 艦上攻撃機:
            case 水上爆撃機:
            case 艦上爆撃機:
                return (int) ((mstSlotitem.getTyku() + getImprovementAA(mstSlotitem, improvementLevel)) * Math.sqrt(slotNum) + getjukurenSeiku(mstSlotitem, alv));
            default:
                return 0;
        }
    }

    /**
     * @param mstSlotItem
     * @param alv         熟練度
     * @return 熟練度によって上昇した制空値
     */
    public static int getjukurenSeiku(MstSlotitem mstSlotItem, int alv) {

        if (Objects.equals(alv, 0)) {
            return 0;
        }

        switch (EquipType2.toEquipType2(mstSlotItem.getType()
                .get(2).getValue())) {
            case 艦上戦闘機:
            case 水上戦闘機:
                switch (alv) {
                    case 1:
                        return 1;
                    case 2:
                        return 3;
                    case 3:
                        return 7;
                    case 4:
                        return 11;
                    case 5:
                        return 16;
                    case 6:
                        return 17;
                    case 7:
                        return 25;
                    default:
                        return 0;
                }
            case 艦上爆撃機:
            case 艦上攻撃機:
                switch (alv) {
                    case 1:
                    case 2:
                        return 1;
                    case 3:
                    case 4:
                    case 5:
                        return 2;
                    case 6:
                    case 7:
                        return 3;
                    default:
                        return 0;
                }
            case 水上爆撃機:
                switch (alv) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                    case 4:
                        return 3;
                    case 5:
                        return 5;
                    case 6:
                        return 6;
                    case 7:
                        return 9;
                    default:
                        return 0;
                }
            default:
                return 0;
        }
    }
    
    private static boolean hasAADirector(MstSlotitem slotitem) {
        switch (slotitem.getName()) {
            case "10cm連装高角砲+高射装置":
            case "12.7cm高角砲+高射装置":
            case "90mm単装高角砲":
            case "5inch連装砲 Mk.28 mod.2":
                return true;
            default:
                return false;
        }
    }
}
