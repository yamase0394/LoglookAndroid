package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import jp.gr.java_conf.snake0394.loglook_android.ErrorLogger;

/**
 * 装備品idと装備品データを対応させます
 */
public enum MstSlotitemManager {
    INSTANCE;

    transient private boolean initialized = false;
    private HashMap<Integer, MstSlotitem> mstSlotitemMap = new HashMap<>();

     MstSlotitemManager() {
    }

    public void put(MstSlotitem mstSlotitem) {
        mstSlotitemMap.put(mstSlotitem.getId(), mstSlotitem);
    }

    public MstSlotitem getMstSlotitem(int id) {
        if (!initialized) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MstSlotitemManager.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mstSlotitemMap = (HashMap<Integer, MstSlotitem>) in.readObject();
                in.close();
                initialized = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mstSlotitemMap.get(id);
    }

    public void serialize() {
        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "MstSlotitemManager.obj";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(mstSlotitemMap);
            out.close();
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }
    }
}

