package jp.gr.java_conf.snake0394.loglook_android;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.storage.RealmInt;

/**
 * Created by snake0394 on 2016/10/29.
 */

public class ShipUtility {
    /**
     * @param myShip
     * @return 砲撃戦の基本攻撃力
     */
    public static float getShellingBasicAttackPower(MyShip myShip) {
        try (Realm realm = Realm.getDefaultInstance()) {
            MstShip mstShip = realm.where(MstShip.class)
                    .equalTo("id", myShip.getShipId())
                    .findFirst();
            switch (ShipType.toShipType(mstShip.getStype())) {
                case 軽空母:
                case 正規空母:
                case 装甲空母:
                    return getShellingBasicAttackPowerCV(myShip);
            }

            float result = 0;
            result += myShip.getKaryoku()
                    .get(0)
                    .getValue() + 5;

            List<Integer> slotItemIdList = new ArrayList<>();
            for (RealmInt value : myShip.getSlot()) {
                slotItemIdList.add(value.getValue());
            }
            slotItemIdList.add(myShip.getSlotEx());

            for (int id : slotItemIdList) {
                MySlotItem mySlotItem = realm.where(MySlotItem.class)
                        .equalTo("id", id)
                        .findFirst();
                if (mySlotItem == null || id == 0 || id == -1) {
                    continue;
                }

                //速吸は艦攻を装備しているとき空母の計算式になる
                MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();
                if (EquipType2.toEquipType2(mstSlotitem.getType().get(2).getValue()) == EquipType2.艦上攻撃機) {
                    return getShellingBasicAttackPowerCV(myShip);
                }

                result += SlotItemUtility.getShellingImprovementFirepower(mstSlotitem, mySlotItem.getLevel());
            }
            return result;
        }
    }

    /**
     * @param myShip
     * @return 空母の砲撃戦の基本攻撃力
     */
    private static float getShellingBasicAttackPowerCV(MyShip myShip) {
        float bomb = 0;
        float improvementFirePower = 0;

        List<Integer> slotItemIdList = new ArrayList<>();
        for (RealmInt realmInt : myShip.getSlot()) {
            slotItemIdList.add(realmInt.getValue());
        }
        slotItemIdList.add(myShip.getSlotEx());

        try(Realm realm = Realm.getDefaultInstance()) {
            for (int id : slotItemIdList) {
                MySlotItem mySlotItem = realm.where(MySlotItem.class).equalTo("id", id).findFirst();
                if (mySlotItem == null) {
                    continue;
                }

                MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();

                improvementFirePower += SlotItemUtility.getShellingImprovementFirepower(mstSlotitem, mySlotItem.getLevel());

                bomb += mstSlotitem.getBaku();
            }
        }

        return (float) (Math.floor((myShip.getKaryoku()
                .get(0)
                .getValue() + myShip.getRaisou()
                .get(0)
                .getValue() + Math.floor(bomb * 1.3) + improvementFirePower) * 1.5) + 55);
    }

    /**
     * @param myShip
     * @return 雷撃戦の基本攻撃力
     */
    public static float getTorpedoSalvoBasicAttackPower(MyShip myShip) {
        float result = 0;
        result += myShip.getRaisou()
                .get(0)
                .getValue();

        List<Integer> slotItemIdList = new ArrayList<>();
        for (RealmInt realmInt : myShip.getSlot()) {
            slotItemIdList.add(realmInt.getValue());
        }
        slotItemIdList.add(myShip.getSlotEx());
        try(Realm realm = Realm.getDefaultInstance()) {
            for (int id : slotItemIdList) {
                MySlotItem mySlotItem = realm.where(MySlotItem.class).equalTo("id", id).findFirst();
                if (mySlotItem == null) {
                    continue;
                }

                MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();

                result += SlotItemUtility.getTorpedoSalvoImprovementPower(mstSlotitem, mySlotItem.getLevel());
            }
        }

        return result;
    }

    /**
     * @param myShip
     * @return 夜戦の基本攻撃力
     */
    public static float getNightBattleBasicAttackPower(MyShip myShip) {
        float result = 0;
        result += myShip.getKaryoku()
                .get(0)
                .getValue() + myShip.getRaisou()
                .get(0)
                .getValue();

        try (Realm realm = Realm.getDefaultInstance()) {
            for (RealmInt id : myShip.getSlot()) {
                MySlotItem mySlotItem = realm.where(MySlotItem.class)
                        .equalTo("id", id.getValue())
                        .findFirst();
                if (mySlotItem == null) {
                    break;
                }

                MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();

                result += SlotItemUtility.getNightBattleImprovementFirepower(mstSlotitem, mySlotItem.getLevel());
            }
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

        List<Integer> slotItemIdList = new ArrayList<>();
        for (RealmInt realmInt : myShip.getSlot()) {
            slotItemIdList.add(realmInt.getValue());
        }
        slotItemIdList.add(myShip.getSlotEx());
        try (Realm realm = Realm.getDefaultInstance()) {
            for (int id : slotItemIdList) {
                MySlotItem mySlotItem = realm.where(MySlotItem.class)
                        .equalTo("id", id)
                        .findFirst();
                if (mySlotItem == null || id == 0 || id == -1) {
                    continue;
                }

                MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();

                equipmentBasicAASum += mstSlotitem.getTyku();

                modifiedEquipmentAASum += SlotItemUtility.getAdjustedAA(mstSlotitem);
                modifiedEquipmentAASum += SlotItemUtility.getImprovementAdjustedAA(mstSlotitem, mySlotItem.getLevel());
            }
        }

        float x = myShip.getTaiku()
                .get(0)
                .getValue() - equipmentBasicAASum + modifiedEquipmentAASum;

        int a = 1;
        if (myShip.getSlot()
                .get(0)
                .getValue() != -1) {
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
        List<Integer> slotItemIdList = new ArrayList<>();
        for (RealmInt realmInt : myShip.getSlot()) {
            slotItemIdList.add(realmInt.getValue());
        }
        slotItemIdList.add(myShip.getSlotEx());
        try (Realm realm = Realm.getDefaultInstance()) {
            for (int slotitemId : slotItemIdList) {
                MySlotItem mySlotItem = realm.where(MySlotItem.class)
                        .equalTo("id", slotitemId)
                        .findFirst();
                if (mySlotItem != null || slotitemId == -1 || slotitemId == 0) {
                    continue;
                }
                MstSlotitem mstSlotitem = realm.where(MstSlotitem.class).equalTo("id", mySlotItem.getMstId()).findFirst();

                modifiedEquipmentAA += SlotItemUtility.getAdjustedFleetAA(mstSlotitem);
                modifiedEquipmentAA += SlotItemUtility.getImprovementAdjustedFleetAA(mstSlotitem, mySlotItem.getLevel());
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
        return (int) Math.floor(((getAdjustedAA(myShip) + DeckUtility.INSTANCE.getAdjustedFleetAA(deck, formation)) * AACIModifier) / 10);
    }


}

