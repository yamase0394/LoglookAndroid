package jp.gr.java_conf.snake0394.loglook_android;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.bean.AbstractBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;

/**
 * Created by snake0394 on 2016/08/30.
 */
public enum TacticalSituation {
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
   * 戦闘後のHPを計算
   */
  public void set(AbstractBattle battle) {
    this.battle = battle;
    friendShipId = new ArrayList<>();
    friendShipIdCombined = new ArrayList<>();
    enemyShipId = new ArrayList<>();
    friendHpBeforeBattle = new ArrayList<>();
    friendNowhps = new ArrayList<>();
    friendMaxhps = new ArrayList<>();
    friendHpBeforeBattleCombined = new ArrayList<>();
    friendNowhpsCombined = new ArrayList<>();
    friendMaxhpsCombined = new ArrayList<>();
    enemyHpBeforeBattle = new ArrayList<>();
    enemyNowhps = new ArrayList<>();
    enemyMaxhps = new ArrayList<>();

    //第1艦隊情報
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

    //第2艦隊情報
    Deck deckCombined;
    if (battle.getNowhpsCombined() != null) {
      deckCombined = DeckManager.INSTANCE.getDeck(2);
      for (int shipId : deckCombined.getShipId()) {
        if (shipId != -1 && MyShipManager.INSTANCE.contains(shipId)) {
          friendShipIdCombined.add(shipId);
        }
      }
      for (int i = 1; i <= friendShipIdCombined.size(); i++) {
        friendHpBeforeBattleCombined.add(battle.getNowhps().get(i));
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

    //基地航空隊
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

    //航空戦
    List<Integer> friendDamage = battle.getKoukuFriendDamage();
    if (friendDamage != null) {
      for (int i = 1; i <= friendShipId.size(); i++) {
        int hp = friendNowhps.get(i - 1);
        hp -= friendDamage.get(i);
        friendNowhps.set(i - 1, hp);
      }
    }

    friendDamage = battle.getKoukuFriendDamageCombined();
    if (friendDamage != null) {
      for (int i = 1; i <= friendShipIdCombined.size(); i++) {
        int hp = friendNowhpsCombined.get(i - 1);
        hp -= friendDamage.get(i);
        friendNowhpsCombined.set(i - 1, hp);
      }
    }

    List<Integer> enemyDamage = battle.getKoukuEnemyDamage();
    if (enemyDamage != null) {
      for (int i = 1; i <= enemyShipId.size(); i++) {
        int hp = enemyNowhps.get(i - 1);
        hp -= enemyDamage.get(i);
        enemyNowhps.set(i - 1, hp);
      }
    }

    //支援攻撃
    enemyDamage = battle.getSupportEnemyDamage();
    if (enemyDamage != null) {
      for (int i = 1; i <= enemyShipId.size(); i++) {
        int hp = enemyNowhps.get(i - 1);
        hp -= enemyDamage.get(i);
        enemyNowhps.set(i - 1, hp);
      }
    }

    //先制対潜
    switch (battle.getBattleType()) {
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
            } else {
              int hp = enemyNowhps.get(df.get(i) - 7);
              hp -= damage.get(i);
              enemyNowhps.set(df.get(i) - 7, hp);
            }
          }
        }
        break;
    }

    //先制雷撃
    switch (battle.getBattleType()) {
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
    }

    enemyDamage = battle.getOpeningAttackEnemyDamage();
    if (enemyDamage != null) {
      for (int i = 1; i <= enemyShipId.size(); i++) {
        int hp = enemyNowhps.get(i - 1);
        hp -= enemyDamage.get(i);
        enemyNowhps.set(i - 1, hp);
      }
    }

    //砲雷撃戦
    switch (battle.getBattleType()) {
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
              hp -= friendDamage.get(i);
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
              hp -= friendDamage.get(i);
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
          for (int i = 0; i <= enemyShipId.size(); i++) {
            int hp = enemyNowhps.get(i);
            hp -= enemyDamage.get(i);
            enemyNowhps.set(i, hp);
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
              hp -= friendDamage.get(i);
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
              hp -= friendDamage.get(i);
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
              hp -= friendDamage.get(i);
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
              hp -= friendDamage.get(i);
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
              hp -= friendDamage.get(i);
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
    }

    //航空戦2
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

  public List<Integer> getFriendNowhps() {
    return friendNowhps;
  }

  public List<Integer> getFriendMaxhps() {
    return friendMaxhps;
  }

  public List<Integer> getFriendNowhpsCombined() {
    return friendNowhpsCombined;
  }

  public List<Integer> getFriendMaxhpsCombined() {
    return friendMaxhpsCombined;
  }

  public List<Integer> getEnemyNowhps() {
    return enemyNowhps;
  }

  public List<Integer> getEnemyMaxhps() {
    return enemyMaxhps;
  }

  public List<Integer> getEnemyHpBeforeBattle() {
    return enemyHpBeforeBattle;
  }

  public List<Integer> getFriendHpBeforeBattle() {
    return friendHpBeforeBattle;
  }

  public List<Integer> getFriendHpBeforeBattleCombined() {
    return friendHpBeforeBattleCombined;
  }
}
