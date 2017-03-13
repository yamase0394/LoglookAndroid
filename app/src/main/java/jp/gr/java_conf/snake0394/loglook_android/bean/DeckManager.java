package jp.gr.java_conf.snake0394.loglook_android.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有する艦隊を管理する。
 */
public enum DeckManager {
    INSTANCE;

    private Map<Integer,Deck>  deckMap = new HashMap<>();

    private DeckManager(){
    }

    public void put(Deck deck){
        deckMap.put(deck.getId(), deck);
    }

    public Deck getDeck(int id){
        return deckMap.get(id);
    }

    public int getDeckNum(){
        return deckMap.size();
    }
}
