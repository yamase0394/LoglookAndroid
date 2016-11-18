package jp.gr.java_conf.snake0394.loglook_android;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by snake0394 on 2016/10/04.
 */

public class ErrorLogger {
  public static void writeLog (Exception e){
    //スタックトレースををStringに
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    pw.flush();
    String str = sw.toString();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/error/");

    String fileName = "error" + sdf.format(Calendar.getInstance().getTime()) + ".txt";

    String Fs = File.separator;

    //テキストファイル保存先のファイルパス
    String filePath = sdcard_path + Fs + fileName;

    //フォルダがなければ作成
    if (!sdcard_path.exists()) {
      sdcard_path.mkdirs();
    }

    try {
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "SJIS"));
      bw.write(str);
      bw.flush();
      bw.close();
    } catch (Exception e1) {

    }
  }
}
