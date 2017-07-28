package jp.gr.java_conf.snake0394.loglook_android.storage

import android.content.Context
import android.util.SparseArray
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.sparseArrayPreference
import jp.takuji31.koreference.KoreferenceModel

/**
 * Created by snake0394 on 2017/02/20.
 */
class UserDataStorage(context: Context) : KoreferenceModel(context = context, name = "storage_user_data") {

    var myShipSparseArray by sparseArrayPreference(default = SparseArray<MyShip>())

    var mySlotitemSparseArray by sparseArrayPreference(default = SparseArray<MySlotItem>())
}//Empty constructor is needed
