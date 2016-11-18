package jp.gr.java_conf.snake0394.loglook_android.bean;

/**
 * Created by snake0394 on 2016/08/22.
 */
public enum Basic {
    INSTANCE;

    /**
     * 司令Lv
     */
    private int level;

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
