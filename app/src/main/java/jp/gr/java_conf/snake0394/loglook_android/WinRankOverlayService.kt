package jp.gr.java_conf.snake0394.loglook_android

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger

/**
 * 戦闘時に[TacticalSituation]で計算された勝利ランクをオーバーレイする
 * リザルト[jp.gr.java_conf.snake0394.loglook_android.api.ApiReqPracticeBattleResult],
 * [jp.gr.java_conf.snake0394.loglook_android.api.ApiReqSortieBattleresult],
 * [jp.gr.java_conf.snake0394.loglook_android.api.ApiReqCombinedBattleBattleresult]で停止される
 */
class WinRankOverlayService : Service() {

    private var overlayView: View? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Logger.d(TAG, "onStartCommand")

        if (startId > 1) {
            Logger.d(TAG, "$TAG is already running")
            (overlayView!!.findViewById(R.id.text_rank) as TextView).apply { text = "${if (TacticalSituation.isBoss) "ボス " else ""}${TacticalSituation.winRank}" }
            return START_STICKY
        }

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
        (overlayView!!.findViewById(R.id.text_rank) as TextView).apply { text = "${if (TacticalSituation.isBoss) "ボス " else ""}${TacticalSituation.winRank}" }
        overlayView!!.setOnClickListener {
            Logger.d(TAG, "$TAG was touched")
            OverlayService.removeOverlayView((overlayView ?: return@setOnClickListener))
            stopSelf()
        }
        OverlayService.addOverlayView(overlayView!!, params)
        Logger.d(TAG, "show overlay")

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        OverlayService.removeOverlayView((overlayView ?: return))
        Logger.d(TAG, "onDestroy")
    }

    companion object {
        private val TAG = "WinRankOverlayService"
    }
}
