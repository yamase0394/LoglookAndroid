package jp.gr.java_conf.snake0394.loglook_android.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by snake0394 on 2016/08/08.
 */
public class MstUseitem implements Serializable{

    private static final long serialVersionUID = 7817017144727754805L;

    /**アイテムID*/
    @SerializedName("api_id")
    private int id;

    /**アイテム名*/
    @SerializedName("api_name")
    private String name;

    public MstUseitem(){}

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
}
