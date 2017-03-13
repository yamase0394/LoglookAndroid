package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;

/**
 * Created by snake0394 on 2016/08/05.
 */
public class JsonParser {

    public synchronized static void parse(String uri, String jsonStr) {
        Context context = App.getInstance();
        try {
            //Log.d("JsonParser", uri);
            //uriでJSONの解析処理を切り替える
            switch (uri) {


            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (sp.getBoolean("saveJson", false)) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/json/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            uri = uri.replaceAll("/", "=");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + sdf.format(Calendar.getInstance().getTime()) + "-" + uri + ".txt";

            //フォルダがなければ作成
            if (!sdcard_path.exists()) {
                sdcard_path.mkdirs();
            }

            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "SJIS")));
                pw.write(jsonStr);
                pw.flush();
                pw.close();
            } catch (Exception e) {
            }
        }

        //Log.d("JSON", jsonStr);
    }

}