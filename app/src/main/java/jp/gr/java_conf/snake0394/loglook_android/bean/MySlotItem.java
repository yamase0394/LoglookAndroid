package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.io.Serializable;

/**
 * 所有する装備のデータ
 */
public class MySlotItem implements Serializable {

    private static final long serialVersionUID = 5836325514763519240L;

    /**
     * 装備固有ID
     */
    private int id;

    /**
     * 装備ID
     */
    private int mstId;

    /**
     * 装備ロック 1=ロック、0=ロックされていない
     */
    private int locked;

    /**
     * 改修度。 0=未改修、1～10改修度
     */
    private int level;

    /**
     * 熟練度。熟練度がついていない場合元データは存在しない。0=熟練度なし。1～7=熟練度。
     */
    private int alv = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMstId() {
        return mstId;
    }

    public void setMstId(int mstId) {
        this.mstId = mstId;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAlv() {
        return alv;
    }

    public void setAlv(int alv) {
        this.alv = alv;
    }
}
