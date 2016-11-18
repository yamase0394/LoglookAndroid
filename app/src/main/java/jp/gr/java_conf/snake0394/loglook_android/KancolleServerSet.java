package jp.gr.java_conf.snake0394.loglook_android;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by snake0394 on 2016/10/27.
 */

public enum KancolleServerSet {
    INSTANCE;

    private Set<String> set;

    KancolleServerSet(){
        set = new HashSet<>();
        set.add("203.104.209.71");//横須賀
        set.add("203.104.209.87");//呉
        set.add("125.6.184.16");//佐世保
        set.add("125.6.187.205");//舞鶴
        set.add("125.6.187.229");//大湊
        set.add("125.6.187.253");//トラック
        set.add("125.6.188.25");//リンガ泊地
        set.add("203.104.248.135");//ラバウル基地
        set.add("125.6.189.7");//ショートランド泊地
        set.add("125.6.189.39");//ブイン基地
        set.add("125.6.189.71");//タウイタウイ泊地
        set.add("125.6.189.103");//パラオ泊地
        set.add("125.6.189.135");//ブルネイ泊地
        set.add("125.6.189.167");//単冠湾泊地
        set.add("125.6.189.215");//幌筵
        set.add("125.6.189.247");//宿毛湾
        set.add("203.104.209.23");//鹿屋基地
        set.add("203.104.209.39");//岩川基地
        set.add("203.104.209.55");//佐伯湾
        set.add("203.104.209.102");//柱島
    }

    public boolean contains(String name){
        return set.contains(name);
    }
}
