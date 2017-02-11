package jp.gr.java_conf.snake0394.loglook_android.bean;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 所有する全艦娘のデータを管理します
 */
public enum MyShipManager {
    INSTANCE;

    private HashMap<Integer, MyShip> myShipMap = new HashMap<>();

    private MyShipManager() {
    }

    public void put(int id, MyShip myShip) {
        myShipMap.put(id, myShip);
    }

    public MyShip getMyShip(int id) {
        return myShipMap.get(id);
    }

    public void delete(List<Integer> list) {
        Set<Integer> set = myShipMap.keySet();
        List<Integer> remove = new ArrayList<>();
        for (int i : set) {
            if (!list.contains(i)) {
                remove.add(i);
            }
        }
        for (int i : remove) {
            myShipMap.remove(i);
        }
    }

    public boolean contains(int id) {
        return myShipMap.containsKey(id);
    }

    /**
     * 保持するMyShipのCollectionを返します。
     * このCollectionに対する変更は内部データに一切の影響を与えません。
     */
    public Collection<MyShip> getMyShips() {
        Cloner cloner = new Cloner();
        return cloner.deepClone(myShipMap.values());
    }
}
