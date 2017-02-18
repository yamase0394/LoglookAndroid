package jp.gr.java_conf.snake0394.loglook_android.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by snake0394 on 2016/11/16.
 */

public class Kdock {

    private int id;

    @SerializedName("api_created_ship_id")
    private int mstShipId;

    @SerializedName("api_item1")
    private int fuel;

    @SerializedName("api_item2")
    private int bullet;

    @SerializedName("api_item3")
    private int steel;

    @SerializedName("api_item4")
    private int bauxite;

    @SerializedName("api_item5")
    private int developmentMaterial;

    public Kdock(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMstShipId() {
        return mstShipId;
    }

    public void setMstShipId(int mstShipId) {
        this.mstShipId = mstShipId;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getBullet() {
        return bullet;
    }

    public void setBullet(int bullet) {
        this.bullet = bullet;
    }

    public int getSteel() {
        return steel;
    }

    public void setSteel(int steel) {
        this.steel = steel;
    }

    public int getBauxite() {
        return bauxite;
    }

    public void setBauxite(int bauxite) {
        this.bauxite = bauxite;
    }

    public int getDevelopmentMaterial() {
        return developmentMaterial;
    }

    public void setDevelopmentMaterial(int developmentMaterial) {
        this.developmentMaterial = developmentMaterial;
    }
}
