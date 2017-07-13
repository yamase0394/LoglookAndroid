package jp.gr.java_conf.snake0394.loglook_android.storage;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefField;

/**
 * {@link jp.gr.java_conf.snake0394.loglook_android.view.fragment.EquipmentFragment}
 */
@Pref(name = "pref_equip_fragment")
public class EquipmentFragmentPrefs {

    @PrefField(name = "sortType")
    public String sortType = "名前";

    @PrefField(name = "order")
    public String order = "昇順";

    public EquipmentFragmentPrefs() {
        //Empty constructor is needed
    }
}