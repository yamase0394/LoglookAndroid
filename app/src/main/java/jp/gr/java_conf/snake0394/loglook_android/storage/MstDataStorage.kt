package jp.gr.java_conf.snake0394.loglook_android.storage

import android.content.Context
import android.util.SparseArray
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem
import jp.gr.java_conf.snake0394.loglook_android.storage.converter.sparseArrayPreference
import jp.takuji31.koreference.KoreferenceModel
import jp.takuji31.koreference.gson.gsonPreference
import java.util.*

class MstDataStorage(context: Context):KoreferenceModel(context = context, name = "storage_mst_data") {

    var mstShipSparseArray by sparseArrayPreference(default = SparseArray<MstShip>())

    var mstMissionIdSparseArray by sparseArrayPreference(default = SparseArray<MstMission>())

    var mstMissionNameMap: Map<String, MstMission> by gsonPreference(default = HashMap())

    var mstSlotitemSparseArray by sparseArrayPreference(default = SparseArray<MstSlotitem>())

    var mstUseitemSparseArray by sparseArrayPreference(default = SparseArray<MstUseitem>())
}//Empty constructor is needed