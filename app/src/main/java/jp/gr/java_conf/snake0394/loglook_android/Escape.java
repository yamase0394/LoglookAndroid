package jp.gr.java_conf.snake0394.loglook_android;

import java.util.ArrayList;

/**
 * 護衛退避
 */
public enum Escape {
  INSTANCE;

  /**
   * 退避した艦船のリスト
   */
  private ArrayList<Integer> escapedShip;

  /**
   * 大破した艦船の固有ID
   */
  private int damaged;

  /**
   * 曳航する艦船の固有ID
   */
  private int towing;

  /**
   * 護衛退避の対象となる艦船をセットします
   * api_req_combined_battle/battleresult
   *
   * @param damaged 大破した艦船の固有ID
   * @param towing  曳航する艦船の固有ID
   */
  public void ready(int damaged, int towing) {
    this.damaged = damaged;
    this.towing = towing;
  }

  /**
   * 護衛退避した艦線をescapedShipに追加します
   * api_req_combined_battle/goback_port
   */
  public void escape() {
    escapedShip.add(damaged);
    escapedShip.add(towing);
  }

  /**
   * パラメーターをリセットします
   * api_port/port
   */
  public void close() {
    escapedShip = new ArrayList<>();
    damaged = 0;
    towing = 0;
  }

  /**
   * 指定された艦船が退避しているかどうか
   *
   * @param shipId 艦船固有ID
   */
  public boolean isEscaped(int shipId) {
    return escapedShip.contains(shipId);
  }
}
