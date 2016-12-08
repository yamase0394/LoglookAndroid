package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 所有艦船データ
 */
public class MyShip implements Serializable {

  private static final long serialVersionUID = 9173002282498069548L;

  /**
   * 艦娘ID
   */
  private int id;

  /**
   * 並び替え順？
   */
  private int sortno;

  /**
   * 艦娘マスターID
   */
  private int shipId;

  /**
   * レベル
   */
  private int lv;

  /**
   * [0]=累計経験値、[1]=次のレベルまでの経験値、[2]=経験値バーの割合
   */
  private List<Integer> exp;

  /**
   * 現在のhp
   */
  private int nowhp;

  /**
   * 最大hp
   */
  private int maxhp;

  /**
   * 射程
   */
  private int leng;

  /**
   * 装備スロット。装備idが各スロットの位置に入る。空きの場合は-1が入る。
   */
  private List<Integer> slot;

  /**
   * 艦載機搭載数
   */
  private List<Integer> onslot;

  /**
   * 補強増設
   */
  private int slotEx;

  /**
   * 近代化改修状態。[0]=火力、[1]=雷装、[2]=対空、[3]=装甲、[4]=運。
   */
  private List<Integer> kyouka;

  /**
   * レアリティ
   */
  private int backs;

  /**
   * 搭載燃料
   */
  private int fuel;

  /**
   * 搭載弾薬
   */
  private int bull;

  /**
   * 装備可能スロット数
   */
  private int slotnum;

  /**
   * 入渠時間（ミリ秒）
   */
  private long ndockTime;

  /**
   * 入渠時の消費資材量。[0]=燃料、[1]=鋼材
   */
  private List<Integer> ndockItem;

  /**
   * 改装☆？
   */
  private int srate;

  /**
   * コンディション
   */
  private int cond;

  /**
   * 火力。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> karyoku;

  /**
   * 雷装。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> raisou;

  /**
   * 対空。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> taiku;

  /**
   * 装甲。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> soukou;

  /**
   * 回避。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> kaihi;

  /**
   * 対潜。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> taisen;

  /**
   * 索敵。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> sakuteki;

  /**
   * 運。[0]=現在値(装備込み)、[1]=最大値(Lv99時)。
   */
  private List<Integer> lucky;

  /**
   * ロックされているか
   */
  private boolean locked;

  /**
   * ロックされている装備を装備しているか
   */
  private boolean lockedEquip;

  /**
   * 札
   */
  private int sallyArea;

  public MyShip() {
  }

  /**
   * @return 艦娘ID
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getSortno() {
    return sortno;
  }

  public void setSortno(int sortno) {
    this.sortno = sortno;
  }

  /**
   * 艦娘マスターID
   */
  public int getShipId() {
    return shipId;
  }

  public void setShipId(int shipId) {
    this.shipId = shipId;
  }

  public int getLv() {
    return lv;
  }

  public void setLv(int lv) {
    this.lv = lv;
  }

  /**
   * @return [0]=累計経験値、[1]=次のレベルまでの経験値、[2]=経験値バーの割合
   */
  public List<Integer> getExp() {
    return exp;
  }

  public void setExp(List<Integer> exp) {
    this.exp = exp;
  }

  public int getNowhp() {
    return nowhp;
  }

  public void setNowhp(int nowhp) {
    this.nowhp = nowhp;
  }

  public int getMaxhp() {
    return maxhp;
  }

  public void setMaxhp(int maxhp) {
    this.maxhp = maxhp;
  }

  /**
   * @return 射程
   */
  public int getLeng() {
    return leng;
  }

  public void setLeng(int leng) {
    this.leng = leng;
  }

  /**
   * @return 装備中の装備IDのリスト。空きの場合は-1が入る。
   */
  public List<Integer> getSlot() {
    return slot;
  }

  public void setSlot(List<Integer> slot) {
    this.slot = slot;
  }

  /**
   * @return 各スロットの搭載数
   */
  public List<Integer> getOnslot() {
    return onslot;
  }

  public void setOnslot(List<Integer> onslot) {
    this.onslot = onslot;
  }

  public int getSlotEx() {
    return slotEx;
  }

  public void setSlotEx(int slotEx) {
    this.slotEx = slotEx;
  }

  public List<Integer> getKyouka() {
    return kyouka;
  }

  public void setKyouka(List<Integer> kyouka) {
    this.kyouka = kyouka;
  }

  public int getBacks() {
    return backs;
  }

  public void setBacks(int backs) {
    this.backs = backs;
  }

  public int getFuel() {
    return fuel;
  }

  public void setFuel(int fuel) {
    this.fuel = fuel;
  }

  public int getBull() {
    return bull;
  }

  public void setBull(int bull) {
    this.bull = bull;
  }

  public int getSlotnum() {
    return slotnum;
  }

  public void setSlotnum(int slotnum) {
    this.slotnum = slotnum;
  }

  public long getNdockTime() {
    return ndockTime;
  }

  public void setNdockTime(long ndockTime) {
    this.ndockTime = ndockTime;
  }

  public List<Integer> getNdockItem() {
    return ndockItem;
  }

  public void setNdockItem(List<Integer> ndockItem) {
    this.ndockItem = ndockItem;
  }

  public int getSrate() {
    return srate;
  }

  public void setSrate(int srate) {
    this.srate = srate;
  }

  public int getCond() {
    return cond;
  }

  public void setCond(int cond) {
    this.cond = cond;
  }

  public List<Integer> getKaryoku() {
    return karyoku;
  }

  public void setKaryoku(List<Integer> karyoku) {
    this.karyoku = karyoku;
  }

  public List<Integer> getRaisou() {
    return raisou;
  }

  public void setRaisou(List<Integer> raisou) {
    this.raisou = raisou;
  }

  public List<Integer> getTaiku() {
    return taiku;
  }

  public void setTaiku(List<Integer> taiku) {
    this.taiku = taiku;
  }

  public List<Integer> getSoukou() {
    return soukou;
  }

  public void setSoukou(List<Integer> soukou) {
    this.soukou = soukou;
  }

  public List<Integer> getKaihi() {
    return kaihi;
  }

  public void setKaihi(List<Integer> kaihi) {
    this.kaihi = kaihi;
  }

  public List<Integer> getTaisen() {
    return taisen;
  }

  public void setTaisen(List<Integer> taisen) {
    this.taisen = taisen;
  }

  public List<Integer> getSakuteki() {
    return sakuteki;
  }

  public void setSakuteki(List<Integer> sakuteki) {
    this.sakuteki = sakuteki;
  }

  public List<Integer> getLucky() {
    return lucky;
  }

  public void setLucky(List<Integer> lucky) {
    this.lucky = lucky;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(int locked) {
    switch (locked) {
      case 0:
        this.locked = false;
        break;
      case 1:
        this.locked = true;
        break;
      default:
        this.locked = false;
    }
  }

  public boolean isLockedEquip() {
    return lockedEquip;
  }

  public void setLockedEquip(int lockedEquip) {
    switch (lockedEquip) {
      case 0:
        this.lockedEquip = false;
        break;
      case 1:
        this.lockedEquip = true;
        break;
      default:
        this.lockedEquip = false;
    }
  }

  public int getSallyArea() {
    return sallyArea;
  }

  public void setSallyArea(int sallyArea) {
    this.sallyArea = sallyArea;
  }

  public String getName() {
    MstShip mstShip = MstShipManager.INSTANCE.getMstShip(shipId);
    return mstShip.getName();
  }
}