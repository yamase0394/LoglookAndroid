package jp.gr.java_conf.snake0394.loglook_android

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import io.realm.Realm
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.ICombinedBattle
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.IEachCombinedBattle
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.PracticeBattle
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger

/**
 * 戦闘時に[TacticalSituation]で計算された勝利ランクをオーバーレイする
 * api_port/port,
 * api_get_member/ship_deckで消える
 */
class WinRankOverlayService : Service() {

    private var overlayView: View? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Logger.d(TAG, "onStartCommand")

        //初回起動時のみOverlayServiceに登録する
        if (startId <= 1) {
            val metrics = resources.displayMetrics
            OverlayService.getDefaultDisplay().getMetrics(metrics)
            val displayWidth = metrics.widthPixels
            val displayHeight = metrics.heightPixels

            overlayView = View.inflate(applicationContext, R.layout.overlay_win_rank, null)
            val params = WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    displayWidth / 2,
                    -(displayHeight / 2),
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            )
            overlayView!!.setOnClickListener { stopSelf() }
            OverlayService.addOverlayView(overlayView!!, params)
        }

        (overlayView!!.findViewById(R.id.text_rank) as TextView).apply {
            text = "${if (TacticalSituation.isBoss && TacticalSituation.battle !is PracticeBattle) "ボス " else ""}${TacticalSituation.winRank}"
        }

        val heavilyDamagedShipIdList = mutableListOf<Int>()
        TacticalSituation.phaseList.last().run {
            val battle = TacticalSituation.battle
            val mainDeck = DeckManager.INSTANCE.getDeck(TacticalSituation.battle.apiDeckId)
            fHp.withIndex().forEach {
                if (it.value <= battle.apiFMaxhps[it.index] / 4) {
                    heavilyDamagedShipIdList.add(mainDeck.shipId[it.index])
                }
            }

            val secondDeck = DeckManager.INSTANCE.getDeck(2)
            val apiMaxhpsCombined =
                    when (battle) {
                        is ICombinedBattle -> battle.apiFMaxhpsCombined
                        is IEachCombinedBattle -> battle.apiFMaxhpsCombined
                        else -> return@run
                    }

            fHpCombined?.withIndex()?.forEach {
                if (it.value <= apiMaxhpsCombined[it.index] / 4) {
                    heavilyDamagedShipIdList.add(secondDeck.shipId[it.index])
                }
            }
        }

        (overlayView!!.findViewById(R.id.text_heavily_damaged) as TextView).apply {
            if (heavilyDamagedShipIdList.isEmpty()) {
                visibility = View.GONE
            } else {
                var shipStrList: List<String> = mutableListOf()
                Realm.getDefaultInstance().use { realm ->
                    shipStrList = heavilyDamagedShipIdList
                            .map { realm.where(MyShip::class.java).equalTo("id", it).findFirst() }
                            .map { realm.where(MstShip::class.java).equalTo("id", it.shipId).findFirst() }
                            .map { it.name }
                }

                (overlayView!!.findViewById(R.id.text_heavily_damaged_list) as TextView).apply {
                    var damagedShipNames = ""
                    (0 until shipStrList.size).forEach { idx ->
                        damagedShipNames += shipStrList[idx]
                        if (idx != shipStrList.size - 1) {
                            damagedShipNames += ", "
                        }
                    }
                    text = damagedShipNames
                }
            }
        }

        Logger.d(TAG, "show overlay")

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(TAG, "onDestroy")
        OverlayService.removeOverlayView((overlayView ?: return))
    }

    companion object {
        private val TAG = "WinRankOverlayService"
    }
}
