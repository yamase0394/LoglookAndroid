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
 * リザルトで大破艦がいることをオーバーレイするためのサービス
 * 次のリザルトにたどり着くか、母港についたときに消える
 */
class HeavilyDamagedWarningService : Service() {

    private var overlayView: View? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Logger.d(TAG, "onStartCommand")

        val metrics = resources.displayMetrics
        OverlayService.getDefaultDisplay().getMetrics(metrics)
        val displayWidth = metrics.widthPixels
        val displayHeight = metrics.heightPixels

        if (overlayView == null) {
            overlayView = View.inflate(applicationContext, R.layout.overlay_heavily_damaged_warning, null)
            val params = WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    displayWidth / 2,
                    -(displayHeight / 2),
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT
            )
            overlayView!!.setOnClickListener { stopSelf() }
            OverlayService.addOverlayView(overlayView!!, params)
        }

        (overlayView!!.findViewById(R.id.text_heavily_damaged_list) as TextView).apply {
            val heavilyDamagedNames = intent!!.getStringArrayListExtra("nameList")
            var damagedShipNames = ""
            (0 until heavilyDamagedNames.size).forEach { idx ->
                damagedShipNames += heavilyDamagedNames[idx]
                if (idx != heavilyDamagedNames.size - 1) {
                    damagedShipNames += ", "
                }
            }
            text = damagedShipNames
        }

        return Service.START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        OverlayService.removeOverlayView((overlayView ?: return))
    }

    companion object {
        private val TAG = "HeavilyDamagedWarningService"
    }
}
