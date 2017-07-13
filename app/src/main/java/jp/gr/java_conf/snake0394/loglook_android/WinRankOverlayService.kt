package jp.gr.java_conf.snake0394.loglook_android

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import jp.gr.java_conf.snake0394.loglook_android.bean.battle.TacticalSituation
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefsSpotRepository

/**
 * 戦闘時に[TacticalSituation]で計算された勝利ランクをオーバーレイする
 * リザルト(api_req_sortie/battleresult, api_req_combined_battle/battleresult)で停止される
 */
class WinRankOverlayService : Service() {

    private var overlayView: View? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Logger.d(TAG, "onCreate")
        if (!GeneralPrefsSpotRepository.getEntity(applicationContext).showsWinRankOverlay) {
            stopSelf()
            return
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Logger.d(TAG, "onStartCommand")
        val metrics = resources.displayMetrics
        OverlayService.getDefaultDisplay().getMetrics(metrics)
        val displayWidth = metrics.widthPixels
        val displayHeight = metrics.heightPixels

        if (overlayView == null) {
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
            (overlayView!!.findViewById(R.id.text_rank) as TextView).apply { text = TacticalSituation.winRank }
            overlayView!!.setOnClickListener { stopSelf() }
            OverlayService.addOverlayView(overlayView!!, params)
        } else {
            (overlayView!!.findViewById(R.id.text_rank) as TextView).apply { text = TacticalSituation.winRank }
        }

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        OverlayService.removeOverlayView((overlayView ?: return))
    }

    companion object {
        private val TAG = "WinRankOverlayService"
    }
}
