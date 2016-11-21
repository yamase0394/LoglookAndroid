package jp.gr.java_conf.snake0394.loglook_android;

/**
 * Created by snake0394 on 2016/08/30.
 */
public enum BattleType {
    /**通常昼戦*/
    BATTLE,
    /**開幕夜戦*/
    SP_MIDNIGTH,
    /**航空戦*/
    AIRBATTLE,
    /**空襲戦*/
    LD_AIRBATTLE,
    /**空母機動部隊*/
    COMBINED_BATTLE,
    /**水上打撃部隊*/
    COMBINED_WATER,
    /**連合艦隊開幕夜戦*/
    COMBINED_SP_MIDNIGHT,
    /**連合艦隊航空戦*/
    COMBINED_AIRBATTLE,
    /**連合艦隊空襲戦*/
    COMBINED_LD_AIRBATTLE,
    /**敵連合艦隊*/
    COMBINED_EC,
    /**空母機動部隊vs敵連合艦隊*/
    COMBINED_EACH,
    /**水上打撃部隊vs敵連合艦隊*/
    COMBINED_EACH_WATER,
    /**演習*/
    PRACTICE,
    NULL;
}