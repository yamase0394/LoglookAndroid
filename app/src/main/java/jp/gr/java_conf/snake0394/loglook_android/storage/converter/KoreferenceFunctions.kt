package jp.gr.java_conf.snake0394.loglook_android.storage.converter

import android.util.SparseArray
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import jp.takuji31.koreference.KoreferenceProperty
import jp.takuji31.koreference.converter.ValueConverter
import jp.takuji31.koreference.type.NullableStringPreference

inline fun <reified K, reified X, reified T : Map<K, List<X>>> mapPreference(default: T, name: String? = null): KoreferenceProperty<T, String?> {
    return object : KoreferenceProperty<T, String?>(default, name), NullableStringPreference, ValueConverter<String?, T> {
        val gson = Gson()
        override fun toPreferenceValue(value: T): String? {
            return if (value != null) gson.toJson(value) else null
        }

        override fun toModelValue(value: String?): T {
            return gson.fromJson(value, object : TypeToken<Map<K, Collection<X>>>() {}.type)
        }
    }
}

inline fun <reified X, reified T:SparseArray<X>> sparseArrayPreference(default: T, name: String? = null): KoreferenceProperty<T, String?> {
    return object : KoreferenceProperty<T, String?>(default, name), NullableStringPreference, ValueConverter<String?, T> {
        val gson = GsonBuilder()
                .registerTypeAdapter(object : TypeToken<T>() {}.type, SparseArrayTypeAdapter(X::class.java))
                .create()

        override fun toPreferenceValue(value: T): String? {
            return if (value != null) gson.toJson(value) else null
        }

        override fun toModelValue(value: String?): T {
            return gson.fromJson(value, object : TypeToken<T>() {}.type)
        }
    }
}
