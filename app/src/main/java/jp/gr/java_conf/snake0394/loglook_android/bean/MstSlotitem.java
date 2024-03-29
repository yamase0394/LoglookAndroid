package jp.gr.java_conf.snake0394.loglook_android.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 装備品のデータを持つクラス
 */
public class MstSlotitem implements Serializable {

    private static final long serialVersionUID = -4407183149378737644L;

    /**
     * id
     */
    @SerializedName("api_id")
    private Integer id;

    /**
     * 名前
     */
    @SerializedName("api_name")
    private String name;

    /**
     * 並び替え順
     */
    @SerializedName("api_sortno")
    private int sortno;

    /**
     * 装備タイプ [0]=大分類、[1]=図鑑表示、[2]=カテゴリ、[3]=アイコンID
     */
    @SerializedName("api_type")
    private List<Integer> type;

    /**
     * 耐久(0)
     */
    @SerializedName("api_taik")
    private int taik;

    /**
     * 装甲
     */
    @SerializedName("api_souk")
    private int souk;

    /**
     * 火力
     */
    @SerializedName("api_houg")
    private int houg;

    /**
     * 雷装
     */
    @SerializedName("api_raig")
    private int raig;

    /**
     * 速力
     */
    @SerializedName("api_soku")
    private int soku;

    /**
     * 爆装
     */
    @SerializedName("api_baku")
    private int baku;

    /**
     * 対空
     */
    @SerializedName("api_tyku")
    private int tyku;

    /**
     * 対潜
     */
    @SerializedName("api_tais")
    private int tais;

    /**
     * ?(0)
     */
    @SerializedName("api_atap")
    private int atap;

    /**
     * 命中
     */
    @SerializedName("api_houm")
    private int houm;

    /**
     * 雷撃命中
     */
    @SerializedName("api_raim")
    private int raim;

    /**
     * 回避
     */
    @SerializedName("api_houk")
    private int houk;

    /**
     * 雷撃回避(0)
     */
    @SerializedName("api_raik")
    private int raik;

    /**
     * 爆撃回避(0)
     */
    @SerializedName("api_bakk")
    private int bakk;

    /**
     * 索敵
     */
    @SerializedName("api_saku")
    private int saku;

    /**
     * 索敵妨害(0)
     */
    @SerializedName("api_sakb")
    private int sakb;

    /**
     * 運(0)
     */
    @SerializedName("api_luck")
    private int luck;

    /**
     * 射程
     */
    @SerializedName("api_leng")
    private int leng;

    /**
     * レアリティ
     */
    @SerializedName("api_rare")
    private int rare;

    /**
     * 廃棄資材
     */
    @SerializedName("api_broken")
    private List<Integer> broken;

    /**
     * 図鑑情報
     */
    @SerializedName("api_info")
    private String info;

    /**
     * ?
     */
    @SerializedName("api_usebull")
    private String usebull;

    /**
     * 航空機コスト
     */
    @SerializedName("api_cost")
    private int cost;

    /**
     * 航続距離
     */
    @SerializedName("api_distance")
    private int distance;


    public MstSlotitem() {
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

    public int getSortno() {
        return sortno;
    }

    public void setSortno(int sortno) {
        this.sortno = sortno;
    }

    public List<Integer> getType() {
        return type;
    }

    public void setType(List<Integer> type) {
        this.type = type;
    }

    public int getTaik() {
        return taik;
    }

    public void setTaik(int taik) {
        this.taik = taik;
    }

    public int getSouk() {
        return souk;
    }

    public void setSouk(int souk) {
        this.souk = souk;
    }

    public int getHoug() {
        return houg;
    }

    public void setHoug(int houg) {
        this.houg = houg;
    }

    public int getRaig() {
        return raig;
    }

    public void setRaig(int raig) {
        this.raig = raig;
    }

    public int getSoku() {
        return soku;
    }

    public void setSoku(int soku) {
        this.soku = soku;
    }

    public int getBaku() {
        return baku;
    }

    public void setBaku(int baku) {
        this.baku = baku;
    }

    public int getTyku() {
        return tyku;
    }

    public void setTyku(int tyku) {
        this.tyku = tyku;
    }

    public int getTais() {
        return tais;
    }

    public void setTais(int tais) {
        this.tais = tais;
    }

    public int getAtap() {
        return atap;
    }

    public void setAtap(int atap) {
        this.atap = atap;
    }

    public int getHoum() {
        return houm;
    }

    public void setHoum(int houm) {
        this.houm = houm;
    }

    public int getRaim() {
        return raim;
    }

    public void setRaim(int raim) {
        this.raim = raim;
    }

    public int getHouk() {
        return houk;
    }

    public void setHouk(int houk) {
        this.houk = houk;
    }

    public int getRaik() {
        return raik;
    }

    public void setRaik(int raik) {
        this.raik = raik;
    }

    public int getBakk() {
        return bakk;
    }

    public void setBakk(int bakk) {
        this.bakk = bakk;
    }

    public int getSaku() {
        return saku;
    }

    public void setSaku(int saku) {
        this.saku = saku;
    }

    public int getSakb() {
        return sakb;
    }

    public void setSakb(int sakb) {
        this.sakb = sakb;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public int getLeng() {
        return leng;
    }

    public void setLeng(int leng) {
        this.leng = leng;
    }

    public int getRare() {
        return rare;
    }

    public void setRare(int rare) {
        this.rare = rare;
    }

    public List<Integer> getBroken() {
        return broken;
    }

    public void setBroken(List<Integer> broken) {
        this.broken = broken;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUsebull() {
        return usebull;
    }

    public void setUsebull(String usebull) {
        this.usebull = usebull;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

}

