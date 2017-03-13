package jp.gr.java_conf.snake0394.loglook_android.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by snake0394 on 2016/08/07.
 */
public class MstMission implements Serializable {

    private static final long serialVersionUID = -5057488489988318811L;

    /**
     * 遠征ID
     */
    @SerializedName("api_id")
    private int id;

    /**
     * 海域カテゴリID
     */
    @SerializedName("api_maparea_id")
    private int mapareaId;

    /**
     * 遠征名
     */
    @SerializedName("api_name")
    private String name;

    /**
     * 遠征時間(分)
     */
    @SerializedName("api_time")
    private int time;

    /**
     * 獲得アイテム1。[0]=アイテムID、[1]=入手個数
     */
    @SerializedName("api_win_item1")
    private List<Integer> winItem1;

    /**
     * 獲得アイテム2。[0]=アイテムID、[1]=入手個数
     */
    @SerializedName("api_win_item2")
    private List<Integer> winItem2;

    public MstMission() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMapareaId() {
        return mapareaId;
    }

    public void setMapareaId(int mapareaId) {
        this.mapareaId = mapareaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Integer> getWinItem1() {
        return winItem1;
    }

    public void setWinItem1(List<Integer> winItem1) {
        this.winItem1 = winItem1;
    }

    public List<Integer> getWinItem2() {
        return winItem2;
    }

    public void setWinItem2(List<Integer> winItem2) {
        this.winItem2 = winItem2;
    }
}
