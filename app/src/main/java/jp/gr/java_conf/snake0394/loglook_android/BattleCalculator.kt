package jp.gr.java_conf.snake0394.loglook_android

import jp.gr.java_conf.snake0394.loglook_android.bean.battle.*
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger


object BattleCalculator {

    fun applyAirBaseInjection(battle: IAirBaseAttack, phase: PhaseState): PhaseState? {

        battle.apiAirBaseInjection?.apiStage3?.run {
            apiEdam.withIndex().forEach {
                phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
            }
        } ?: return null

        battle.apiAirBaseInjection?.apiStage3Combined?.apiEdam?.run {
            withIndex().forEach {
                phase.eHpCombined!![it.index] = Math.max(phase.eHpCombined[it.index] - it.value, 0)
            }
        }

        return phase
    }

    fun applyInjectionKouku(battle: IInjectionKouku, phase: PhaseState): PhaseState? {

        battle.apiInjectionKouku?.apiStage3?.run {
            apiEdam.withIndex().forEach {
                phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
            }
        } ?: return null

        battle.apiInjectionKouku?.apiStage3Combined?.apiEdam?.run {
            withIndex().forEach {
                phase.eHpCombined!![it.index] = Math.max(phase.eHpCombined[it.index] - it.value, 0)
            }
        }

        return phase
    }

    fun applyAirBaseAttack(battle: IAirBaseAttack, phase: PhaseState): PhaseState? {

        battle.apiAirBaseAttack?.forEach { apiAirBaseAttack ->

            apiAirBaseAttack.apiStage3?.run {
                apiEdam.withIndex().forEach {
                    phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
                }
            } ?: return null

            apiAirBaseAttack.apiStage3Combined?.apiEdam?.run {
                withIndex().forEach {
                    phase.eHpCombined!![it.index] = Math.max(phase.eHpCombined[it.index] - it.value, 0)
                }
            }
        } ?: return null

        return phase
    }

    fun applyKouku(stageFlag: List<Int>, apiKouku: ApiKouku, phase: PhaseState): PhaseState? {
        if (stageFlag.all { it == 0 }) {
            return null
        }

        apiKouku.apiStage3?.run {
            //TODO:subList or slice ?
            apiFdam.withIndex().forEach {
                phase.fHp[it.index] = Math.max(phase.fHp[it.index] - it.value, 0)
            }

            apiEdam.withIndex().forEach {
                phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
            }
        } ?: return null

        apiKouku.apiStage3Combined?.run {
            if (phase.fHpCombined != null) {
                apiFdam.withIndex().forEach {
                    phase.fHpCombined[it.index] = Math.max(phase.fHpCombined[it.index] - it.value, 0)
                }
            }

            apiEdam?.run {
                withIndex().forEach {
                    phase.eHpCombined!![it.index] = Math.max(phase.eHpCombined[it.index] - it.value, 0)
                }
            }
        }

        return phase
    }

    fun applySupport(battle: ISupport, phase: PhaseState): PhaseState? {
        when (battle.apiSupportFlag) {
            0 -> return null
            1 -> {
                battle.apiSupportInfo.apiSupportAiratack.apiStage3.apiEdam.withIndex().forEach {
                    if (it.index < phase.eHp.size) {
                        phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
                    } else if (phase.eHpCombined != null && it.index < phase.eHpCombined.size + 6) {
                        phase.eHpCombined[it.index - 6] = Math.max(phase.eHpCombined[it.index - 6] - it.value, 0)
                    } else {
                        return@forEach
                    }
                }
            }
            2, 3 -> {
                battle.apiSupportInfo.apiSupportHourai.apiDamage.withIndex().forEach {
                    if (it.index < phase.eHp.size) {
                        phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
                    } else if (phase.eHpCombined != null && it.index < phase.eHpCombined.size + 6) {
                        phase.eHpCombined[it.index - 6] = Math.max(phase.eHpCombined[it.index - 6] - it.value, 0)
                    } else {
                        return@forEach
                    }
                }
            }
        }
        return phase
    }

    fun applyNSupport(battle: INSupport, phase: PhaseState): PhaseState? {
        when (battle.apiNSupportFlag) {
            0 -> return null
            1 -> {
                battle.apiNSupportInfo.apiSupportAiratack.apiStage3.apiEdam.withIndex().forEach {
                    if (it.index < phase.eHp.size) {
                        phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
                    } else if (phase.eHpCombined != null && it.index < phase.eHpCombined.size + 6) {
                        phase.eHpCombined[it.index - 6] = Math.max(phase.eHpCombined[it.index - 6] - it.value, 0)
                    } else {
                        return@forEach
                    }
                }
            }
            2 -> {
                battle.apiNSupportInfo.apiSupportHourai.apiDamage.withIndex().forEach {
                    if (it.index < phase.eHp.size) {
                        phase.eHp[it.index] = Math.max(phase.eHp[it.index] - it.value, 0)
                    } else if (phase.eHpCombined != null && it.index < phase.eHpCombined.size + 6) {
                        phase.eHpCombined[it.index - 6] = Math.max(phase.eHpCombined[it.index - 6] - it.value, 0)
                    } else {
                        return@forEach
                    }
                }
            }
        }

        return phase
    }

    fun applyOpeningTaisen(battle: IOpeningTaisen, phase: PhaseState): PhaseState? {

        if (battle.apiOpeningTaisenFlag == 0) {
            return null
        }

        battle.apiOpeningTaisen!!.run {
            //連合VS連合のときapi_at_eflagがある？
            if (apiAtEflag != null) {
                Logger.d("applyOpeningTaisen", "has apiAtEflag")
                apiDfList.map { it[0] }.withIndex().forEach {
                    val damage = apiDamage[it.index].sum()
                    when (apiAtEflag[it.index]) {
                        0 -> {
                            if (it.value < 6) {
                                phase.eHp[it.value] = Math.max(phase.eHp[it.value] - damage, 0)
                            } else {
                                phase.eHpCombined!![it.value - 6] = Math.max(phase.eHpCombined[it.value - 6] - damage, 0)
                            }
                        }
                        1 -> {
                            if (it.value < 6) {
                                phase.fHp[it.value] = Math.max(phase.fHp[it.value] - damage, 0)
                            } else {
                                phase.fHpCombined!![it.value - 6] = Math.max(phase.fHpCombined[it.value - 6] - damage, 0)
                            }
                        }
                        else -> throw IllegalArgumentException()
                    }
                }
            } else {
                //TODO("もう必要ないかも")
                Logger.d("applyOpeningTaisen", "does not has apiAtEflag")
                apiDfList.map { it[0] }.withIndex().forEach {
                    val damage = apiDamage[it.index].sum()
                    if (it.value < 6) {
                        //機動、輸送の場合の敵の攻撃対象は第2艦隊
                        if (battle is CombinedBattleBattle) {
                            phase.fHpCombined!![it.value] = Math.max(phase.fHpCombined[it.value] - damage, 0)
                        } else {
                            phase.fHp[it.value] = Math.max(phase.fHp[it.value] - damage, 0)
                        }
                    } else {
                        phase.eHp[it.value - 6] = Math.max(phase.eHp[it.value - 6] - damage, 0)
                    }
                }
            }
        }

        return phase
    }

    fun applyOpeningAttack(battle: IOpeningAttack, phase: PhaseState): PhaseState? {

        if (battle.apiOpeningFlag == 0) {
            return null
        }

        when (battle) {
            is ICombinedBattle -> applyRaigeki(battle.apiOpeningAtack!!, phase.fHpCombined!!, phase.eHp)
            else -> applyRaigeki(battle.apiOpeningAtack!!, phase)
        }

        return phase
    }

    fun applyHourai(battle: IHourai, phase: PhaseState): PhaseState? {
        if (battle.apiHouraiFlag.all { it == 0 }) {
            return null
        }

        when (battle) {
            is SortieBattle, is PracticeBattle -> {
                if (battle.apiHouraiFlag[0] == 1) {
                    applyHougeki(battle.apiHougeki1, phase)
                }
                if (battle.apiHouraiFlag[1] == 1) {
                    applyHougeki(battle.apiHougeki2, phase)
                }
                if (battle.apiHouraiFlag[3] == 1) {
                    applyRaigeki(battle.apiRaigeki, phase)
                }
            }
            is CombinedBattleBattle -> {
                if (battle.apiHouraiFlag[0] == 1) {
                    applyHougeki(battle.apiHougeki1, phase)
                }
                if (battle.apiHouraiFlag[1] == 1) {
                    applyRaigeki(battle.apiRaigeki, phase)
                }
                if (battle.apiHouraiFlag[2] == 1) {
                    applyHougeki(battle.apiHougeki2, phase)
                }
                if (battle.apiHouraiFlag[3] == 1) {
                    applyHougeki(battle.apiHougeki3, phase)
                }
            }
            is CombinedBattleBattleWater -> {
                if (battle.apiHouraiFlag[0] == 1) {
                    applyHougeki(battle.apiHougeki1, phase)
                }
                if (battle.apiHouraiFlag[1] == 1) {
                    applyHougeki(battle.apiHougeki2, phase)
                }
                if (battle.apiHouraiFlag[2] == 1) {
                    applyHougeki(battle.apiHougeki3, phase)
                }
                if (battle.apiHouraiFlag[3] == 1) {
                    applyRaigeki(battle.apiRaigeki, phase.fHpCombined!!, phase.eHp)
                }
            }
            is CombinedBattleEcBattle -> {
                if (battle.apiHouraiFlag[0] == 1) {
                    //対随伴
                    applyHougeki(battle.apiHougeki1, phase)
                }
                if (battle.apiHouraiFlag[1] == 1) {
                    //雷撃対全体
                    applyRaigeki(battle.apiRaigeki, phase)
                }
                if (battle.apiHouraiFlag[2] == 1) {
                    //対本隊
                    applyHougeki(battle.apiHougeki2, phase)
                }
                if (battle.apiHouraiFlag[3] == 1) {
                    //対全体
                    applyHougeki(battle.apiHougeki3, phase)
                }
            }
            is CombinedBattleEachBattle -> {
                if (battle.apiHouraiFlag[0] == 1) {
                    //第1艦隊砲撃(対本隊)
                    applyHougeki(battle.apiHougeki1, phase)
                }
                if (battle.apiHouraiFlag[1] == 1) {
                    //第2艦隊砲撃(対随伴)
                    applyHougeki(battle.apiHougeki2, phase)
                }
                if (battle.apiHouraiFlag[2] == 1) {
                    //第2艦隊雷撃(対全体)
                    applyRaigeki(battle.apiRaigeki, phase)
                }
                if (battle.apiHouraiFlag[3] == 1) {
                    //第1艦隊砲撃(対全体)
                    applyHougeki(battle.apiHougeki3, phase)
                }
            }
            is CombinedBattleEachBattleWater -> {
                if (battle.apiHouraiFlag[0] == 1) {
                    //第1艦隊砲撃(対本隊)
                    applyHougeki(battle.apiHougeki1, phase)
                }
                if (battle.apiHouraiFlag[1] == 1) {
                    //第1艦隊砲撃(対全体)
                    applyHougeki(battle.apiHougeki2, phase)
                }
                if (battle.apiHouraiFlag[2] == 1) {
                    //第2艦隊砲撃(対随伴)
                    applyHougeki(battle.apiHougeki3, phase)
                }
                if (battle.apiHouraiFlag[3] == 1) {
                    //第2艦隊雷撃(対全体)
                    applyRaigeki(battle.apiRaigeki, phase)
                }
            }
        }

        return phase
    }

    private fun applyHougeki(apiHougeki: ApiHougeki, phase: PhaseState) {
        Logger.d("apiHougeki", apiHougeki.toString())
        apiHougeki.apiAtEflag!!.indices.forEach {
            val dfIdx = apiHougeki.apiDfList[it][0]
            val damage = apiHougeki.apiDamage[it].sum()
            when (apiHougeki.apiAtEflag[it]) {
                0 -> {
                    if (dfIdx < phase.eHp.size) {
                        phase.eHp[dfIdx] = Math.max(phase.eHp[dfIdx] - damage, 0)
                    } else if (phase.eHpCombined != null && dfIdx - 6 < phase.eHpCombined.size) {
                        phase.eHpCombined!![dfIdx - 6] = Math.max(phase.eHpCombined[dfIdx - 6] - damage, 0)
                    } else {
                        Logger.d("applyHougeki", "index=$it enemy dfIdx=$dfIdx")
                    }
                }
                1 -> {
                    if (dfIdx < phase.fHp.size) {
                        phase.fHp[dfIdx] = Math.max(phase.fHp[dfIdx] - damage, 0)
                    } else if (phase.fHpCombined != null && dfIdx - 6 < phase.fHpCombined.size) {
                        phase.fHpCombined!![dfIdx - 6] = Math.max(phase.fHpCombined[dfIdx - 6] - damage, 0)
                    } else {
                        Logger.d("applyHougeki", "index=$it friend dfIdx=$dfIdx")
                    }
                }
                else -> throw IllegalArgumentException("illegal value : apiAtEflag[${it}]=${apiHougeki.apiAtEflag[it]}")
            }
        }
    }

    private fun applyHougekiMidnight(apiHougeki: ApiHougeki, fHp: MutableList<Int>, eHp: MutableList<Int>) {
        if (apiHougeki.apiAtEflag == null) {
            return
        }

        apiHougeki.apiAtEflag.indices.forEach {
            val dfIdx = apiHougeki.apiDfList[it][0]
            val damage = apiHougeki.apiDamage[it].map { Math.max(it, 0) }.sum()
            when (apiHougeki.apiAtEflag[it]) {
                0 -> {
                    if (dfIdx < eHp.size) {
                        eHp[dfIdx] = Math.max(eHp[dfIdx] - damage, 0)
                    } else {
                        throw IndexOutOfBoundsException("dfIdx=$dfIdx")
                    }
                }
                1 -> {
                    if (dfIdx < fHp.size) {
                        fHp[dfIdx] = Math.max(fHp[dfIdx] - damage, 0)
                    } else {
                        throw IndexOutOfBoundsException("dfIdx=$dfIdx")
                    }
                }
                else -> throw IllegalArgumentException("illegal value : apiAtEflag[${it}]=${apiHougeki.apiAtEflag[it]}")
            }
        }
    }

    fun applyHougekiMidnight(battle: IMidnightBattle, phase: PhaseState): PhaseState {
        when (battle) {
            is IEnemyCombinedMidnightBattle -> {
                when (battle.apiActiveDeck[1]) {
                    1 -> applyHougekiMidnight(battle.apiHougeki, phase.fHpCombined ?: phase.fHp, phase.eHp)
                    2 -> applyHougekiMidnight(battle.apiHougeki, phase.fHpCombined ?: phase.fHp, phase.eHpCombined!!)
                    else -> throw IllegalArgumentException("illegal value : battle.apiActiveDeck[1]=${battle.apiActiveDeck[1]}")
                }
            }
            else -> applyHougekiMidnight(battle.apiHougeki, phase.fHpCombined ?: phase.fHp, phase.eHp)
        }

        return phase
    }

    private fun applyRaigeki(apiRaigeki: ApiRaigeki, fHp: MutableList<Int>, eHp: MutableList<Int>) {
        apiRaigeki.apiFdam.withIndex().forEach {
            fHp[it.index] -= it.value
            if (fHp[it.index] < 0) {
                fHp[it.index] = 0
            }
        }

        apiRaigeki.apiEdam.withIndex().forEach {
            eHp[it.index] -= it.value
            if (eHp[it.index] < 0) {
                eHp[it.index] = 0
            }
        }
    }

    private fun applyRaigeki(apiRaigeki: ApiRaigeki, phase: PhaseState) {
        Logger.d("apiRaigeki", apiRaigeki.toString())
        Logger.d("phaseState", phase.toString())
        apiRaigeki.apiFdam.withIndex().forEach {
            if (it.index < phase.fHp.size) {
                phase.fHp[it.index] -= it.value
                if (phase.fHp[it.index] < 0) {
                    phase.fHp[it.index] = 0
                }
            } else if (phase.fHpCombined != null && it.index - 6 < phase.fHpCombined.size) {
                phase.fHpCombined!![it.index - 6] -= it.value
                if (phase.fHpCombined!![it.index - 6] < 0) {
                    phase.fHpCombined[it.index - 6] = 0
                }
            }
        }

        apiRaigeki.apiEdam.withIndex().forEach {
            if (it.index < phase.eHp.size) {
                phase.eHp[it.index] -= it.value
                if (phase.eHp[it.index] < 0) {
                    phase.eHp[it.index] = 0
                }
            } else if (phase.eHpCombined != null && it.index - 6 < phase.eHpCombined.size) {
                phase.eHpCombined!![it.index - 6] -= it.value
                if (phase.eHpCombined!![it.index - 6] < 0) {
                    phase.eHpCombined[it.index - 6] = 0
                }
            }
        }

    }
}