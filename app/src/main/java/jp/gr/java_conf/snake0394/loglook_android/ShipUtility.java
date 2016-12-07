package jp.gr.java_conf.snake0394.loglook_android;

import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;

/**
 * Created by snake0394 on 2016/10/29.
 */

public class ShipUtility {
    /**
     * @param myShip
     * @return 砲撃戦の基本攻撃力
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
     * @param myShip
     * @return 空母の砲撃戦の基本攻撃力
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
     * @param myShip
     * @return 夜戦の基本攻撃力
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

    /**
     * @param myShip
     * @return 加重対空値
     */
    public static double getAdjustedAA(MyShip myShip) {
        int equipmentBasicAASum = 0;
        float modifiedEquipmentAASum = 0;

        for (int id : myShip.getSlot()) {
            if (!MySlotItemManager.INSTANCE.contains(id)) {
                break;
            }

            MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(id);
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

            int slotitemBasicAA = mstSlotitem.getTyku();
            equipmentBasicAASum += slotitemBasicAA;

            //装備倍率
            switch (EquipIconId.toEquipIconId(mstSlotitem.getType().get(3))) {
                case 高角砲:
                case 高射装置:
                    modifiedEquipmentAASum += slotitemBasicAA * 4;
                    break;
                case 対空機銃:
                    modifiedEquipmentAASum += slotitemBasicAA * 6;
                    break;
                case 電探:
                    modifiedEquipmentAASum += slotitemBasicAA * 3;
                    break;
            }

            int level = mySlotItem.getLevel();

            //改修係数
            switch (EquipIconId.toEquipIconId(mstSlotitem.getType().get(3))) {
                case 高角砲:
                    modifiedEquipmentAASum += Math.sqrt(level) * 3;
                    break;
                case 対空機銃:
                    modifiedEquipmentAASum += Math.sqrt(level) * 4;
                    break;
            }
        }

        float x = myShip.getTaiku().get(0) - equipmentBasicAASum + modifiedEquipmentAASum;

        int a = 1;
        if (myShip.getSlot().get(0) != -1) {
            a = 2;
        }

        return a * Math.floor(x / a);
    }

    /**
     * @param myShip
     * @return 艦隊対空ボーナス値
     */
    public static double getAdjustedFleetAA(MyShip myShip) {
        float modifiedEquipmentAA = 0;
        for (int slotitemId : myShip.getSlot()) {
            if (!MySlotItemManager.INSTANCE.contains(slotitemId) || slotitemId == -1) {
                break;
            }
            MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(slotitemId);
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
            int slotitemBasicAA = mstSlotitem.getTyku();

            //装備倍率
            switch (EquipIconId.toEquipIconId(mstSlotitem.getType().get(3))) {
                case 小口径主砲:
                case 中口径主砲:
                case 大口径主砲:
                case 副砲:
                case 対空機銃:
                case 艦上戦闘機:
                case 艦上爆撃機:
                case 水上機:
                    modifiedEquipmentAA += slotitemBasicAA * 0.2;
                    break;
                case 高角砲:
                case 高射装置:
                    modifiedEquipmentAA += slotitemBasicAA * 0.35;
                    break;
                case 電探:
                    modifiedEquipmentAA += slotitemBasicAA * 0.4;
                    break;
                case 対空強化弾:
                    modifiedEquipmentAA += slotitemBasicAA * 0.6;
                    break;
            }

            //改修係数
            int level = mySlotItem.getLevel();
            switch (EquipIconId.toEquipIconId(mstSlotitem.getType().get(3))) {
                case 高角砲:
                    modifiedEquipmentAA += Math.sqrt(level) * 3;
                    break;
                case 電探:
                    modifiedEquipmentAA += Math.sqrt(level) * 1.5;
                    break;
            }

        }
        return Math.floor(modifiedEquipmentAA);
    }

    /**
     * @param myShip
     * @param slotNum 相手スロット機数
     * @return 割合撃墜機数
     */
    public static int getProportionalAirDefense(MyShip myShip, int slotNum) {
        return (int) Math.floor(getAdjustedAA(myShip) / 400 * slotNum);
    }

    /**
     * @param myShip
     * @param deck
     * @param formation
     * @param AACIModifier 対空CI変動ボーナス補正
     * @return 固定撃墜機数
     */
    public static int getFixedAirDefense(MyShip myShip, Deck deck, String formation, float AACIModifier) {
        return (int) Math.floor(((getAdjustedAA(myShip) + DeckUtility.getAdjustedFleetAA(deck, formation)) * AACIModifier) / 10);
    }


}

