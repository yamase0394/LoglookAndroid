package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.List;

/**
 * api_req_mission/result
 */
public class MissionResult {
    /**
     * 艦船IDのリスト。[0]=-1、[1]～[6]=保有艦船ID。リストの長さは出撃艦数+1
     */
    private List<Integer> shipId;

    /**
     * 遠征結果。[0]=失敗、[1]=成功、[2]=大成功
     */
    private int clearResult;

    /**
     * 海域カテゴリ名
     */
    private String mapareaName;

    /**
     * 遠征名
     */
    private String questName;

    /**
     * 獲得資源
     */
    private List<Integer> gainMaterial;

    /**
     * 取得アイテムID(1)
     */
    private int useitemId1 = -1;

    /**
     * 取得アイテム名(1)
     */
    private String useitemName1 = "";

    /**
     * 取得アイテム数(1)
     */
    private int useitemCount1 = -1;

    /**
     * 取得アイテムID(2)
     */
    private int useitemId2 = -1;

    /**
     * 取得アイテム名(2)
     */
    private String useitemName2 = "";

    /**
     * 取得アイテム数(2)
     */
    private int useitemCount2 = -1;

    /**
     * 獲得経験値合計
     */
    private int expSum;

    /**
     * コンストラクタ
     */
    public MissionResult() {
    }

    public List<Integer> getShipId() {
        return shipId;
    }

    public void setShipId(List<Integer> shipId) {
        this.shipId = shipId;
    }

    public int getClearResult() {
        return clearResult;
    }

    public void setClearResult(int clearResult) {
        this.clearResult = clearResult;
    }

    public String getMapareaName() {
        return mapareaName;
    }

    public void setMapareaName(String mapareaName) {
        this.mapareaName = mapareaName;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public List<Integer> getGainMaterial() {
        return gainMaterial;
    }

    public void setGainMaterial(List<Integer> gainMaterial) {
        this.gainMaterial = gainMaterial;
    }

    public int getUseitemId1() {
        return useitemId1;
    }

    public void setUseitemId1(int useitemId1) {
        this.useitemId1 = useitemId1;
    }

    public String getUseitemName1() {
        return useitemName1;
    }

    public void setUseitemName1(String useitemName1) {
        this.useitemName1 = useitemName1;
    }

    public int getUseitemCount1() {
        return useitemCount1;
    }

    public void setUseitemCount1(int useitemCount1) {
        this.useitemCount1 = useitemCount1;
    }

    public int getUseitemId2() {
        return useitemId2;
    }

    public void setUseitemId2(int useitemId2) {
        this.useitemId2 = useitemId2;
    }

    public String getUseitemName2() {
        return useitemName2;
    }

    public void setUseitemName2(String useitemName2) {
        this.useitemName2 = useitemName2;
    }

    public int getUseitemCount2() {
        return useitemCount2;
    }

    public void setUseitemCount2(int useitemCount2) {
        this.useitemCount2 = useitemCount2;
    }

    public int getExpSum() {
        return expSum;
    }

    public void setExpSum(int expSum) {
        this.expSum = expSum;
    }
}
