package jp.gr.java_conf.snake0394.loglook_android.logger

import android.os.Environment
import android.util.Log

import com.google.gson.JsonObject

import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Calendar

import jp.gr.java_conf.snake0394.loglook_android.App
import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Created by snake0394 on 2017/02/21.
 */
class ApiLogger : APIListenerSpi {

    companion object {
        private val url = "http://192.168.1.206:8090/"
    }

    override fun accept(json: JsonObject, req: RequestMetaData, res: ResponseMetaData) {
        Logger.d("uri", req.requestURI)
        Logger.d("reqest", req.parameterMap.toString())

        val prefs = GeneralPrefs(App.getInstance().applicationContext)

        if (prefs.sendsJson) {
            val client = OkHttpClient()
            val mimeType = MediaType.parse("plain/text; charset=utf-8")
            client.newCall(Request.Builder().url(url).post(RequestBody.create(mimeType, req.requestURI)).build()).execute()
            client.newCall(Request.Builder().url(url).post(RequestBody.create(mimeType, req.parameterMap.toString())).build()).execute()

            try {
                val jsonObject = JSONObject(json.toString())
                val jsonStr = jsonObject.toString(2)
                client.newCall(Request.Builder().url(url).post(RequestBody.create(mimeType, jsonStr)).build()).execute()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        if (prefs.logsJson) {
            //SDカードのディレクトリパス
            val sdcard_path = File(Environment.getExternalStorageDirectory().path + "/泥提督支援アプリ/json/")

            val uri = req.requestURI.replace("/".toRegex(), "=")

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            //テキストファイル保存先のファイルパス
            val filePath = sdcard_path.toString() + File.separator + sdf.format(Calendar.getInstance().time) + "-" + uri + ".txt"

            //フォルダがなければ作成
            if (!sdcard_path.exists()) {
                sdcard_path.mkdirs()
            }

            try {
                val pw = PrintWriter(BufferedWriter(OutputStreamWriter(FileOutputStream(filePath), "SJIS")))
                val sb = StringBuilder()
                sb.append("Request---------------------\r\n")
                sb.append(req.parameterMap
                        .toString())
                sb.append("\r\n")
                sb.append("Json------------------------\r\n")
                sb.append(json.toString())
                pw.write(sb.toString())
                pw.flush()
                pw.close()
            } catch (e: Exception) {
            }

        }
    }
}
