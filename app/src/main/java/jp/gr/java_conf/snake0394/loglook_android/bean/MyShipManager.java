package jp.gr.java_conf.snake0394.loglook_android.bean;

import android.util.SparseArray;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.storage.UserDataStorage;
import jp.gr.java_conf.snake0394.loglook_android.storage.UserDataStorageSpotRepository;

/**
 * 所有する全艦娘のデータを管理します
 */
public enum MyShipManager {
    INSTANCE;

    private SparseArray<MyShip> sparseArray;

    private MyShipManager() {
        UserDataStorage storage = UserDataStorageSpotRepository.getEntity(App.getInstance());
        this.sparseArray = storage.myShipSparseArray;
    }

    public void put(int id, MyShip myShip) {
        sparseArray.put(id, myShip);
    }

    public MyShip getMyShip(int id) {
        return sparseArray.get(id);
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

    public boolean contains(int id) {
        return sparseArray.indexOfKey(id) >= 0;
    }

    /**
     * 保持するMyShipのCollectionを返します。
     * deep copyであるため、このCollectionに対する変更は内部データに一切の影響を与えません。
     */
    public Collection<MyShip> getMyShips() {
        Collection<MyShip> values = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            values.add(sparseArray.valueAt(i));
        }

        Cloner cloner = new Cloner();
        return cloner.deepClone(values);
    }

    public void serialize() {
        UserDataStorage storage = UserDataStorageSpotRepository.getEntity(App.getInstance());
        storage.myShipSparseArray = this.sparseArray;
        UserDataStorageSpotRepository.putEntity(App.getInstance(), storage);
    }
}
