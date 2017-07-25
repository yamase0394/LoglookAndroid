package jp.gr.java_conf.snake0394.loglook_android.storage.converter

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import jp.takuji31.koreference.KoreferenceProperty
import jp.takuji31.koreference.converter.ValueConverter
import jp.takuji31.koreference.type.NullableStringPreference
import java.lang.reflect.Type
import kotlin.reflect.KClass

inline fun <T : Any> listPreference(gson: Gson = Gson(), wrapped: KClass<T>, name: String? = null): KoreferenceProperty<List<T>, String?> {
    val t = ListOfSomething<T>(wrapped.java)
    return object : KoreferenceProperty<List<T>, String?>(arrayListOf<T>(), name), NullableStringPreference, ListConverter<T> {
        override val type: Type = t
        override val gson: Gson = gson
    }
}

inline fun <reified K,reified X,reified T : Map<K, List<X>>> mapPreference(default: T, name: String? = null): KoreferenceProperty<T, String?> {
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

interface ListConverter<T : Any?> : ValueConverter<String?, List<T>> {
    val gson: Gson

    val type: Type

    override fun toPreferenceValue(value: List<T>): String? {
        return if (value != null) gson.toJson(value) else null
    }

    override fun toModelValue(value: String?): List<T> {
        return gson.fromJson(value, type)
    }
}