package jp.gr.java_conf.snake0394.loglook_android.storage

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.realm.RealmList
import io.realm.RealmObject
import java.io.IOException

/**
 * Created by snake0394 on 2017/07/28.
 */
object RealmUtils {
    fun getGsonInstance(): Gson {
        return GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        }).registerTypeAdapter(object : TypeToken<RealmList<RealmInt>>() {}.type, object : TypeAdapter<RealmList<RealmInt>>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: RealmList<RealmInt>) {
                // Ignore
            }

            @Throws(IOException::class)
            override fun read(reader: JsonReader): RealmList<RealmInt> {
                val list = RealmList<RealmInt>()
                reader.beginArray()
                while (reader.hasNext()) {
                    list.add(RealmInt(reader.nextInt()))
                }
                reader.endArray()
                return list
            }
        }).create()
    }
}