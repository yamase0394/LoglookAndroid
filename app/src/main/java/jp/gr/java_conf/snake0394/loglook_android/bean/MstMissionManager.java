package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;

/**
 * Created by snake0394 on 2016/08/08.
 */
public enum MstMissionManager implements Serializable {
    INSTANCE;

    transient private boolean initializedId = false;
    transient private boolean initializedStr = false;
    private HashMap<Integer, MstMission> mstMissionIdMap = new HashMap<>();
    private HashMap<String, MstMission> mstMissionStringMap = new HashMap<>();

    MstMissionManager() {
    }

    public void put(MstMission mstMission) {
        mstMissionIdMap.put(mstMission.getId(), mstMission);
        mstMissionStringMap.put(mstMission.getName(), mstMission);
    }

    public MstMission getMstMission(int id) {
        if (!initializedId) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MstMissionIdMap.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mstMissionIdMap = (HashMap<Integer, MstMission>) in.readObject();
                in.close();
                initializedId = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mstMissionIdMap.get(id);
    }

    public MstMission getMstMission(String name) {
        if (!initializedStr) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MstMissionStringMap.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mstMissionStringMap = (HashMap<String, MstMission>) in.readObject();
                in.close();
                initializedStr = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mstMissionStringMap.get(name);
    }

    public void serialize() {
        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "MstMissionStringMap.obj";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(mstMissionStringMap);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        //SDカードのディレクトリパス
        sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

        //パス区切り用セパレータ
        Fs = File.separator;

        //テキストファイル保存先のファイルパス
        filePath = sdcard_path + Fs + "MstMissionIdMap.obj";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(mstMissionIdMap);
            out.close();
            initializedId = true;
            initializedStr = true;

        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }
    }
}
