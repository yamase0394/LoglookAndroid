package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import com.google.gson.*
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import java.lang.reflect.Type

class ApiOpeningTaisenDeserializer : JsonDeserializer<ApiOpeningTaisen> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ApiOpeningTaisen {
        Logger.d(this.javaClass.name, json.toString())

        val df =  (json as JsonObject).getAsJsonArray("api_df_list")
        df.remove(0)

        val damage = json.getAsJsonArray("api_damage")
        damage.remove(0)

        if(json.has("api_at_eflag")){
            val atEflag = json.getAsJsonArray("api_at_eflag")
            atEflag.remove(0)
        }

        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(json, ApiOpeningTaisen::class.java)
    }
}