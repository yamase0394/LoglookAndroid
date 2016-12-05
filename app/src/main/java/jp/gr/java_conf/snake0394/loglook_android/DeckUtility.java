package jp.gr.java_conf.snake0394.loglook_android;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.bean.Basic;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;

import static jp.gr.java_conf.snake0394.loglook_android.SlotItemUtility.getjukurenSeiku;

/**
 * Created by snake0394 on 2016/08/29.
 */
public class DeckUtility {
    /**
     * @return 艦隊の制空値
     */
    public static int getSeiku(Deck deck) {
        List<Integer> shipId = deck.getShipId();
        int seiku = 0;
        for (int i = 0; i < shipId.size(); i++) {
            //空きの場合
            if (shipId.get(i) == -1) {
                break;
            }
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId.get(i));

            //有効なスロットの装備を一つずつ見る
            for (int j = 0; j < myShip.getSlotnum(); j++) {
                int slotitemId = myShip.getSlot().get(j);
                //所有装備に無いIDの場合は繰り返しを終える
                if (!MySlotItemManager.INSTANCE.contains(slotitemId) || slotitemId == -1) {
                    break;
                }
                MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(slotitemId);
                MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                //制空に関係のある装備のみ制空値を求める
                switch (EquipType.toEquipType(mstSlotitem.getType().get(2))) {
                    case 艦上戦闘機:
                        seiku += (int) ((mstSlotitem.getTyku() + 0.2 * mySlotItem.getLevel()) * Math.sqrt(myShip.getOnslot().get(j)) + getjukurenSeiku(mySlotItem));
                        break;
                    case 水上戦闘機:
                    case 艦上攻撃機:
                    case 水上爆撃機:
                        seiku += (int) (mstSlotitem.getTyku() * Math.sqrt(myShip.getOnslot().get(j)) + getjukurenSeiku(mySlotItem));
                        break;
                    case 艦上爆撃機:
                        seiku += (int) ((mstSlotitem.getTyku() + 0.25 * mySlotItem.getLevel()) * Math.sqrt(myShip.getOnslot().get(j)) + getjukurenSeiku(mySlotItem));
                }
            }
        }
        return seiku;
    }

    /**
     * @return 艦隊の触接開始率
     */
    public static int getTouchStartRate(Deck deck) {
        List<Integer> shipId = deck.getShipId();
        float touchStartRate = 0;
        for (int i = 0; i < shipId.size(); i++) {
            //空きの場合
            if (shipId.get(i) == -1) {
                break;
            }
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId.get(i));
            float rate = 0;
            for (int j = 0; j < myShip.getSlotnum(); j++) {
                int slotitemId = myShip.getSlot().get(j);
                if (!MySlotItemManager.INSTANCE.contains(slotitemId)) {
                    break;
                }
                MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(slotitemId);
                MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                EquipType et = EquipType.toEquipType(mstSlotitem.getType().get(2));
                switch (et) {
                    case 水上偵察機:
                    case 艦上偵察機:
                    case 大型飛行艇:
                        rate += 0.04 * mstSlotitem.getSaku() * Math.sqrt(myShip.getOnslot().get(j));
                }
            }
            touchStartRate += rate;
        }
        return (int) (touchStartRate * 100);
    }


    /**
     * @return 艦隊の判定式(33)の索敵値
     */
    public static float getSakuteki33(Deck deck) {
        float sakuteki = 0;
        //艦隊の空き数
        int empty = 0;
        for (int i = 0; i < deck.getShipId().size(); i++) {
            int shipId = deck.getShipId().get(i);
            if (!MyShipManager.INSTANCE.contains(shipId) || shipId == -1) {
                empty = deck.getShipId().size() - i;
                break;
            }
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId);
            int equipSakuSum = 0;
            for (int j = 0; j < myShip.getSlotnum(); j++) {
                int mySlotItemId = myShip.getSlot().get(j);
                if (!MySlotItemManager.INSTANCE.contains(mySlotItemId)) {
                    break;
                }
                MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(mySlotItemId);
                int mstSlotItemId = mySlotItem.getMstId();
                MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mstSlotItemId);
                equipSakuSum += mstSlotitem.getSaku();
                EquipType et = EquipType.toEquipType(mstSlotitem.getType().get(2));
                //改修と係数を考慮した装備の索敵値を足す
                switch (et) {
                    case 艦上戦闘機:
                    case 艦上爆撃機:
                    case 対潜哨戒機:
                    case 探照灯:
                    case 司令部施設:
                    case 航空要員:
                    case 水上艦要員:
                    case 大型ソナー:
                    case 大型飛行艇:
                    case 大型探照灯:
                    case 水上戦闘機:
                        sakuteki += mstSlotitem.getSaku() * 0.6;
                        break;
                    case 小型電探:
                        //大型電探の索敵の改修上昇値は*1.4かも
                        //ただし、索敵33計算の係数が変わるかは不明
                    case 大型電探:
                        float tempSakuteki = (float) (mstSlotitem.getSaku() + 1.25 * Math.sqrt(mySlotItem.getLevel()));
                        sakuteki += tempSakuteki * 0.6;
                        break;
                    case 艦上攻撃機:
                        sakuteki += mstSlotitem.getSaku() * 0.8;
                        break;
                    case 艦上偵察機:
                    case 艦上偵察機Ⅱ:
                        sakuteki += mstSlotitem.getSaku();
                        break;
                    case 水上偵察機:
                        tempSakuteki = (float) (mstSlotitem.getSaku() + 1.2 * Math.sqrt(mySlotItem.getLevel()));
                        sakuteki += tempSakuteki * 1.2;
                        break;
                    case 水上爆撃機:
                        sakuteki += mstSlotitem.getSaku() * 1.1;
                        break;
                }
            }
            //艦船の素敵値を足す
            sakuteki += Math.sqrt(myShip.getSakuteki().get(0) - equipSakuSum);
        }
        //索敵 = -(司令部レベル*0.4)小数点以下を切り上げ + 2*艦隊の空き数
        sakuteki += (float) (-Math.ceil(0.4 * Basic.INSTANCE.getLevel()) + 2 * empty);
        BigDecimal bd = new BigDecimal(Float.toString(sakuteki));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    /**
     * @return 2-5式(秋)の索敵値
     */
    public static float getSakuteki25(Deck deck) {
        double sakuteki = 0;
        for (int i = 0; i < deck.getShipId().size(); i++) {
            int shipId = deck.getShipId().get(i);
            if (!MyShipManager.INSTANCE.contains(shipId) || shipId == -1) {
                break;
            }
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId);
            int equipSakuSum = 0;
            for (int j = 0; j < myShip.getSlotnum(); j++) {
                int mySlotItemId = myShip.getSlot().get(j);
                if (!MySlotItemManager.INSTANCE.contains(mySlotItemId)) {
                    break;
                }
                MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(mySlotItemId);
                int mstSlotItemId = mySlotItem.getMstId();
                MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mstSlotItemId);
                equipSakuSum += mstSlotitem.getSaku();
                EquipType et = EquipType.toEquipType(mstSlotitem.getType().get(2));
                //改修と係数を考慮した装備の索敵値を足す
                switch (et) {
                    case 艦上爆撃機:
                        sakuteki += mstSlotitem.getSaku() * 1.0376255;
                        break;
                    case 艦上攻撃機:
                        sakuteki += mstSlotitem.getSaku() * 1.3677954;
                        break;
                    case 艦上偵察機:
                        sakuteki += mstSlotitem.getSaku() * 1.6592780;
                        break;
                    case 水上偵察機:
                        sakuteki += mstSlotitem.getSaku() * 2.0000000;
                        break;
                    case 水上爆撃機:
                        sakuteki += mstSlotitem.getSaku() * 1.7787282;
                        break;
                    case 小型電探:
                        sakuteki += mstSlotitem.getSaku() * 1.0045358;
                        break;
                    case 大型電探:
                        sakuteki += mstSlotitem.getSaku() * 0.9906638;
                        break;
                    case 探照灯:
                        sakuteki += mstSlotitem.getSaku() * 0.9067950;
                        break;
                }
            }
            //艦船の素敵値を足す
            sakuteki += Math.sqrt(myShip.getSakuteki().get(0) - equipSakuSum) * 1.6841056;
        }
        double sirei = Basic.INSTANCE.getLevel();
        if (sirei % 5 != 0) {
            sirei = (Math.floor(Basic.INSTANCE.getLevel() / 5) + 1) * 5;
        }
        sakuteki += sirei * -0.6142467;
        BigDecimal bd = new BigDecimal(Double.toString(sakuteki));
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static String getCondRecoveryTime(Deck deck) {
        List<Integer> shipId = deck.getShipId();
        int leastCond = 49;
        for (int i = 1; i <= shipId.size(); i++) {
            //空きの場合
            if (shipId.get(i - 1) == -1) {
                continue;
            }
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId.get(i - 1));
            if (leastCond > myShip.getCond()) {
                leastCond = myShip.getCond();
            }
        }
        if ((leastCond - 49) < 0) {
            leastCond = 49 - leastCond;
            float t = leastCond;
            t /= 3;
            t = (float) Math.ceil(t);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(calendar.getTimeInMillis() + TimeUnit.MINUTES.toMillis((long) (t * 3)));
            return sdf.format(calendar.getTime());
        }
        return "";
    }

    public static int getLevelSum(Deck deck) {
        List<Integer> shipId = deck.getShipId();
        int sum = 0;
        for (int i = 0; i < shipId.size(); i++) {
            //空きの場合
            if (shipId.get(i) == -1) {
                continue;
            }
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId.get(i));
            sum += myShip.getLv();
        }

        return sum;
    }

    /**
     * @return 艦隊防空値(単縦陣、梯形陣、単横陣)
     */
    public static float getAdjustedFleetAA(Deck deck, String formation) {
        //艦隊防空値
        int fleetAA = 0;

        for (int shipId : deck.getShipId()) {
            if (!MyShipManager.INSTANCE.contains(shipId) || shipId == -1) {
                break;
            }
            MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId);

            //各艦の艦隊対空ボーナス値
            fleetAA += ShipUtility.getAdjustedFleetAA(myShip);
        }

        //陣形補正
        float formationModifier = 1;
        try {
            switch (formation) {
                case "単縦陣":
                case "梯形陣":
                case "単横陣":
                    formationModifier = 1;
                    break;
                case "複縦陣":
                    formationModifier = 1.2f;
                    break;
                case "輪形陣":
                    formationModifier = 1.6f;
                    break;
            }
        }catch (NullPointerException e){
            //
        }

        return (float) (Math.floor(formationModifier * fleetAA) * 2 / 1.3);
    }
}
