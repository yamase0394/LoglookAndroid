package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.List;

/**
 * 艦隊
 */
public class Deck {
    /**
     * 艦隊番号
     */
    private int id;

    /**
     * 艦隊名
     */
    private String name;

    /**
     * 遠征状況。[0]={0=未出撃, 1=遠征中, 2=遠征帰投, 3=強制帰投中}, [1]=遠征先ID, [2]=帰投時間, [3]=0
     */
    private List<Long> mission;

    /**
     * 艦隊に配備中の艦の所有艦娘IDのリスト。空きの場合は-1。[0]からidが入っています。
     */
    private List<Integer> shipId;


    /**
     * 更新
     * api_port/port
     * api_req_hensei/preset_select
     * api_req_hensei/change (request)
     */
    private String condRecoveryTime = "";


    /**
     * 更新
     * api_port/port
     * api_req_hensei/preset_select
     * api_req_hensei/change (request)
     */
    private int levelSum;

    public Deck() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getMission() {
        return mission;
    }

    public void setMission(List<Long> mission) {
        this.mission = mission;
    }

    public List<Integer> getShipId() {
        return shipId;
    }

    public void setShipId(List<Integer> shipId) {
        this.shipId = shipId;
    }

    public String getCondRecoveryTime() {
        return condRecoveryTime;
    }

    public void setCondRecoveryTime(String condRecoveryTime) {
        this.condRecoveryTime = condRecoveryTime;
    }

    public int getLevelSum() {
        return levelSum;
    }

    public void setLevelSum(int levelSum) {
        this.levelSum = levelSum;
    }
}
