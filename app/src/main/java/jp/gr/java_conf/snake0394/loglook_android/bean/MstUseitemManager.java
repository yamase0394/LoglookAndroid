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
 * Created by snake0394 on 2016/08/08.
 */
public enum MstUseitemManager {
    INSTANCE;

    transient private boolean initialized = false;
    private HashMap<Integer, MstUseitem> mstUseitemMap = new HashMap<>();

    MstUseitemManager() {
    }

    public void put(MstUseitem mstUseitem) {
        mstUseitemMap.put(mstUseitem.getId(), mstUseitem);
    }

    public MstUseitem getMstUseitem(int id) {
        if (!initialized) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MstUseitemMap.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mstUseitemMap = (HashMap<Integer, MstUseitem>) in.readObject();
                in.close();
                initialized = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mstUseitemMap.get(id);
    }

    public void serialize() {
        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "MstUseitemMap.obj";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(mstUseitemMap);
            out.close();
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }
    }
}
