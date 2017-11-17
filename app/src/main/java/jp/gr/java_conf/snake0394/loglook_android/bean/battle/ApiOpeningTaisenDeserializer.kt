package jp.gr.java_conf.snake0394.loglook_android.bean.battle

import com.google.gson.*
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import java.lang.reflect.Type

class ApiOpeningTaisenDeserializer : JsonDeserializer<ApiOpeningTaisen> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ApiOpeningTaisen {
        Logger.d(this.javaClass.name, json.toString())

        if(json?.isJsonNull == true){
            return GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                    .fromJson(json, ApiOpeningTaisen::class.java)
        }

        val df = (json as JsonObject).getAsJsonArray("api_df_list")
        try {
            if (df[0].asInt == -1) {
                df.remove(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val damage = json.getAsJsonArray("api_damage")
        try {
            if (damage[0].asInt == -1) {
                damage.remove(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (json.has("api_at_eflag")) {
            val atEflag = json.getAsJsonArray("api_at_eflag")
            //TODO:修正されているかわからないので保険
            try {
                if (atEflag[0].asInt == -1) {
                    atEflag.remove(0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(json, ApiOpeningTaisen::class.java)
    }
}