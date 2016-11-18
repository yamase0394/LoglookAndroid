package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.List;

/**
 * Created by snake0394 on 2016/09/13.
 */
public class Material {
    /**
     * [0]から順に燃料, 弾薬, 鋼材, ボーキサイト, 高速建造材, 高速修復材, 開発資材, 改修資材
     */
    private List<Integer> materialList = null;

    public Material() {
    }

    public List<Integer> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Integer> materialList) {
        this.materialList = materialList;
    }
}
