package jp.gr.java_conf.snake0394.loglook_android

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.Display
import android.view.View
import android.view.WindowManager
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger

class OverlayService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Logger.d(TAG, "onStartCommand")
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private val TAG = "OverlayService"
        private lateinit var wm: WindowManager
        private val viewMap by lazy { mutableMapOf<View, WindowManager.LayoutParams>() }
        private var winRankOverlayView:View? = null

        @JvmStatic fun addOverlayView(view: View, params: WindowManager.LayoutParams) {
            wm.addView(view, params)
            viewMap.put(view, params)
        }

        @JvmStatic fun removeOverlayView(view: View) {
            wm.removeView(view)
            viewMap.remove(view)
        }

        @JvmStatic fun removeOverlayViewImmediate(view: View) {
            wm.removeViewImmediate(view)
            viewMap.remove(view)
        }

        @JvmStatic fun getDefaultDisplay(): Display {
            return wm.defaultDisplay
        }

        @JvmStatic fun hideAllOverlayView() {
            for ((key) in viewMap) {
                wm.removeViewImmediate(key)
            }
            Thread.sleep(50)
        }

        @JvmStatic fun showAllOverlayView() {
            for ((key, value) in viewMap) {
                wm.addView(key, value)
            }
        }

        @JvmStatic fun updateOverlayViewLayout(view: View, params: WindowManager.LayoutParams) {
            wm.updateViewLayout(view, params)
        }
    }
}
