package jp.gr.java_conf.snake0394.loglook_android.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * api_req_mission/result
 */
public class MissionResult {
    /**
     * 艦船IDのリスト。[0]=-1、[1]～[6]=保有艦船ID。リストの長さは出撃艦数+1
     */
    @SerializedName("api_ship_id")
    private List<Integer> shipId;

    /**
     * 遠征結果。[0]=失敗、[1]=成功、[2]=大成功
     */
    @SerializedName("api_clear_result")
    private int clearResult;

    /**
     * 海域カテゴリ名
     */
    @SerializedName("api_maparea_name")
    private String mapareaName;

    /**
     * 遠征名
     */
    @SerializedName("api_quest_name")
    private String questName;

    /**
     * 取得アイテムの存在フラグ [n]=0ならapi_get_item<n+1>は存在しない
     * 0=なし, 1=高速修復材, 2=高速建造材, 3=開発資材, 4=アイテム, 5=家具コイン
     */
    @SerializedName("api_useitem_flag")
    private List<Integer> useitemFlag;

    /**
     * 獲得資源
     */
    @SerializedName("api_get_material")
    private List<Integer> gainMaterial;

    @SerializedName("api_get_item1")
    private ApiGetItem item1;

    @SerializedName("api_get_item2")
    private ApiGetItem item2;

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

    public List<Integer> getUseitemFlag() {
        return useitemFlag;
    }

    public int getUseitemId1() {
        if (item1 == null) {
            return -1;
        }
        return item1.id;
    }

    public void setUseitemId1(int id) {
        if (item1 == null) {
            return;
        }
        this.item1.id = id;
    }

    public String getUseitemName1() {
        if (item1 == null) {
            return "";
        }
        return item1.name;
    }

    public void setUseitemName1(String name) {
        if (this.item1 == null) {
            return;
        }
        this.item1.name = name;
    }

    public int getUseitemCount1() {
        if (item1 == null) {
            return 0;
        }
        return item1.count;
    }

    public int getUseitemId2() {
        if (item2 == null) {
            return -1;
        }
        return item2.id;
    }

    public void setUseitemId2(int id) {
        if (item2 == null) {
            return;
        }
        this.item2.id = id;
    }

    public String getUseitemName2() {
        if (item2 == null) {
            return "";
        }
        return item2.name;
    }

    public void setUseitemName2(String name) {
        if (this.item2 == null) {
            return;
        }
        this.item2.name = name;
    }

    public int getUseitemCount2() {
        if (item2 == null) {
            return 0;
        }
        return item2.count;
    }

    public int getExpSum() {
        return expSum;
    }

    public void setExpSum(int expSum) {
        this.expSum = expSum;
    }

    private class ApiGetItem {

        @SerializedName("api_useitem_id")
        private int id = -1;

        @SerializedName("api_useitem_name")
        private String name = "";

        @SerializedName("api_useitem_count")
        private int count = 0;

        ApiGetItem() {
        }
    }
}
