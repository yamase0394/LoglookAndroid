package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.os.Environment;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;

/**
 * Created by snake0394 on 2017/02/21.
 */
public class ApiLogger implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        Log.d("uri", req.getRequestURI());
        Log.d("reqest", req.getParameterMap().toString());
        try {
            JSONObject jsonObject = new JSONObject(json.toString());
            Log.d("response", jsonObject.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeneralPrefs prefs = new GeneralPrefs(App.getInstance().getApplicationContext());
        if(prefs.getLogsJson()) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory()
                                                   .getPath() + "/泥提督支援アプリ/json/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            String uri = req.getRequestURI()
                            .replaceAll("/", "=");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + sdf.format(Calendar.getInstance()
                                                                    .getTime()) + "-" + uri + ".txt";

            //フォルダがなければ作成
            if (!sdcard_path.exists()) {
                sdcard_path.mkdirs();
            }

            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "SJIS")));
                StringBuilder sb = new StringBuilder();
                sb.append("Request---------------------\r\n");
                sb.append(req.getParameterMap()
                             .toString());
                sb.append("\r\n");
                sb.append("Json------------------------\r\n");
                sb.append(json.toString());
                pw.write(sb.toString());
                pw.flush();
                pw.close();
            } catch (Exception e) {
            }
        }
    }
}
