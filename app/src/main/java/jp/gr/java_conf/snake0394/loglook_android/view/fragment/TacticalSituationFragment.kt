package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.*
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.*

class TacticalSituationFragment : Fragment() {

    val realm by lazy { Realm.getDefaultInstance() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_tactical_situation, container, false)
    }

    override fun onStart() {
        super.onStart()

        var manager = childFragmentManager
        var fragment = manager.findFragmentById(R.id.fragment) as ErrorFragment
        var transaction: android.support.v4.app.FragmentTransaction = manager.beginTransaction()
        transaction.hide(fragment)
        transaction.commit()

        try {
            val battle = TacticalSituation.battle

            val formation = TacticalSituation.battle as IFormation
            var textView = activity.findViewById(R.id.tactic) as TextView
            textView.text = BattleUtility.getTactic(formation.apiFormation[2])

            textView = activity.findViewById(R.id.formation) as TextView
            textView.text = BattleUtility.getFormation(formation.apiFormation[0])

            textView = activity.findViewById(R.id.eFormation) as TextView
            textView.text = BattleUtility.getFormation(formation.apiFormation[1])

            val mainDeck = DeckManager.INSTANCE.getDeck(battle.apiDeckId)
            when (battle) {
                is IKouku -> {
                    textView = activity.findViewById(R.id.seiku) as TextView
                    textView.text = "${DeckUtility.getSeiku(mainDeck)}(${BattleUtility.getDispSeiku(battle.apiKouku.apiStage1!!.apiDispSeiku)})"

                    textView = activity.findViewById(R.id.touchPlane) as TextView
                    val touchPlane = realm.where(MstSlotitem::class.java).equalTo("id", battle.apiKouku.apiStage1!!.apiTouchPlane[0]).findFirst()?.name ?: "なし"
                    textView.text = touchPlane

                    textView = activity.findViewById(R.id.enemyTouchPlane) as TextView
                    val enemyTouchPlane = realm.where(MstSlotitem::class.java).equalTo("id", battle.apiKouku.apiStage1!!.apiTouchPlane[1]).findFirst()?.name ?: "なし"
                    textView.text = enemyTouchPlane
                }
                is IMidnightBattle -> {
                    textView = activity.findViewById(R.id.seiku) as TextView
                    textView.text = "${DeckUtility.getSeiku(mainDeck)}()"

                    textView = activity.findViewById(R.id.touchPlane) as TextView
                    val touchPlane = realm.where(MstSlotitem::class.java).equalTo("id", battle.apiTouchPlane[0]).findFirst()?.name ?: "なし"
                    textView.text = touchPlane

                    textView = activity.findViewById(R.id.enemyTouchPlane) as TextView
                    val enemyTouchPlane = realm.where(MstSlotitem::class.java).equalTo("id", battle.apiTouchPlane[1]).findFirst()?.name ?: "なし"
                    textView.text = enemyTouchPlane
                }
            }

            val phaseList = TacticalSituation.phaseList
            for (i in 1..7) {
                //本隊
                if (i > phaseList[0].fHp.size) {
                    var name = "name" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "lv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "state" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "hp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "beforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "damage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""
                } else {
                    val id = mainDeck.shipId[i - 1]
                    val myShip = realm.where(MyShip::class.java).equalTo("id", id).findFirst()

                    var name = "name" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = realm.where(MstShip::class.java).equalTo("id", myShip.shipId).findFirst().name

                    name = "lv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = "(Lv${myShip.lv})"

                    name = "state" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        val lastHp = phaseList.last().fHp[i - 1]
                        setStateText(this, lastHp, myShip.maxhp, false, Escape.INSTANCE.isEscaped(myShip.id))
                    }

                    name = "beforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        text = "${TacticalSituation.phaseList.first().fHp[i - 1]}/${myShip.maxhp}→"
                    }

                    name = "hp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        text = "${TacticalSituation.phaseList.last().fHp[i - 1]}/${myShip.maxhp}"
                    }

                    name = "damage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        val damage = TacticalSituation.phaseList.last().fHp[i - 1] - TacticalSituation.phaseList.first().fHp[i - 1]
                        text = damage.toString()
                        if (damage == 0) {
                            setTextColor(ContextCompat.getColor(context, R.color.undamaged))
                        } else {
                            setTextColor(ContextCompat.getColor(context, R.color.sank))
                        }
                    }
                }

                if (i == 7) {
                    break
                }

                //敵艦隊
                if (i > phaseList.first().eHp.size) {
                    var name = "eName" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "eLv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "eState" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "eBeforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "eHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""

                    name = "eDamage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = ""
                } else {
                    val id = battle.apiShipKe[i - 1]
                    val eShip = realm.where(MstShip::class.java).equalTo("id", id).findFirst()

                    var name = "eName" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = eShip.name

                    name = "eLv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        text = if (battle is PracticeBattle) {
                            "(Lv${battle.apiShipLv[i - 1]})"
                        } else {
                            "${eShip.yomi}(Lv${TacticalSituation.battle.apiShipLv[i - 1]})"
                        }
                    }

                    var lastHp = phaseList.last().eHp[i - 1]
                    var eMaxhp = battle.apiEMaxhps[i - 1]
                    name = "eState" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        setStateText(this, lastHp, eMaxhp, true)
                    }

                    name = "eBeforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = "${phaseList.first().eHp[i - 1]}/$eMaxhp→"

                    name = "eHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.text = "${lastHp}/$eMaxhp"

                    name = "eDamage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        val damage = lastHp - phaseList.first().eHp[i - 1]
                        text = damage.toString()
                        if (damage == 0) {
                            setTextColor(ContextCompat.getColor(context, R.color.undamaged))
                        } else {
                            setTextColor(ContextCompat.getColor(context, R.color.sank))
                        }
                    }
                }

                //第2艦隊
                val deck2 = DeckManager.INSTANCE.getDeck(2)
                if (i > phaseList.first().fHpCombined?.size ?: 0) {
                    var name = "cName" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "cLv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "cState" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "cBeforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "cHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "cDamage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE
                } else {

                    val id = deck2.shipId[i - 1]
                    val myShip = realm.where(MyShip::class.java).equalTo("id", id).findFirst()

                    var name = "cName" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        text = realm.where(MstShip::class.java).equalTo("id", myShip.shipId).findFirst().name
                    }

                    name = "cLv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        text = "(Lv${myShip.lv})"
                    }

                    name = "cState" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    val lastHp = phaseList.last().fHpCombined!![i - 1]
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        setStateText(this, lastHp, myShip.maxhp, false, Escape.INSTANCE.isEscaped(myShip.id))
                    }

                    name = "cBeforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        text = "${phaseList.first().fHpCombined!![i - 1]}/${myShip.maxhp}→"
                    }

                    name = "cHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        text = "$lastHp/${myShip.maxhp}"
                    }

                    name = "cDamage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        val damage = lastHp - phaseList.first().fHpCombined!![i - 1]
                        visibility = View.VISIBLE
                        text = damage.toString()
                        if (damage == 0) {
                            setTextColor(ContextCompat.getColor(context, R.color.undamaged))
                        } else {
                            setTextColor(ContextCompat.getColor(context, R.color.sank))
                        }
                    }
                }

                //敵第2艦隊
                if (i > phaseList.first().eHpCombined?.size ?: 0) {
                    var name = "ecName" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "ecLv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "ecState" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "ecBeforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "ecHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE

                    name = "ecDamage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    textView = activity.findViewById(strId) as TextView
                    textView.visibility = View.GONE
                } else {

                    val id = when (battle) {
                        is IEnemyCombinedBattle -> battle.apiShipKeCombined[i - 1]
                        is IEachCombinedBattle -> battle.apiShipKeCombined[i - 1]
                        else -> throw IllegalArgumentException("${battle.javaClass.name}")
                    }
                    val eShip = realm.where(MstShip::class.java).equalTo("id", id).findFirst()

                    var name = "ecName" + i
                    var strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        text = eShip.name
                    }

                    name = "ecLv" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        val lv = when (battle) {
                            is IEnemyCombinedBattle -> battle.apiShipLvCombined[i - 1]
                            is IEachCombinedBattle -> battle.apiShipLvCombined[i - 1]
                            else -> throw IllegalArgumentException("${battle.javaClass.name}")
                        }
                        visibility = View.VISIBLE
                        text = "${eShip.yomi}(Lv$lv)"
                    }

                    val lastHp = phaseList.last().eHpCombined!![i - 1]
                    val maxHp = when (battle) {
                        is IEnemyCombinedBattle -> battle.apiEMaxhpsCombined[i - 1]
                        is IEachCombinedBattle -> battle.apiEMaxhpsCombined[i - 1]
                        else -> throw IllegalArgumentException("${battle.javaClass.name}")
                    }
                    name = "ecState" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        setStateText(this, lastHp, maxHp, true)
                    }

                    name = "ecBeforeHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        text = "${phaseList.first().eHpCombined!![i - 1]}/$maxHp→"
                    }

                    name = "ecHp" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        visibility = View.VISIBLE
                        text = "$lastHp/$maxHp"
                    }

                    name = "ecDamage" + i
                    strId = resources.getIdentifier(name, "id", activity.packageName)
                    (activity.findViewById(strId) as TextView).apply {
                        val damage = lastHp - phaseList.first().eHpCombined!![i - 1]
                        text = damage.toString()
                        visibility = View.VISIBLE
                        if (damage == 0) {
                            setTextColor(ContextCompat.getColor(context, R.color.undamaged))
                        } else {
                            setTextColor(ContextCompat.getColor(context, R.color.sank))
                        }
                    }
                }
            }

            (activity.findViewById(R.id.rank) as TextView).apply {
                text = TacticalSituation.winRank
            }
        } catch (e: Exception) {
            e.printStackTrace()
            manager = childFragmentManager
            fragment = manager.findFragmentById(R.id.fragment) as ErrorFragment
            transaction = manager.beginTransaction()
            transaction.show(fragment)
            transaction.commit()
        }
    }

    private fun setStateText(textView: TextView, lastHp: Int, maxHp: Int, isEnemy: Boolean, isEscaped: Boolean = false) {
        textView.run {
            when {
                isEscaped -> {
                    text = "退避"
                    setTextColor(ContextCompat.getColor(context, R.color.escort))
                }
                lastHp <= 0 -> {
                    text = if (isEnemy) "撃沈" else "轟沈"
                    setTextColor(ContextCompat.getColor(context, R.color.sank))
                }
                lastHp <= maxHp / 4 -> {
                    text = "大破"
                    setTextColor(ContextCompat.getColor(context, R.color.heavy_damage))
                }
                lastHp <= maxHp / 2 -> {
                    text = "中破"
                    setTextColor(ContextCompat.getColor(context, R.color.moderate_damage))
                }
                lastHp <= maxHp * 3 / 4 -> {
                    text = "小破"
                    setTextColor(ContextCompat.getColor(context, R.color.minor_damage))
                }
                lastHp < maxHp -> {
                    text = "健在"
                    setTextColor(ContextCompat.getColor(context, R.color.good_health))
                }
                else -> {
                    text = "無傷"
                    setTextColor(ContextCompat.getColor(context, R.color.undamaged))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        realm.close()
    }

    companion object {

        @JvmStatic
        fun newInstance(): TacticalSituationFragment {
            val fragment = TacticalSituationFragment()
            return fragment
        }
    }
}
