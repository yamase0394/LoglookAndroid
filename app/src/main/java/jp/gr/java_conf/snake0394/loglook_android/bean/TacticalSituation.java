package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;

public enum TacticalSituation implements Serializable {
    INSTANCE;

    /**
     * 戦闘情報
     */
    private AbstractBattle battle;

    /**
     * 出撃艦隊の所有艦娘ID
     */
    private List<Integer> friendShipId;

    /**
     * 第二艦隊の所有艦娘ID
     */
    private List<Integer> friendShipIdCombined;

    /**
     * 敵艦隊の艦船ID
     */
    private List<Integer> enemyShipId;

    /**
     * 敵艦隊の艦船ID
     */
    private List<Integer> enemyShipIdCombined;

    /**
     * 出撃艦隊の戦闘前HP
     */
    private List<Integer> friendHpBeforeBattle;

    /**
     * 出撃艦隊のHP
     * このHPを減らしていく
     */
    private List<Integer> friendNowhps;

    /**
     * 出撃艦隊の最大HP
     */
    private List<Integer> friendMaxhps;

    /**
     * 第二艦隊の戦闘前HP
     */
    private List<Integer> friendHpBeforeBattleCombined;

    /**
     * 第二艦隊のHP
     * このHPを減らしていく
     */
    private List<Integer> friendNowhpsCombined;

    /**
     * 第二艦隊の戦闘前HP
     */
    private List<Integer> friendMaxhpsCombined;

    /**
     * 敵艦隊の戦闘前HP
     */
    private List<Integer> enemyHpBeforeBattle;

    /**
     * 敵艦隊のHP
     * このHPを減らしていく
     */
    private List<Integer> enemyNowhps;

    /**
     * 敵艦隊の最大HP
     */
    private List<Integer> enemyMaxhps;

    /**
     * 敵艦隊の戦闘前HP
     */
    private List<Integer> enemyHpBeforeBattleCombined;

    /**
     * 敵艦隊のHP
     * このHPを減らしていく
     */
    private List<Integer> enemyNowhpsCombined;

    /**
     * 敵艦隊の最大HP
     */
    private List<Integer> enemyMaxhpsCombined;

    /**
     * 戦闘後のHPを計算
     */
    public void set(AbstractBattle battle) {
        this.battle = battle;
        friendShipId = new ArrayList<>();
        friendShipIdCombined = new ArrayList<>();
        enemyShipId = new ArrayList<>();
        enemyShipIdCombined = new ArrayList<>();
        friendHpBeforeBattle = new ArrayList<>();
        friendNowhps = new ArrayList<>();
        friendMaxhps = new ArrayList<>();
        friendHpBeforeBattleCombined = new ArrayList<>();
        friendNowhpsCombined = new ArrayList<>();
        friendMaxhpsCombined = new ArrayList<>();
        enemyHpBeforeBattle = new ArrayList<>();
        enemyNowhps = new ArrayList<>();
        enemyMaxhps = new ArrayList<>();
        enemyHpBeforeBattleCombined = new ArrayList<>();
        enemyNowhpsCombined = new ArrayList<>();
        enemyMaxhpsCombined = new ArrayList<>();

        BattleType battleType = battle.getBattleType();

        //出撃艦隊or第一艦隊情報
        Deck deck = DeckManager.INSTANCE.getDeck(battle.getDeckId());
        for (int shipId : deck.getShipId()) {
            if (shipId != -1 && MyShipManager.INSTANCE.contains(shipId)) {
                friendShipId.add(shipId);
            }
        }
        for (int i = 1; i <= friendShipId.size(); i++) {
            friendHpBeforeBattle.add(battle.getNowhps().get(i));
            friendNowhps.add(battle.getNowhps().get(i));
            friendMaxhps.add(battle.getMaxhps().get(i));
        }

        //第ニ艦隊情報
        switch (battleType) {
            case COMBINED_BATTLE:
            case COMBINED_WATER:
            case COMBINED_SP_MIDNIGHT:
            case COMBINED_AIRBATTLE:
            case COMBINED_LD_AIRBATTLE:
            case COMBINED_EACH:
            case COMBINED_EACH_WATER:
                Deck deckCombined = DeckManager.INSTANCE.getDeck(2);
                for (int shipId : deckCombined.getShipId()) {
                    if (shipId != -1 && MyShipManager.INSTANCE.contains(shipId)) {
                        friendShipIdCombined.add(shipId);
                    }
                }
                for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                    friendHpBeforeBattleCombined.add(battle.getNowhpsCombined().get(i));
                    friendNowhpsCombined.add(battle.getNowhpsCombined().get(i));
                    friendMaxhpsCombined.add(battle.getMaxhpsCombined().get(i));
                }
        }

        //敵艦隊情報
        for (int shipId : battle.geteShip()) {
            if (shipId != -1 && MstShipManager.INSTANCE.contains(shipId)) {
                enemyShipId.add(shipId);
            }
        }
        for (int i = 7; i <= enemyShipId.size() + 6; i++) {
            enemyHpBeforeBattle.add(battle.getNowhps().get(i));
            enemyNowhps.add(battle.getNowhps().get(i));
            enemyMaxhps.add(battle.getMaxhps().get(i));
        }

        //敵第二艦隊情報
        switch (battleType) {
            case COMBINED_EC:
            case COMBINED_EACH:
            case COMBINED_EACH_WATER:
                for (int shipId : battle.geteShipCombined()) {
                    if (shipId != -1 && MstShipManager.INSTANCE.contains(shipId)) {
                        enemyShipIdCombined.add(shipId);
                    }
                }
                for (int i = 7; i <= enemyShipIdCombined.size() + 6; i++) {
                    enemyHpBeforeBattleCombined.add(battle.getNowhpsCombined().get(i));
                    enemyNowhpsCombined.add(battle.getNowhpsCombined().get(i));
                    enemyMaxhpsCombined.add(battle.getMaxhpsCombined().get(i));
                }
        }

        /****基地航空隊 噴式****/
        //敵本隊へのダメージ
        List<Integer> baseInjectionEnemyDamage = battle.getBaseInjectionEnemyDamage();
        if (baseInjectionEnemyDamage != null) {
            for (int i = 1; i <= enemyShipId.size(); i++) {
                int hp = enemyNowhps.get(i - 1);
                hp -= baseInjectionEnemyDamage.get(i);
                enemyNowhps.set(i - 1, hp);
            }
        }
        //敵随伴艦隊へのダメージ
        baseInjectionEnemyDamage = battle.getBaseInjectionEnemyDamageCombined();
        if (baseInjectionEnemyDamage != null) {
            for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                int hp = enemyNowhpsCombined.get(i - 1);
                hp -= baseInjectionEnemyDamage.get(i);
                enemyNowhpsCombined.set(i - 1, hp);
            }
        }

        /****基地航空隊****/
        //敵本隊へのダメージ
        List<List<Integer>> baseEnemyDamage = battle.getBaseEnemyDamage();
        if (baseEnemyDamage != null) {
            for (List<Integer> damage : baseEnemyDamage) {
                for (int i = 1; i <= enemyShipId.size(); i++) {
                    int hp = enemyNowhps.get(i - 1);
                    hp -= damage.get(i);
                    enemyNowhps.set(i - 1, hp);
                }
            }
        }
        //敵随伴艦隊へのダメージ
        baseEnemyDamage = battle.getBaseEnemyDamageCombined();
        if (baseEnemyDamage != null) {
            for (List<Integer> damage : baseEnemyDamage) {
                for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                    int hp = enemyNowhpsCombined.get(i - 1);
                    hp -= damage.get(i);
                    enemyNowhpsCombined.set(i - 1, hp);
                }
            }
        }

        /****航空戦 噴式****/
        //本隊へのダメージ
        List<Integer> friendDamage = battle.getInjectionKoukuFriendDamage();
        if (friendDamage != null) {
            for (int i = 1; i <= friendShipId.size(); i++) {
                int hp = friendNowhps.get(i - 1);
                hp -= friendDamage.get(i);
                friendNowhps.set(i - 1, hp);
            }
        }
        //随伴艦隊へのダメージ
        friendDamage = battle.getInjectionKoukuFriendDamageCombined();
        if (friendDamage != null) {
            for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                int hp = friendNowhpsCombined.get(i - 1);
                hp -= friendDamage.get(i);
                friendNowhpsCombined.set(i - 1, hp);
            }
        }
        //敵本隊へのダメージ
        List<Integer> enemyDamage = battle.getInjectionKoukuEnemyDamage();
        if (enemyDamage != null) {
            for (int i = 1; i <= enemyShipId.size(); i++) {
                int hp = enemyNowhps.get(i - 1);
                hp -= enemyDamage.get(i);
                enemyNowhps.set(i - 1, hp);
            }
        }
        //敵随伴艦隊へのダメージ
        enemyDamage = battle.getInjectionKoukuEnemyDamageCombined();
        if (enemyDamage != null) {
            for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                int hp = enemyNowhpsCombined.get(i - 1);
                hp -= enemyDamage.get(i);
                enemyNowhpsCombined.set(i - 1, hp);
            }
        }


        /****航空戦****/
        //本隊へのダメージ
        friendDamage = battle.getKoukuFriendDamage();
        if (friendDamage != null) {
            for (int i = 1; i <= friendShipId.size(); i++) {
                int hp = friendNowhps.get(i - 1);
                hp -= friendDamage.get(i);
                friendNowhps.set(i - 1, hp);
            }
        }
        //随伴艦隊へのダメージ
        friendDamage = battle.getKoukuFriendDamageCombined();
        if (friendDamage != null) {
            for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                int hp = friendNowhpsCombined.get(i - 1);
                hp -= friendDamage.get(i);
                friendNowhpsCombined.set(i - 1, hp);
            }
        }
        //敵本隊へのダメージ
        enemyDamage = battle.getKoukuEnemyDamage();
        if (enemyDamage != null) {
            for (int i = 1; i <= enemyShipId.size(); i++) {
                int hp = enemyNowhps.get(i - 1);
                hp -= enemyDamage.get(i);
                enemyNowhps.set(i - 1, hp);
            }
        }
        //敵随伴艦隊へのダメージ
        enemyDamage = battle.getKoukuEnemyDamageCombined();
        if (enemyDamage != null) {
            for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                int hp = enemyNowhpsCombined.get(i - 1);
                hp -= enemyDamage.get(i);
                enemyNowhpsCombined.set(i - 1, hp);
            }
        }

        //長距離空襲の場合はここまで
        switch (battleType) {
            case COMBINED_LD_AIRBATTLE:
                return;
        }


        /****支援攻撃****/
        enemyDamage = battle.getSupportEnemyDamage();
        if (enemyDamage != null) {
            for (int i = 1; i <= enemyShipId.size(); i++) {
                int hp = enemyNowhps.get(i - 1);
                hp -= enemyDamage.get(i);
                enemyNowhps.set(i - 1, hp);
            }
            switch (battleType) {
                case COMBINED_EC:
                case COMBINED_EACH:
                case COMBINED_EACH_WATER:
                    for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                        int hp = enemyNowhpsCombined.get(i - 1);
                        hp -= enemyDamage.get(i + 6);
                        enemyNowhpsCombined.set(i - 1, hp);
                    }
                    break;
            }
        }

        /****航空戦2****/
        switch (battleType) {
            case AIRBATTLE:
            case COMBINED_AIRBATTLE:
                friendDamage = battle.getKouku2FriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipId.size(); i++) {
                        int hp = friendNowhps.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhps.set(i - 1, hp);
                    }
                }
                friendDamage = battle.getKouku2FriendDamageCombined();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                        int hp = friendNowhpsCombined.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhpsCombined.set(i - 1, hp);
                    }
                }
                enemyDamage = battle.getKouku2EnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipId.size(); i++) {
                        int hp = enemyNowhps.get(i - 1);
                        hp -= enemyDamage.get(i);
                        enemyNowhps.set(i - 1, hp);
                    }
                }
                return;
        }


        /****先制対潜****/
        switch (battleType) {
            case BATTLE:
            case PRACTICE:
                List<Integer> at = battle.getOpeningTaisenAtList();
                List<Integer> df = battle.getOpeningTaisenDfList();
                List<Integer> damage = battle.getOpeningTaisenDamage();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }
                break;
            case COMBINED_BATTLE:
            case COMBINED_WATER:
                at = battle.getOpeningTaisenAtList();
                df = battle.getOpeningTaisenDfList();
                damage = battle.getOpeningTaisenDamage();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhpsCombined.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhpsCombined.set(df.get(i) - 1, hp);
                            continue;
                        }
                        int hp = enemyNowhps.get(df.get(i) - 7);
                        hp -= damage.get(i);
                        enemyNowhps.set(df.get(i) - 7, hp);
                    }
                }
                break;
        }

        /****先制雷撃****/
        switch (battleType) {
            case BATTLE:
            case PRACTICE:
                friendDamage = battle.getOpeningAttackFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipId.size(); i++) {
                        int hp = friendNowhps.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhps.set(i - 1, hp);
                    }
                }
                break;
            case COMBINED_BATTLE:
            case COMBINED_WATER:
                friendDamage = battle.getOpeningAttackFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                        int hp = friendNowhpsCombined.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhpsCombined.set(i - 1, hp);
                    }
                }
                break;
            case COMBINED_EC:
            case COMBINED_EACH:
            case COMBINED_EACH_WATER:
                friendDamage = battle.getOpeningAttackFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipId.size(); i++) {
                        int hp = friendNowhps.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhps.set(i - 1, hp);
                    }
                    for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                        int hp = friendNowhpsCombined.get(i - 1);
                        hp -= friendDamage.get(i + 6);
                        friendNowhpsCombined.set(i - 1, hp);
                    }
                }

                enemyDamage = battle.getOpeningAttackEnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                        int hp = enemyNowhpsCombined.get(i - 1);
                        hp -= enemyDamage.get(i + 6);
                        enemyNowhpsCombined.set(i - 1, hp);
                    }
                }
                break;
        }
        //共通処理
        enemyDamage = battle.getOpeningAttackEnemyDamage();
        if (enemyDamage != null) {
            for (int i = 1; i <= enemyShipId.size(); i++) {
                int hp = enemyNowhps.get(i - 1);
                hp -= enemyDamage.get(i);
                enemyNowhps.set(i - 1, hp);
            }
        }

        /****砲雷撃戦****/
        switch (battleType) {
            case BATTLE:
            case PRACTICE:
                List<Integer> at = battle.getHougekiAtList1();
                List<Integer> df = battle.getHougekiDfList1();
                List<Integer> damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }

                at = battle.getHougekiAtList2();
                df = battle.getHougekiDfList2();
                damage = battle.getHougekiDamage2();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }

                friendDamage = battle.getRaigekiFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipId.size(); i++) {
                        int hp = friendNowhps.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhps.set(i - 1, hp);
                    }
                }

                enemyDamage = battle.getRaigekiEnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipId.size(); i++) {
                        int hp = enemyNowhps.get(i - 1);
                        hp -= enemyDamage.get(i);
                        enemyNowhps.set(i - 1, hp);
                    }
                }
                break;

            case SP_MIDNIGTH:
                at = battle.getHougekiAtList1();
                df = battle.getHougekiDfList1();
                damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }
                break;

            case COMBINED_SP_MIDNIGHT:
                at = battle.getHougekiAtList1();
                df = battle.getHougekiDfList1();
                damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhpsCombined.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhpsCombined.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }
                break;

            case COMBINED_BATTLE:
                //随伴護衛艦隊
                at = battle.getHougekiAtList1();
                df = battle.getHougekiDfList1();
                damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhpsCombined.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhpsCombined.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //随伴護衛艦隊 雷撃
                friendDamage = battle.getRaigekiFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                        int hp = friendNowhpsCombined.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhpsCombined.set(i - 1, hp);
                    }
                }

                enemyDamage = battle.getRaigekiEnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipId.size(); i++) {
                        int hp = enemyNowhps.get(i - 1);
                        hp -= enemyDamage.get(i);
                        enemyNowhps.set(i - 1, hp);
                    }
                }

                //本隊1巡目
                at = battle.getHougekiAtList2();
                df = battle.getHougekiDfList2();
                damage = battle.getHougekiDamage2();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //本隊2巡目
                at = battle.getHougekiAtList3();
                df = battle.getHougekiDfList3();
                damage = battle.getHougekiDamage3();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }
                break;

            case COMBINED_WATER:
                //本隊1巡目
                at = battle.getHougekiAtList1();
                df = battle.getHougekiDfList1();
                damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //本隊2巡目
                at = battle.getHougekiAtList2();
                df = battle.getHougekiDfList2();
                damage = battle.getHougekiDamage2();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //随伴護衛艦隊
                at = battle.getHougekiAtList3();
                df = battle.getHougekiDfList3();
                damage = battle.getHougekiDamage3();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (df.get(i) < 7) {
                            int hp = friendNowhpsCombined.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhpsCombined.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //随伴護衛艦隊 雷撃
                friendDamage = battle.getRaigekiFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                        int hp = friendNowhpsCombined.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhpsCombined.set(i - 1, hp);
                    }
                }

                enemyDamage = battle.getRaigekiEnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipId.size(); i++) {
                        int hp = enemyNowhps.get(i - 1);
                        hp -= enemyDamage.get(i);
                        enemyNowhps.set(i - 1, hp);
                    }
                }
                break;

            case COMBINED_EC:
                //砲撃戦(対随伴)
                List<Integer> eflag = battle.getHougekiAtEFlagList1();
                at = battle.getHougekiAtList1();
                df = battle.getHougekiDfList1();
                damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhpsCombined.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhpsCombined.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //雷撃戦(対全体)
                friendDamage = battle.getRaigekiFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipId.size(); i++) {
                        int hp = friendNowhps.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhps.set(i - 1, hp);
                    }
                }

                enemyDamage = battle.getRaigekiEnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipId.size(); i++) {
                        int hp = enemyNowhps.get(i - 1);
                        hp -= enemyDamage.get(i);
                        enemyNowhps.set(i - 1, hp);
                    }
                    for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                        int hp = enemyNowhpsCombined.get(i - 1);
                        hp -= enemyDamage.get(i + 6);
                        enemyNowhpsCombined.set(i - 1, hp);
                    }
                }

                //砲撃戦(対本隊)
                eflag = battle.getHougekiAtEFlagList2();
                at = battle.getHougekiAtList2();
                df = battle.getHougekiDfList2();
                damage = battle.getHougekiDamage2();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 1, hp);
                        }
                    }
                }

                //砲撃戦(対全体)
                eflag = battle.getHougekiAtEFlagList3();
                at = battle.getHougekiAtList3();
                df = battle.getHougekiDfList3();
                damage = battle.getHougekiDamage3();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            if (df.get(i) >= 7) {
                                int hp = enemyNowhpsCombined.get(df.get(i) - 7);
                                hp -= damage.get(i);
                                enemyNowhpsCombined.set(df.get(i) - 7, hp);
                                continue;
                            }
                            int hp = enemyNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 1, hp);
                        }
                    }
                }
                break;

            case COMBINED_EACH:
                //本隊1
                eflag = battle.getHougekiAtEFlagList1();
                at = battle.getHougekiAtList1();
                df = battle.getHougekiDfList1();
                damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 1, hp);
                        }
                    }
                }

                //随伴艦
                eflag = battle.getHougekiAtEFlagList2();
                at = battle.getHougekiAtList2();
                df = battle.getHougekiDfList2();
                damage = battle.getHougekiDamage2();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            int hp = friendNowhpsCombined.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            friendNowhpsCombined.set(df.get(i) - 7, hp);
                        } else {
                            int hp = enemyNowhpsCombined.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhpsCombined.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //随伴護衛艦隊 雷撃
                friendDamage = battle.getRaigekiFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipId.size(); i++) {
                        int hp = friendNowhps.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhps.set(i - 1, hp);
                    }
                    for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                        int hp = friendNowhpsCombined.get(i - 1);
                        hp -= friendDamage.get(i + 6);
                        friendNowhpsCombined.set(i - 1, hp);
                    }
                }

                enemyDamage = battle.getRaigekiEnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipId.size(); i++) {
                        int hp = enemyNowhps.get(i - 1);
                        hp -= enemyDamage.get(i);
                        enemyNowhps.set(i - 1, hp);
                    }
                    for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                        int hp = enemyNowhpsCombined.get(i - 1);
                        hp -= enemyDamage.get(i + 6);
                        enemyNowhpsCombined.set(i - 1, hp);
                    }
                }


                //本隊2巡目
                eflag = battle.getHougekiAtEFlagList3();
                at = battle.getHougekiAtList3();
                df = battle.getHougekiDfList3();
                damage = battle.getHougekiDamage3();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            if (df.get(i) >= 7) {
                                int hp = friendNowhpsCombined.get(df.get(i) - 7);
                                hp -= damage.get(i);
                                friendNowhpsCombined.set(df.get(i) - 7, hp);
                                continue;
                            }
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            if (df.get(i) >= 7) {
                                int hp = enemyNowhpsCombined.get(df.get(i) - 7);
                                hp -= damage.get(i);
                                enemyNowhpsCombined.set(df.get(i) - 7, hp);
                                continue;
                            }
                            int hp = enemyNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 1, hp);
                        }
                    }
                }
                break;

            case COMBINED_EACH_WATER:
                //第1艦隊砲撃(対本隊)
                eflag = battle.getHougekiAtEFlagList1();
                at = battle.getHougekiAtList1();
                df = battle.getHougekiDfList1();
                damage = battle.getHougekiDamage1();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            int hp = enemyNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 1, hp);
                        }
                    }
                }

                //第1艦隊砲撃(対全体)
                eflag = battle.getHougekiAtEFlagList3();
                at = battle.getHougekiAtList3();
                df = battle.getHougekiDfList3();
                damage = battle.getHougekiDamage3();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            if (df.get(i) >= 7) {
                                int hp = friendNowhpsCombined.get(df.get(i) - 7);
                                hp -= damage.get(i);
                                friendNowhpsCombined.set(df.get(i) - 7, hp);
                                continue;
                            }
                            int hp = friendNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            friendNowhps.set(df.get(i) - 1, hp);
                        } else {
                            if (df.get(i) >= 7) {
                                int hp = enemyNowhpsCombined.get(df.get(i) - 7);
                                hp -= damage.get(i);
                                enemyNowhpsCombined.set(df.get(i) - 7, hp);
                                continue;
                            }
                            int hp = enemyNowhps.get(df.get(i) - 1);
                            hp -= damage.get(i);
                            enemyNowhps.set(df.get(i) - 1, hp);
                        }
                    }
                }

                //第2艦隊(対随伴艦)
                eflag = battle.getHougekiAtEFlagList2();
                at = battle.getHougekiAtList2();
                df = battle.getHougekiDfList2();
                damage = battle.getHougekiDamage2();
                if (at != null) {
                    for (int i = 0; i < at.size(); i++) {
                        if (eflag.get(i) == 1) {
                            int hp = friendNowhpsCombined.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            friendNowhpsCombined.set(df.get(i) - 7, hp);
                        } else {
                            int hp = enemyNowhpsCombined.get(df.get(i) - 7);
                            hp -= damage.get(i);
                            enemyNowhpsCombined.set(df.get(i) - 7, hp);
                        }
                    }
                }

                //第2艦隊雷撃(対全体)
                friendDamage = battle.getRaigekiFriendDamage();
                if (friendDamage != null) {
                    for (int i = 1; i <= friendShipId.size(); i++) {
                        int hp = friendNowhps.get(i - 1);
                        hp -= friendDamage.get(i);
                        friendNowhps.set(i - 1, hp);
                    }
                    for (int i = 1; i <= friendShipIdCombined.size(); i++) {
                        int hp = friendNowhpsCombined.get(i - 1);
                        hp -= friendDamage.get(i + 6);
                        friendNowhpsCombined.set(i - 1, hp);
                    }
                }

                enemyDamage = battle.getRaigekiEnemyDamage();
                if (enemyDamage != null) {
                    for (int i = 1; i <= enemyShipId.size(); i++) {
                        int hp = enemyNowhps.get(i - 1);
                        hp -= enemyDamage.get(i);
                        enemyNowhps.set(i - 1, hp);
                    }
                    for (int i = 1; i <= enemyShipIdCombined.size(); i++) {
                        int hp = enemyNowhpsCombined.get(i - 1);
                        hp -= enemyDamage.get(i + 6);
                        enemyNowhpsCombined.set(i - 1, hp);
                    }
                }
                break;
        }

    }

    public AbstractBattle getBattle() {
        return battle;
    }

    public List<Integer> getFriendShipId() {
        return friendShipId;
    }

    public List<Integer> getFriendShipIdCombined() {
        return friendShipIdCombined;
    }

    public List<Integer> getEnemyShipId() {
        return enemyShipId;
    }

    public List<Integer> getEnemyShipIdCombined() {
        return enemyShipIdCombined;
    }

    public List<Integer> getFriendHpBeforeBattle() {
        return friendHpBeforeBattle;
    }

    public List<Integer> getFriendNowhps() {
        return friendNowhps;
    }

    public List<Integer> getFriendMaxhps() {
        return friendMaxhps;
    }

    public List<Integer> getFriendHpBeforeBattleCombined() {
        return friendHpBeforeBattleCombined;
    }

    public List<Integer> getFriendNowhpsCombined() {
        return friendNowhpsCombined;
    }

    public List<Integer> getFriendMaxhpsCombined() {
        return friendMaxhpsCombined;
    }

    public List<Integer> getEnemyHpBeforeBattle() {
        return enemyHpBeforeBattle;
    }

    public List<Integer> getEnemyNowhps() {
        return enemyNowhps;
    }

    public List<Integer> getEnemyMaxhps() {
        return enemyMaxhps;
    }

    public List<Integer> getEnemyHpBeforeBattleCombined() {
        return enemyHpBeforeBattleCombined;
    }

    public List<Integer> getEnemyNowhpsCombined() {
        return enemyNowhpsCombined;
    }

    public List<Integer> getEnemyMaxhpsCombined() {
        return enemyMaxhpsCombined;
    }
}
