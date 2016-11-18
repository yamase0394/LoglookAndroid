package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.io.Serializable;

/**
 * 艦娘の基本データを持つクラス
 */
public class MstShip implements Serializable {

    private static final long serialVersionUID = -5042059996240131993L;

    /**
     * 艦娘固有id
     * api_id
     */
    private Integer id;

    /**
     * 名前
     * api_name
     */
    private String name;

    /**
     * ふりがな(深海棲艦の場合はflagship、elite）
     * api_yomi
     */
    private String yomi;

    /**api_stype 艦種*/
    private int stype;

    /**
     * 最大搭載燃料
     * api_fuel_max
     */
    private int fuelMax;

    /**
     * 最大搭載弾薬
     * api_bull_max
     */
    private int bullMax;

    public MstShip() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYomi() {
        return yomi;
    }

    public void setYomi(String yomi) {
        this.yomi = yomi;
    }

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getFuelMax() {
        return fuelMax;
    }

    public void setFuelMax(int fuelMax) {
        this.fuelMax = fuelMax;
    }

    public int getBullMax() {
        return bullMax;
    }

    public void setBullMax(int bullMax) {
        this.bullMax = bullMax;
    }
}
