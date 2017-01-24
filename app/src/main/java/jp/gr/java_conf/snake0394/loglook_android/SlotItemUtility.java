package jp.gr.java_conf.snake0394.loglook_android;

import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;

/**
 * Created by snake0394 on 2016/10/29.
 */
public class SlotItemUtility {

    /**
     * 改修によって上昇した分の砲撃戦火力を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     *
     * @return 改修で上昇する砲撃戦火力
     */
    public static float getShellingImprovementFirepower(MstSlotitem mstSlotitem,int improvementLevel) {
        //改修していない
        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType.toEquipType(mstSlotitem.getType().get(2))) {
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
     * 改修によって上昇した分の夜戦火力を返します
     *
     * @param mstSlotitem
     * @param improvementLevel 改修度
     *
     * @return 改修で上昇する夜戦火力
     */
    public static float getNightBattleImprovementFirepower(MstSlotitem mstSlotitem,int improvementLevel) {
        //改修していない
        if (improvementLevel == 0) {
            return 0;
        }

        switch (EquipType.toEquipType(mstSlotitem.getType().get(2))) {
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
     * @param mySlotItem
     *
     * @return 熟練度によって上昇した制空値
     */
    public static int getjukurenSeiku(MySlotItem mySlotItem) {
        MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
        switch (EquipType.toEquipType(mstSlotitem.getType().get(2))) {
            case 艦上戦闘機:
            case 水上戦闘機:
                switch (mySlotItem.getAlv()) {
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
                }
                break;
            case 艦上爆撃機:
            case 艦上攻撃機:
                switch (mySlotItem.getAlv()) {
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
                }
                break;
            case 水上爆撃機:
                switch (mySlotItem.getAlv()) {
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
                }
                break;
        }
        return 0;
    }
}
