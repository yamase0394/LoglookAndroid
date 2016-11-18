package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import jp.gr.java_conf.snake0394.loglook_android.ErrorLogger;

/**
 * Created by snake0394 on 2016/08/04.
 */
public enum MstShipManager implements Serializable {
    INSTANCE;

    transient private boolean initialized = false;

    private HashMap<Integer, MstShip> mstShipMap = new HashMap<>();

    MstShipManager() {
    }

    public void put(MstShip mstShip) {
        mstShipMap.put(mstShip.getId(), mstShip);
    }

    public MstShip getMstShip(int id) {
        if (!initialized) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MstShipMap.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mstShipMap = (HashMap<Integer, MstShip>) in.readObject();
                in.close();
                initialized = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mstShipMap.get(id);
    }

    public boolean contains(int id) {
        if (!initialized) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MstShipMap.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mstShipMap = (HashMap<Integer, MstShip>) in.readObject();
                in.close();
                initialized = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mstShipMap.containsKey(id);
    }


    public void serialize() {
        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "MstShipMap.obj";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(mstShipMap);
            out.close();
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }
    }
}
