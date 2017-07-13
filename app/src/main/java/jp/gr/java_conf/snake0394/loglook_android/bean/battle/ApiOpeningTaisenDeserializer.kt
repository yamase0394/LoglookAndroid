package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import com.google.gson.*
import java.lang.reflect.Type

class ApiOpeningTaisenDeserializer : JsonDeserializer<ApiOpeningTaisen> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ApiOpeningTaisen {
        val df =  (json as JsonObject).getAsJsonArray("api_df_list")
        df.remove(0)
        val damage = json.getAsJsonArray("api_damage")
        damage.remove(0)
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(json, ApiOpeningTaisen::class.java)
    }
}