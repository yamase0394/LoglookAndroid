package jp.gr.java_conf.snake0394.loglook_android

import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.SlotItemUtility.getImprovementLOS
import jp.gr.java_conf.snake0394.loglook_android.bean.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by snake0394 on 2016/08/29.
 */
object DeckUtility {
    /**
     * @param deck
     * *
     * @return 艦隊の制空値
     */
    fun getSeiku(deck: Deck): Int {
        val shipId = deck.shipId
        var seiku = 0
        Realm.getDefaultInstance().use {
            for (i in shipId.indices) {
                //空きの場合
                if (shipId[i] == -1) {
                    break
                }

                val myShip = it.where(MyShip::class.java).equalTo("id", shipId[i]).findFirst()

                //有効なスロットの装備を一つずつ見る
                for (j in 0..myShip.slotnum - 1) {
                    val slotitemId = myShip.slot[j].value
                    //所有装備に無いIDの場合は繰り返しを終える
                    if (slotitemId <= 0) {
                        break
                    }
                    val mySlotItem = it.where(MySlotItem::class.java).equalTo("id", slotitemId).findFirst()
                    val mstSlotitem = it.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()

                    seiku += SlotItemUtility.getFighterPower(mstSlotitem, myShip.onslot[j].value, mySlotItem.level, mySlotItem.alv)
                }
            }
        }
        return seiku
    }

    /**
     * @param deck
     * *
     * @return 艦隊の触接開始率
     */
    fun getTouchStartRate(deck: Deck): Int {
        val shipId = deck.shipId
        var touchStartRate = 0f
        Realm.getDefaultInstance().use {
            for (i in shipId.indices) {
                //空きの場合
                if (shipId[i] == -1) {
                    break
                }
                val myShip = it.where(MyShip::class.java).equalTo("id", shipId[i]).findFirst()
                var rate = 0f
                for (j in 0..myShip.slotnum - 1) {
                    val slotitemId = myShip.slot[j].value
                    if (slotitemId <= 0) {
                        break
                    }
                    val mySlotItem = it.where(MySlotItem::class.java).equalTo("id", slotitemId).findFirst()
                    val mstSlotitem = it.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()
                    val et = EquipType2.toEquipType2(mstSlotitem.type[2].value)
                    when (et) {
                        EquipType2.水上偵察機, EquipType2.艦上偵察機, EquipType2.大型飛行艇 -> rate += (0.04 * mstSlotitem.saku.toDouble() * Math.sqrt(myShip.onslot[j].value.toDouble())).toFloat()
                    }
                }
                touchStartRate += rate
            }
        }
        return (touchStartRate * 100).toInt()
    }


    /**
     * @param deck
     * *
     * @return 艦隊の判定式(33)の索敵値
     */
    fun getSakuteki33(deck: Deck, junctionCoefficient: Float): Float {
        var sakuteki = 0f
        var adjustedEquipsearch = 0f
        //艦隊の空き数
        var empty = 0
        Realm.getDefaultInstance().use {
            for (i in 0..deck.shipId.size - 1) {
                val shipId = deck.shipId[i]
                val myShip = it.where(MyShip::class.java).equalTo("id", shipId).findFirst()
                if (myShip == null || shipId == -1) {
                    empty = deck.shipId.size - i
                    break
                }
                var equipSakuSum = 0
                for (j in 0..myShip.slotnum - 1) {
                    val mySlotItemId = myShip.slot[j].value
                    if (mySlotItemId <= 0) {
                        break
                    }

                    val mySlotItem = it.where(MySlotItem::class.java).equalTo("id", mySlotItemId).findFirst()
                    val mstSlotitem = it.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()

                    equipSakuSum += mstSlotitem.saku

                    val et = EquipType2.toEquipType2(mstSlotitem.type[2].value)
                    val basicSlotLOS = mstSlotitem.saku + getImprovementLOS(mstSlotitem, mySlotItem.level)
                    //係数を考慮した装備の索敵値を足す
                    when (et) {
                        EquipType2.艦上戦闘機, EquipType2.艦上爆撃機, EquipType2.対潜哨戒機, EquipType2.探照灯, EquipType2.司令部施設, EquipType2.航空要員, EquipType2.水上艦要員, EquipType2.大型ソナー, EquipType2.大型飛行艇, EquipType2.大型探照灯, EquipType2.水上戦闘機, EquipType2.噴式戦闘爆撃機, EquipType2.小型電探, EquipType2.大型電探 -> adjustedEquipsearch += (basicSlotLOS * 0.6).toFloat()
                        EquipType2.艦上攻撃機 -> adjustedEquipsearch += (basicSlotLOS * 0.8).toFloat()
                        EquipType2.艦上偵察機, EquipType2.`艦上偵察機Ⅱ` -> adjustedEquipsearch += basicSlotLOS
                        EquipType2.水上偵察機 -> adjustedEquipsearch += (basicSlotLOS * 1.2).toFloat()
                        EquipType2.水上爆撃機 -> adjustedEquipsearch += (basicSlotLOS * 1.1).toFloat()
                    }
                }
                //艦船の素敵値を足す
                sakuteki += Math.sqrt((myShip.sakuteki[0].value - equipSakuSum).toDouble()).toFloat()
            }
        }
        adjustedEquipsearch *= junctionCoefficient
        sakuteki += adjustedEquipsearch
        //索敵 = -(司令部レベル*0.4)小数点以下を切り上げ + 2*艦隊の空き数
        sakuteki += (-Math.ceil(0.4 * Basic.INSTANCE.level) + 2 * empty).toFloat()
        var bd = BigDecimal(java.lang.Float.toString(sakuteki))
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
        return bd.toFloat()
    }

    /**
     * @param deck
     * *
     * @return 2-5式(秋)の索敵値
     */
    fun getSakuteki25(deck: Deck): Float {
        var sakuteki = 0.0

        Realm.getDefaultInstance().use {
            for (i in 0..deck.shipId.size - 1) {
                val shipId = deck.shipId[i]
                val myShip = it.where(MyShip::class.java).equalTo("id", shipId).findFirst()
                if (myShip == null || shipId == -1) {
                    break
                }
                var equipSakuSum = 0
                for (j in 0..myShip.slotnum - 1) {
                    val mySlotItemId = myShip.slot[j].value
                    if (mySlotItemId <= 0) {
                        break
                    }
                    val mySlotItem = it.where(MySlotItem::class.java).equalTo("id", mySlotItemId).findFirst()
                    val mstSlotitem = it.where(MstSlotitem::class.java).equalTo("id", mySlotItem.mstId).findFirst()
                    equipSakuSum += mstSlotitem.saku
                    val et = EquipType2.toEquipType2(mstSlotitem.type[2].value)
                    //改修と係数を考慮した装備の索敵値を足す
                    when (et) {
                        EquipType2.艦上爆撃機 -> sakuteki += mstSlotitem.saku * 1.0376255
                        EquipType2.艦上攻撃機 -> sakuteki += mstSlotitem.saku * 1.3677954
                        EquipType2.艦上偵察機 -> sakuteki += mstSlotitem.saku * 1.6592780
                        EquipType2.水上偵察機 -> sakuteki += mstSlotitem.saku * 2.0000000
                        EquipType2.水上爆撃機 -> sakuteki += mstSlotitem.saku * 1.7787282
                        EquipType2.小型電探 -> sakuteki += mstSlotitem.saku * 1.0045358
                        EquipType2.大型電探 -> sakuteki += mstSlotitem.saku * 0.9906638
                        EquipType2.探照灯 -> sakuteki += mstSlotitem.saku * 0.9067950
                    }
                }
                //艦船の素敵値を足す
                sakuteki += Math.sqrt((myShip.sakuteki[0].value - equipSakuSum).toDouble()) * 1.6841056
            }
        }
        var sirei = Basic.INSTANCE.level.toDouble()
        if (sirei % 5 != 0.0) {
            sirei = (Math.floor((Basic.INSTANCE.level / 5).toDouble()) + 1) * 5
        }
        sakuteki += sirei * -0.6142467
        var bd = BigDecimal(java.lang.Double.toString(sakuteki))
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP)
        return bd.toFloat()
    }

    /**
     * @param deck
     * *
     * @return 艦隊全員のcondが自然回復上限に達する時刻。HH:mmの形式。
     */
    fun getCondRecoveryTime(deck: Deck): String {
        val shipId = deck.shipId
        var leastCond = 49
        Realm.getDefaultInstance().use {
            for (i in 1..shipId.size) {
                //空きの場合
                if (shipId[i - 1] == -1) {
                    continue
                }
                val myShip = it.where(MyShip::class.java).equalTo("id", shipId[i - 1]).findFirst()
                if (leastCond > myShip.cond) {
                    leastCond = myShip.cond
                }
            }
        }
        if (leastCond - 49 < 0) {
            leastCond = 49 - leastCond
            var t = leastCond.toFloat()
            t /= 3f
            t = Math.ceil(t.toDouble()).toFloat()
            val sdf = SimpleDateFormat("HH:mm")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = calendar.timeInMillis + TimeUnit.MINUTES.toMillis((t * 3).toLong())
            return sdf.format(calendar.time)
        }
        return ""
    }

    /**
     * @param deck
     * *
     * @return 艦隊に所属する艦のレベル合計
     */
    fun getLevelSum(deck: Deck): Int {
        var sum = 0
        Realm.getDefaultInstance().use { realm ->
            deck.shipId
                    .filter { it != -1 }
                    .map { realm.where(MyShip::class.java).equalTo("id", it).findFirst() }
                    .forEach { sum += it.lv }
        }

        return sum
    }

    /**
     * @param deck
     * *
     * @param formation
     * *
     * @return 艦隊防空値
     */
    fun getAdjustedFleetAA(deck: Deck?, formation: String): Float {
        if (deck == null) {
            return 0f
        }

        //艦隊防空値
        var fleetAA = 0

        Realm.getDefaultInstance().use { realm ->
            deck.shipId
                    .filter { it != -1 }
                    .map { realm.where(MyShip::class.java).equalTo("id", it).findFirst() }
                    .forEach { fleetAA += ShipUtility.getAdjustedFleetAA(it).toInt() }
        }

        //陣形補正
        var formationModifier = 1f
        try {
            when (formation) {
                "単縦陣/梯形陣/単横陣", "単縦陣", "梯形陣", "単横陣" -> formationModifier = 1f
                "複縦陣" -> formationModifier = 1.2f
                "輪形陣" -> formationModifier = 1.6f
            }
        } catch (e: NullPointerException) {
            //何もしない
            e.printStackTrace()
        }

        return (Math.floor((formationModifier * fleetAA).toDouble()) * 2 / 1.3).toFloat()
    }
}
