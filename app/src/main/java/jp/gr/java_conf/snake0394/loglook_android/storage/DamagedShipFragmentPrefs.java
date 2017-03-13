package jp.gr.java_conf.snake0394.loglook_android.storage;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefString;

/**
 * {@link jp.gr.java_conf.snake0394.loglook_android.view.fragment.DamagedShipFragment}
 */
@Pref(name = "pref_damaged_ship_fragment")
public class DamagedShipFragmentPrefs {

    @PrefString(name = "sortType", defaultValue = "修復時間")
    public String sortType;

    @PrefString(name = "order", defaultValue = "降順")
    public String order;

    public DamagedShipFragmentPrefs() {
        //Empty constructor is needed
    }
}