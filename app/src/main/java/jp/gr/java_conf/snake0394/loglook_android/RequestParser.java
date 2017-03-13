package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;

/**
 * Created by snake0394 on 2016/10/26.
 */
public class RequestParser {
    public synchronized static void parse(String uri, String requestBody) {
        //Log.d("requestURI", uri);
        try {
            Context context = App.getInstance();
            switch (uri) {


            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        if (App.getInstance().getSharedPreferences().getBoolean("saveRequest", false)) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/request/");

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
                pw.write(requestBody);
                pw.flush();
                pw.close();
            } catch (Exception e) {

            }
        }
    }
}
