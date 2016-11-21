package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.gr.java_conf.snake0394.loglook_android.ShipType;
import jp.gr.java_conf.snake0394.loglook_android.bean.Basic;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.Kdock;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;

/**
 * Created by snake0394 on 2016/11/16.
 */

public enum CreateShipLogger {
    INSTANCE;

    private int kdockId;

    /**大型建造フラグ*/
    private int largeFlag;

    /**writeをする準備ができているか*/
    private boolean isReady;

    public int getKdockId() {
        return kdockId;
    }

    public boolean isReady() {
        return isReady;
    }

    public void ready(int kdockId, int largeFlag){
        this.kdockId = kdockId;
        this.largeFlag = largeFlag;
        isReady = true;
    }

    public void write(Kdock kdock,int emptyDockNum){
        if(!isReady){
            return;
        }

        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "建造報告書.csv";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdirs();
        }

        try {
            StringBuffer sb = new StringBuffer();

            File file = new File(filePath);
            if (!file.exists()) {
                sb.append("日付,種類,名前,艦種,燃料,弾薬,鋼材,ボーキ,開発資材,空きドック,秘書艦,司令部Lv\r\n");
            }

            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "SJIS"));

            //日付
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append(sdf.format(Calendar.getInstance().getTime()) + ",");

            //種類
            switch (largeFlag){
                case 0:
                    sb.append("通常艦建造,");
                    break;
                case 1:
                    sb.append("大型艦建造,");
                    break;
                default:
                    sb.append(",");
            }

            //名前
            MstShip createdShip = MstShipManager.INSTANCE.getMstShip(kdock.getMstShipId());
            sb.append(createdShip.getName());
            sb.append(",");

            //艦種
            ShipType shipType = ShipType.toShipType(createdShip.getStype());
            sb.append(shipType.toString());
            sb.append(",");

            //燃料
            sb.append(kdock.getFuel());
            sb.append(",");

            //弾薬
            sb.append(kdock.getBullet());
            sb.append(",");

            //鋼材
            sb.append(kdock.getSteel());
            sb.append(",");

            //ボーキサイト
            sb.append(kdock.getBauxite());
            sb.append(",");

            //開発資材
            sb.append(kdock.getDevelopmentMaterial());
            sb.append(",");

            //空きドック
            sb.append(emptyDockNum);
            sb.append(",");

            //秘書艦
            Deck deck1 = DeckManager.INSTANCE.getDeck(1);
            MyShip  secretaryShip = MyShipManager.INSTANCE.getMyShip(deck1.getShipId().get(0));
            sb.append(secretaryShip.getName() + "(Lv" + secretaryShip.getLv() +")");
            sb.append(",");

            //司令部Lv
            sb.append(Basic.INSTANCE.getLevel());

            sb.append("\r\n");

            pw.write(sb.toString());
            pw.flush();
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        isReady = false;
    }
}
