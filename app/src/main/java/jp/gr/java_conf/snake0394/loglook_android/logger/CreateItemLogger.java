package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import jp.gr.java_conf.snake0394.loglook_android.EquipType2;
import jp.gr.java_conf.snake0394.loglook_android.bean.Basic;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;

/**
 * Created by snake0394 on 2016/11/16.
 */

public enum CreateItemLogger {
    INSTANCE;

    private int fuel;
    private int bullet;
    private int steel;
    private int bauxite;

    /**
     * writeをする準備ができているか
     */
    private boolean isReady;

    public void ready(int fuel, int bullet, int steel, int bauxite) {
        this.fuel = fuel;
        this.bullet = bullet;
        this.steel = steel;
        this.bauxite = bauxite;
        isReady = true;
    }

    public void write(int createFlag, MstSlotitem mstSlotitem, EquipType2 equipType2) {
        if (!isReady) {
            return;
        }

        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory()
                                               .getPath() + "/泥提督支援アプリ/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "開発報告書.csv";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdirs();
        }

        try {
            StringBuffer sb = new StringBuffer();
            File file = new File(filePath);
            if (!file.exists()) {
                sb.append("日付,開発装備,種別,燃料,弾薬,鋼材,ボーキ,秘書艦,司令部Lv\r\n");
            }

            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "SJIS"));

            //日付
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append(sdf.format(Calendar.getInstance()
                                         .getTime()) + ",");

            //開発装備,種別
            switch (createFlag) {
                case 0:
                    sb.append(",,");
                    break;
                case 1:
                    sb.append(mstSlotitem.getName());
                    sb.append(",");
                    sb.append(equipType2.toString());
                    sb.append(",");
                    break;
                default:
                    sb.append(",,");
            }

            //燃料
            sb.append(fuel);
            sb.append(",");

            //弾薬
            sb.append(bullet);
            sb.append(",");

            //鋼材
            sb.append(steel);
            sb.append(",");

            //ボーキサイト
            sb.append(bauxite);
            sb.append(",");

            //秘書艦
            Deck deck1 = DeckManager.INSTANCE.getDeck(1);
            try(Realm realm = Realm.getDefaultInstance()) {
                MyShip secretaryShip = realm.where(MyShip.class).equalTo("id", deck1.getShipId().get(0)).findFirst();
                MstShip mstShip = realm.where(MstShip.class).equalTo("id", secretaryShip.getShipId()).findFirst();
                sb.append(mstShip.getName())
                        .append("(Lv")
                        .append(secretaryShip.getLv())
                        .append("),");
            }

            //司令部Lv
            sb.append(Basic.INSTANCE.getLevel())
                    .append("\r\n");

            pw.write(sb.toString());
            pw.flush();
            pw.close();

            Log.d("createitem", sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        isReady = false;
    }
}
