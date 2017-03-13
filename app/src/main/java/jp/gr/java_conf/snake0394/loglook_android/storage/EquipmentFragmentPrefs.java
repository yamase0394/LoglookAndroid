package jp.gr.java_conf.snake0394.loglook_android.storage;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefString;

/**
 * {@link jp.gr.java_conf.snake0394.loglook_android.view.fragment.EquipmentFragment}
 */
@Pref(name = "pref_equip_fragment")
public class EquipmentFragmentPrefs {

    @PrefString(name = "sortType", defaultValue = "名前")
    public String sortType;

    @PrefString(name = "order", defaultValue = "昇順")
    public String order;

    public EquipmentFragmentPrefs() {
        //Empty constructor is needed
    }
}