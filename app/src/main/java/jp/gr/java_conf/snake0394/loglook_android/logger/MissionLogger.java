package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.gr.java_conf.snake0394.loglook_android.bean.MissionResult;

/**
 * 遠征の記録を行います。
 */
public enum MissionLogger {
    INSTANCE;

    synchronized public void writeLog(MissionResult mr, Context context) {
        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory()
                                               .getPath() + "/泥提督支援アプリ/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "遠征報告書.csv";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }

        try {
            StringBuffer sb = new StringBuffer();
            File file = new File(filePath);
            if (!file.exists()) {
                sb.append("日付,結果,海域,遠征,燃料,弾薬,鋼材,ボーキ,アイテム1,獲得数1,アイテム2,獲得数2,経験値合計\r\n");
            }

            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "SJIS"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append(sdf.format(Calendar.getInstance()
                                         .getTime()) + ",");
            if (mr.getClearResult() == 0) {
                sb.append("失敗,");
            } else if (mr.getClearResult() == 1) {
                sb.append("成功,");
            } else if (mr.getClearResult() == 2) {
                sb.append("大成功,");
            }
            sb.append(mr.getMapareaName() + ",");
            sb.append(mr.getQuestName() + ",");
            for (int i = 0; i < mr.getGainMaterial()
                                  .size(); i++) {
                sb.append(mr.getGainMaterial()
                            .get(i) + ",");
            }
            if (mr.getUseitemCount1() > 0) {
                sb.append(mr.getUseitemName1() + ",");
                sb.append(mr.getUseitemCount1() + ",");
            } else {
                sb.append(",,");
            }
            if (mr.getUseitemCount2() > 0) {
                sb.append(mr.getUseitemName2() + ",");
                sb.append(mr.getUseitemCount2());
            } else {
                sb.append(",");
            }
            sb.append("," + mr.getExpSum());
            Log.d("missionLog", sb.toString());
            sb.append("\r\n");
            pw.write(sb.toString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
