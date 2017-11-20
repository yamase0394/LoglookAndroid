package jp.gr.java_conf.snake0394.loglook_android

import android.content.Intent
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.*
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs
import java.math.BigDecimal

/**
 * Created by snake0394 on 2017/07/09.
 */
object TacticalSituation {
    private val TAG = "TacticalSituation"
    lateinit var phaseList: MutableList<PhaseState>
    lateinit var battle: IBattle
    var midnightBattle: IMidnightBattle? = null
    lateinit var winRank: String
    var isBoss = false

    fun applyBattle(battle: IBattle) {
        phaseList = mutableListOf<PhaseState>()
        TacticalSituation.battle = battle

        val fHp = battle.apiFNowhps.toMutableList()
        val eHp = battle.apiENowhps.toMutableList()
        var fHpCombined: MutableList<Int>? =
                when (battle) {
                    is ICombinedBattle -> battle.apiFNowhpsCombined.toMutableList()
                    is IEachCombinedBattle -> battle.apiFNowhpsCombined.toMutableList()
                    else -> null
                }
        var eHpCombined: MutableList<Int>? =
                when (battle) {
                    is IEnemyCombinedBattle -> battle.apiENowhpsCombined.toMutableList()
                    is IEachCombinedBattle -> battle.apiENowhpsCombined.toMutableList()
                    else -> null
                }
        var phase = PhaseState(PhaseState.Type.BEFORE, fHp, fHpCombined, eHp, eHpCombined)
        phaseList.add(phase)

        if (battle is INSupport) {
            BattleCalculator.applyNSupport(battle, phase.deepCopy(PhaseState.Type.SUPPORT))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is INHougeki){
            BattleCalculator.applyNHougeki(battle, phase.deepCopy(PhaseState.Type.MIDNIGHT)).let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IMidnightBattle) {
            BattleCalculator.applyHougekiMidnight(battle, phase.deepCopy(PhaseState.Type.MIDNIGHT)).let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IAirBaseAttack) {
            BattleCalculator.applyAirBaseInjection(battle, phase.deepCopy(PhaseState.Type.BASE_INJECTION))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IInjectionKouku) {
            BattleCalculator.applyInjectionKouku(battle, phase.deepCopy(PhaseState.Type.INJECTION))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IAirBaseAttack) {
            BattleCalculator.applyAirBaseAttack(battle, phase.deepCopy(PhaseState.Type.BASE))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IKouku) {
            BattleCalculator.applyKouku(battle.apiStageFlag, battle.apiKouku, phase.deepCopy(PhaseState.Type.KOUKU))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is ISupport) {
            BattleCalculator.applySupport(battle, phase.deepCopy(PhaseState.Type.SUPPORT))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IOpeningTaisen) {
            BattleCalculator.applyOpeningTaisen(battle, phase.deepCopy(PhaseState.Type.OPENING_ANTI_SUBMARINE))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IOpeningAttack) {
            BattleCalculator.applyOpeningAttack(battle, phase.deepCopy(PhaseState.Type.OPENING_TORPEDO))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        if (battle is IHourai) {
            BattleCalculator.applyHourai(battle, phase.deepCopy(PhaseState.Type.SHELLING))?.let {
                phase = it
                phaseList.add(it)
            }
        }

        if (battle is IKouku2) {
            BattleCalculator.applyKouku(battle.apiStageFlag2, battle.apiKouku2, phase.deepCopy(PhaseState.Type.KOUKU2))?.let {
                phase = it
                phaseList.add(phase)
            }
        }

        calcWinRank()
        if (!GeneralPrefs(App.getInstance().applicationContext).showsWinRankOverlay) {
            return
        }
        App.getInstance().stopService(Intent(App.getInstance().applicationContext, HeavilyDamagedWarningService::class.java))
        App.getInstance().startService(Intent(App.getInstance().applicationContext, WinRankOverlayService::class.java))
    }

    fun applyMidnightBattle(battle: IMidnightBattle) {
        midnightBattle = battle
        phaseList.add(BattleCalculator.applyHougekiMidnight(battle, phaseList.last().deepCopy(PhaseState.Type.MIDNIGHT)))

        calcWinRank()
        if (!GeneralPrefs(App.getInstance().applicationContext).showsWinRankOverlay) {
            return
        }
        App.getInstance().startService(Intent(App.getInstance().applicationContext, WinRankOverlayService::class.java))
    }

    private fun calcWinRank() {
        val friendHpBeforeSum = (phaseList.first().fHp.sum() + (phaseList.first().fHpCombined?.sum() ?: 0)).toFloat()
        val friendHpAfterSum = (phaseList.last().fHp.sum() + (phaseList.last().fHpCombined?.sum() ?: 0)).toFloat()
        val friendDamageSum = friendHpBeforeSum - friendHpAfterSum
        val friendSink = phaseList.last().fHp.filter { it <= 0 }.size + (phaseList.last().fHpCombined?.filter { it <= 0 }?.size ?: 0)
        Logger.d(TAG, "friendHpBeforeSum=$friendHpBeforeSum")
        Logger.d(TAG, "friendHpAfterSum=$friendHpAfterSum")
        Logger.d(TAG, "friendDamageSum=$friendDamageSum")
        Logger.d(TAG, "friendSink=$friendSink")

        if (battle is CombinedBattleLdAirbattle || battle is SortieLdAirbattle) {
            val damageRatio = friendDamageSum / (friendHpBeforeSum) * 100
            Logger.d(TAG, "damagetRatio=$damageRatio")
            winRank = when {
                damageRatio == 0f -> "完全勝利S"
                damageRatio <= 10 -> "A勝利"
                damageRatio <= 20 -> "B勝利"
                damageRatio <= 30 -> "C敗北"
                damageRatio <= 40 -> "D敗北"
                else -> "E敗北"
            }
        } else {
            val friendNum = phaseList.first().fHp.size + (phaseList.first().fHpCombined?.size ?: 0)
            val enemyNum = phaseList.first().eHp.size + (phaseList.first().eHpCombined?.size ?: 0)

            val enemySink = phaseList.last().eHp.filter { it <= 0 }.size + (phaseList.last().eHpCombined?.filter { it <= 0 }?.size ?: 0)

            val enemyHpBeforeSum = (phaseList.first().eHp.sum() + (phaseList.first().eHpCombined?.sum() ?: 0)).toFloat()
            val enemyHpAfterSum = (phaseList.last().eHp.sum() + (phaseList.last().eHpCombined?.sum() ?: 0)).toFloat()
            val friendAchievements = (enemyHpBeforeSum - enemyHpAfterSum) * 100f / enemyHpBeforeSum

            val enemyAchievements = friendDamageSum * 100f / friendHpBeforeSum

            val achievementsRatio =
                    if (enemyAchievements > 0.0f) {
                        BigDecimal((friendAchievements / enemyAchievements).toString()).setScale(1, BigDecimal.ROUND_DOWN).toFloat()
                    } else if (enemyAchievements == 0.0f && friendAchievements == 0.0f) {
                        0f
                    } else {
                        //enemyAchievementsが0のため割れない
                        3f
                    }

            val isEnemyFlagShipSank = phaseList.last().eHp[0] <= 0

            Logger.d(TAG, "enemyHpBeforeSum=$enemyHpBeforeSum")
            Logger.d(TAG, "enemyHpAfterSum=$enemyHpAfterSum")
            Logger.d(TAG, "enemySink=$enemySink")

            Logger.d(TAG, "friendAchievements=$friendAchievements")
            Logger.d(TAG, "enemyAchievements=$enemyAchievements")
            Logger.d(TAG, "achievementsRatio=$achievementsRatio")

            /*
             * 轟沈艦ありと書いていない場合は原則轟沈なし状態となります。
             * なお轟沈艦ありの場合最高でもB勝利止まりになります。
             * 一方轟沈なしでは現状はD敗北が最低となります。
             */
            winRank = when {
                //自艦隊の被ダメージ0、且つ敵艦隊全隻撃沈。もしくは敵戦果ゲージ0%かつ敵艦隊全隻撃沈。
                //後者は女神等で大破から完全回復した時等に発生する。
                friendSink == 0 && enemyNum == enemySink && (friendDamageSum == 0f || enemyAchievements == 0f) -> "完全勝利S"
                friendSink == 0 && enemyNum == enemySink -> "S勝利"
                friendSink == 0 && enemySink != 0 && enemySink >= Math.floor(enemyNum.toDouble() * 2 / 3) -> "A勝利"
                friendSink == 0 && isEnemyFlagShipSank -> "B勝利"
                friendSink == 0 && friendAchievements < 0.05 -> "D敗北"
                friendSink == 0 && !isEnemyFlagShipSank && achievementsRatio > 2.5 -> "B勝利"
                friendSink > 0 && !isEnemyFlagShipSank && achievementsRatio >= 2.5 -> "B勝利"
                friendSink > 0 && isEnemyFlagShipSank && friendSink < enemySink -> "B勝利"
                friendSink == 0 && !isEnemyFlagShipSank && 1 <= achievementsRatio && achievementsRatio < 2.5 -> "C敗北"
                friendSink == 0 && !isEnemyFlagShipSank && friendAchievements >= 50 && achievementsRatio < 2.5 -> "C敗北"
                friendSink > 0 && !isEnemyFlagShipSank && 1 <= achievementsRatio && achievementsRatio < 2.5 -> "C敗北"
                friendSink > 0 && isEnemyFlagShipSank && friendSink >= enemySink -> "C敗北"
                friendSink == 0 && !isEnemyFlagShipSank && friendAchievements < 50 && friendAchievements < enemyAchievements -> "D敗北"
                !isEnemyFlagShipSank && friendSink >= Math.floor(friendNum.toDouble() * 2 / 3) -> "E敗北"
                friendSink > 0 && !isEnemyFlagShipSank && friendAchievements < enemyAchievements -> "D敗北"
                else -> "不明"
            }
        }
    }
}