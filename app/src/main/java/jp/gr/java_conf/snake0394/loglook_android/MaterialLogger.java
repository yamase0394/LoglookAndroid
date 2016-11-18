package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.bean.Material;

/**
 * 資材の記録を行います
 */
public enum MaterialLogger {
    INSTANCE;

    /**
     * 前回の記録したときのCalender
     * 記録間隔を制御するのに用いる
     */
    private Calendar preCalender = Calendar.getInstance();


    public void writeLog(Material material, Context context) {

        Calendar calendar = Calendar.getInstance();

        //前の記録から10分以上経過している場合記録する
        if (calendar.compareTo(preCalender) > 0) {
            preCalender = Calendar.getInstance();
            preCalender.setTimeInMillis(calendar.getTimeInMillis() + TimeUnit.MINUTES.toMillis(10));
        } else {
            return;
        }

        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "資材ログ.csv";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }

        try {
            StringBuffer sb = new StringBuffer();
            File file = new File(filePath);
            if (!file.exists()) {
                sb.append("日付,燃料,弾薬,鋼材,ボーキ,高速修復材,高速建造材,開発資材,改修資材\r\n");
            }

            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "SJIS"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append(sdf.format(calendar.getTime()) + ",");
            for (int i = 0; i < material.getMaterialList().size(); i++) {
                sb.append(material.getMaterialList().get(i));
                if (i == material.getMaterialList().size() - 1) {
                    break;
                }
                sb.append(",");
            }
            Log.d("materialLog", sb.toString());
            sb.append("\r\n");
            pw.write(sb.toString());
            pw.flush();
            pw.close();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            if (sp.getBoolean("saveInDropbox", false)) {
                //SDカードのディレクトリパス
                sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/temp/material");

                String fileName = "material" + sdf.format(calendar.getTime()) + ".txt";
                //テキストファイル保存先のファイルパス
                filePath = sdcard_path + Fs + fileName;

                //フォルダがなければ作成
                if (!sdcard_path.exists()) {
                    sdcard_path.mkdirs();
                }
                pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "SJIS"));
                pw.write(sb.toString());
                pw.flush();
                pw.close();

                DropboxAPI<AndroidAuthSession> mDBApi;
                DropboxAuthManager dropboxAuthManager = new DropboxAuthManager(context);
                mDBApi = new DropboxAPI<>(dropboxAuthManager.loadAndroidAuthSession()); //SharedPreferencesから認証情報取得
                file = new File(filePath);
                FileInputStream inputStream = new FileInputStream(file);
                DropboxAPI.Entry response = mDBApi.putFile("/material/" + fileName, inputStream, file.length(), null, null);
                Log.d("DbExampleLog", "The uploaded file's rev is: " + response.rev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
