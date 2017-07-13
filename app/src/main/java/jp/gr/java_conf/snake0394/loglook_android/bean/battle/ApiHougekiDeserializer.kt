package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import com.google.gson.*
import java.lang.reflect.Type

/**
 * Created by snake0394 on 2017/07/05.
 */
class ApiHougekiDeserializer : JsonDeserializer<ApiHougeki> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ApiHougeki {
        val df =  (json as JsonObject).getAsJsonArray("api_df_list")
        df.remove(0)
        val damage = json.getAsJsonArray("api_damage")
        damage.remove(0)
        if(json.has("api_at_eflag")){
            val eflag = json.getAsJsonArray("api_at_eflag")
            eflag.remove(0)
        }
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(json, ApiHougeki::class.java)
    }
}