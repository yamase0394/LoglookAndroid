package jp.gr.java_conf.snake0394.loglook_android.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by snake0394 on 2016/08/05.
 */
public class SortieBattleresult {

    /**
     * 戦闘結果ランク
     */
    @SerializedName("api_win_rank")
    private String rank;

    /**
     * 海域名
     */
    @SerializedName("api_quest_name")
    private String questName;

    @SerializedName("api_enemy_info")
    private ApiEnemyInfo apiEnemyInfo;

    @SerializedName("api_get_ship")
    private ApiGetShip apiGetShip;


    private SortieBattleresult() {
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String geteFleetName() {
        return this.apiEnemyInfo.eFleetName;
    }

    public void setGetShipType(String getShipType) {
        if (this.apiGetShip == null) {
            return;
        }
        this.apiGetShip.getShipName = getShipType;
    }

    public String getGetShipType() {
        if (this.apiGetShip == null) {
            return "";
        }
        return apiGetShip.getShipType;
    }

    public void setGetShipName(String getShipName) {
        if (this.apiGetShip == null) {
            return;
        }
        this.apiGetShip.getShipName = getShipName;
    }

    public String getGetShipName() {
        if (this.apiGetShip == null) {
            return "";
        }
        return apiGetShip.getShipName;
    }

    private class ApiEnemyInfo {
        /**
         * 敵艦隊名
         */
        @SerializedName("api_deck_name")
        private String eFleetName;

        ApiEnemyInfo() {
        }
    }

    private class ApiGetShip {
        /**
         * ドロップした艦種
         */
        @SerializedName("api_ship_type")
        private String getShipType = "";

        /**
         * ドロップした艦名
         */
        @SerializedName("api_ship_name")
        private String getShipName = "";

        ApiGetShip() {
        }
    }
}
