package jp.gr.java_conf.snake0394.loglook_android.storage;

import android.util.SparseArray;

import io.github.kobakei.spot.annotation.Pref;
import io.github.kobakei.spot.annotation.PrefString;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MyShipSparseArrayTypeConverter;
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.MySlotitemSparseArrayTypeConverter;

/**
 * Created by snake0394 on 2017/02/20.
 */
@Pref(name = "storage_user_data")
public class UserDataStorage {

    @PrefString(name = "myShipSparseArray", converter = MyShipSparseArrayTypeConverter.class)
    public SparseArray<MyShip> myShipSparseArray;

    @PrefString(name = "mySlotitemSparseArray", converter = MySlotitemSparseArrayTypeConverter.class)
    public SparseArray<MySlotItem> mySlotitemSparseArray;

    public UserDataStorage() {
        //Empty constructor is needed
    }
}
