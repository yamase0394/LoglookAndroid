package jp.gr.java_conf.snake0394.loglook_android.bean;

/**
 * Created by snake0394 on 2016/08/05.
 */
public enum SortieBattleresult {
    INSTANCE;

    /**戦闘結果ランク*/
    private String rank;

    /**海域名*/
    private String questName;

    /**敵艦隊名*/
    private String eFleetName;

    /**ドロップした艦種*/
    private String getShipType = "";

    /**ドロップした艦名*/
    private String getShipName = "";

    private SortieBattleresult(){}

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
        return eFleetName;
    }

    public void seteFleetName(String eFleetName) {
        this.eFleetName = eFleetName;
    }

    public String getGetShipType() {
        return getShipType;
    }

    public void setGetShipType(String getShipType) {
        this.getShipType = getShipType;
    }

    public String getGetShipName() {
        return getShipName;
    }

    public void setGetShipName(String getShipName) {
        this.getShipName = getShipName;
    }
}
