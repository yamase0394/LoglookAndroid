package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.UserDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.UserDataStorageSpotRepository;

/**
 * Created by snake0394 on 2016/08/22.
 */
public enum MySlotItemManager {
    INSTANCE;

    private SparseArray<MySlotItem> sparseArray;

    MySlotItemManager() {
        UserDataStorage storage = UserDataStorageSpotRepository.getEntity(App.getInstance());
        this.sparseArray = storage.mySlotitemSparseArray;
    }

    public void put(MySlotItem mySlotItem) {
        this.sparseArray.put(mySlotItem.getId(), mySlotItem);
    }

    public MySlotItem getMySlotItem(int id) {
        return sparseArray.get(id);
    }

    public boolean contains(int id) {
        return sparseArray.indexOfKey(id) >= 0;
    }

    public void delete(List<Integer> list) {
        List<Integer> remove = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            if(!list.contains(sparseArray.keyAt(i))){
                remove.add(sparseArray.keyAt(i));
            }
        }
        for (int id : remove) {
            sparseArray.remove(id);
        }
    }

    public Collection<MySlotItem> getMySlotItems (){
        Collection<MySlotItem> collection = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            collection.add(sparseArray.valueAt(i));
        }
        return collection;
    }

    public void serialize() {
        UserDataStorage storage = UserDataStorageSpotRepository.getEntity(App.getInstance());
        storage.mySlotitemSparseArray = this.sparseArray;
        UserDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}
