package jp.gr.java_conf.snake0394.loglook_android

/**
 * 護衛退避
 */
enum class Escape {
    INSTANCE;

    /**
     * 退避した艦船のリスト
     */
    private var escapedShip: ArrayList<Int> = ArrayList()

    /**
     * 大破した艦船の固有ID
     */
    private var damaged: Int = 0

    /**
     * 曳航する艦船の固有ID
     */
    private var towing: Int = 0

    /**
     * 護衛退避の対象となる艦船をセットします
     * api_req_combined_battle/battleresult

     * @param damaged 大破した艦船の固有ID
     * *
     * @param towing  曳航する艦船の固有ID
     */
    fun ready(damaged: Int, towing: Int) {
        this.damaged = damaged
        this.towing = towing
    }

    /**
     * 護衛退避した艦線をescapedShipに追加します
     * api_req_combined_battle/goback_port
     */
    fun escape() {
        escapedShip.add(damaged)
        escapedShip.add(towing)
    }

    /**
     * パラメーターをリセットします
     * api_port/port
     */
    fun close() {
        escapedShip = ArrayList<Int>()
        damaged = 0
        towing = 0
    }

    /**
     * 指定された艦船が退避しているかどうか

     * @param shipId 艦船固有ID
     */
    fun isEscaped(shipId: Int): Boolean {
        return escapedShip.contains(shipId)
    }
}
