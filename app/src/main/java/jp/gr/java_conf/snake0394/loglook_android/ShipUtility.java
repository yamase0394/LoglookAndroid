package jp.gr.java_conf.snake0394.loglook_android;

import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipType;

/**
 * Created by snake0394 on 2016/10/29.
 */

public class ShipUtility {
    /**
     * 砲撃戦の基本攻撃力を返します
     */
    public static float getShellingBasicAttackPower(MyShip myShip) {
        MstShip mstShip = MstShipManager.INSTANCE.getMstShip(myShip.getShipId());

        switch (ShipType.toShipType(mstShip.getStype())) {
            case 軽空母:
            case 正規空母:
            case 装甲空母:
                return getShellingBasicAttackPowerCV(myShip);
        }

        float result = 0;
        result += myShip.getKaryoku().get(0) + 5;

        for (int id : myShip.getSlot()) {
            if (!MySlotItemManager.INSTANCE.contains(id)) {
                break;
            }
            MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(id);

            //速吸は艦攻を装備しているとき空母の計算式になる
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
            if (EquipType.toEquipType(mstSlotitem.getType().get(2)) == EquipType.艦上攻撃機) {
                return getShellingBasicAttackPowerCV(myShip);
            }

            result += SlotItemUtility.getShellingImprovementFirepower(mstSlotitem, mySlotItem.getLevel());
        }

        return result;
    }

    /**
     * 空母の砲撃戦の基本攻撃力を返します
     */
    private static float getShellingBasicAttackPowerCV(MyShip myShip) {
        float bomb = 0;
        float improvementFirePower = 0;

        for (int id : myShip.getSlot()) {
            if (!MySlotItemManager.INSTANCE.contains(id)) {
                break;
            }

            MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(id);
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

            improvementFirePower += SlotItemUtility.getShellingImprovementFirepower(mstSlotitem, mySlotItem.getLevel());

            bomb += mstSlotitem.getBaku();
        }

        return (float) (Math.floor((myShip.getKaryoku().get(0) + myShip.getRaisou().get(0) + Math.floor(bomb * 1.3) + improvementFirePower) * 1.5) + 55);
    }

    /**
     * 夜戦の基本攻撃力を返します
     */
    public static float getNightBattleBasicAttackPower(MyShip myShip) {
        float result = 0;
        result += myShip.getKaryoku().get(0) + myShip.getRaisou().get(0);

        for (int id : myShip.getSlot()) {
            if (!MySlotItemManager.INSTANCE.contains(id)) {
                break;
            }

            MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(id);
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

            result += SlotItemUtility.getNightBattleImprovementFirepower(mstSlotitem, mySlotItem.getLevel());
        }

        return result;
    }
}
