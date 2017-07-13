package jp.gr.java_conf.snake0394.loglook_android.storage;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefField;

/**
 * {@link jp.gr.java_conf.snake0394.loglook_android.view.fragment.DamagedShipFragment}
 */
@Pref(name = "pref_damaged_ship_fragment")
public class DamagedShipFragmentPrefs {

    @PrefField(name = "sortType")
    public String sortType = "修復時間";

    @PrefField(name = "order")
    public String order = "降順";

    public DamagedShipFragmentPrefs() {
        //Empty constructor is needed
    }
}