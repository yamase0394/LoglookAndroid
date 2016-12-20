package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;

/**
 * Created by snake0394 on 2016/09/07.
 */
public abstract class AbstractBattle {
    public abstract BattleType getBattleType();

    public abstract int getDeckId();

    public abstract List<Integer> geteShip();

    public List<Integer> geteShipCombined() {
        return null;
    }

    public abstract List<Integer> geteShipLv();

    public List<Integer> geteShipLvCombined() {
        return null;
    }

    public abstract List<Integer> getNowhps();

    public abstract List<Integer> getMaxhps();

    public List<Integer> getNowhpsCombined() {
        return null;
    }

    public List<Integer> getMaxhpsCombined() {
        return null;
    }

    public abstract String getFormation();

    public abstract String geteFormation();

    public abstract String getTouchPlane();

    public abstract String getEtTouchPlane();

    public abstract String getTactic();

    public String getSeiku() {
        return "";
    }

    public List<Integer> getBaseInjectionEnemyDamage() {
        return null;
    }

    public List<Integer> getBaseInjectionEnemyDamageCombined() {
        return null;
    }

    public List<List<Integer>> getBaseEnemyDamage() {
        return null;
    }

    public List<List<Integer>> getBaseEnemyDamageCombined() {
        return null;
    }

    public List<Integer> getInjectionKoukuFriendDamage() {
        return null;
    }

    public List<Integer> getInjectionKoukuEnemyDamage() {
        return null;
    }

    public List<Integer> getInjectionKoukuFriendDamageCombined() {
        return null;
    }

    public List<Integer> getInjectionKoukuEnemyDamageCombined() {
        return null;
    }

    public List<Integer> getKoukuFriendDamage() {
        return null;
    }

    public List<Integer> getKoukuEnemyDamage() {
        return null;
    }

    public List<Integer> getKoukuFriendDamageCombined() {
        return null;
    }

    public List<Integer> getKoukuEnemyDamageCombined() {
        return null;
    }

    public List<Integer> getKouku2FriendDamage() {
        return null;
    }

    public List<Integer> getKouku2EnemyDamage() {
        return null;
    }

    public List<Integer> getKouku2FriendDamageCombined() {
        return null;
    }

    public List<Integer> getSupportEnemyDamage() {
        return null;
    }

    public List<Integer> getOpeningTaisenAtList() {
        return null;
    }

    public List<Integer> getOpeningTaisenDfList() {
        return null;
    }

    public List<Integer> getOpeningTaisenDamage() {
        return null;
    }

    public List<Integer> getOpeningAttackFriendDamage() {
        return null;
    }

    public List<Integer> getOpeningAttackEnemyDamage() {
        return null;
    }

    public List<Integer> getHougekiAtEFlagList1() {
        return null;
    }

    public List<Integer> getHougekiAtList1() {
        return null;
    }

    public List<Integer> getHougekiDfList1() {
        return null;
    }

    public List<Integer> getHougekiDamage1() {
        return null;
    }

    public List<Integer> getHougekiAtEFlagList2() {
        return null;
    }

    public List<Integer> getHougekiAtList2() {
        return null;
    }

    public List<Integer> getHougekiDfList2() {
        return null;
    }

    public List<Integer> getHougekiDamage2() {
        return null;
    }

    public List<Integer> getHougekiAtEFlagList3() {
        return null;
    }

    public List<Integer> getHougekiAtList3() {
        return null;
    }

    public List<Integer> getHougekiDfList3() {
        return null;
    }

    public List<Integer> getHougekiDamage3() {
        return null;
    }

    public List<Integer> getRaigekiFriendDamage() {
        return null;
    }

    public List<Integer> getRaigekiEnemyDamage() {
        return null;
    }
}
