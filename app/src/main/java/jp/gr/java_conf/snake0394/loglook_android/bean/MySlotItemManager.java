package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;

/**
 * Created by snake0394 on 2016/08/22.
 */
public enum MySlotItemManager {
    INSTANCE;

    transient private boolean initialized = false;
    private HashMap<Integer, MySlotItem> mySlotItemMap = new HashMap<>();

    private MySlotItemManager() {
    }

    public void put(MySlotItem mySlotItem) {
        mySlotItemMap.put(mySlotItem.getId(), mySlotItem);
    }

    public MySlotItem getMySlotItem(int id) {
        if (!initialized) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MySlotItemMap.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mySlotItemMap = (HashMap<Integer, MySlotItem>) in.readObject();
                in.close();
                initialized = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mySlotItemMap.get(id);
    }

    public boolean contains(int id) {
        if (!initialized) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + "MySlotItemMap.obj";

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
                mySlotItemMap = (HashMap<Integer, MySlotItem>) in.readObject();
                in.close();
                initialized = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLogger.writeLog(e);
            }
        }
        return mySlotItemMap.containsKey(id);
    }

    public void delete(List<Integer> list) {
        Set<Integer> set = mySlotItemMap.keySet();
        List<Integer> remove = new ArrayList<>();
        for (int i : set) {
            if (!list.contains(i)) {
                remove.add(i);
            }
        }
        for (int i : remove) {
            mySlotItemMap.remove(i);
        }
    }

    public void serialize() {
        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/data/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "MySlotItemMap.obj";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(mySlotItemMap);
            out.close();
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }
    }
}
