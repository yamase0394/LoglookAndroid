package jp.gr.java_conf.snake0394.loglook_android

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.Display
import android.view.View
import android.view.WindowManager
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

class OverlayService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d(TAG, "onStartCommand")
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return Service.START_STICKY
    }

    companion object {
        private val TAG = "OverlayService"
        private var wm: WindowManager? = null
        private val viewMap by lazy { mutableMapOf<View, WindowManager.LayoutParams>() }

        @JvmStatic
        fun addOverlayView(view: View, params: WindowManager.LayoutParams) {
            waitWindowManagerInit()
            wm!!.addView(view, params)
            viewMap.put(view, params)
        }

        @JvmStatic
        fun removeOverlayView(view: View) {
            waitWindowManagerInit()
            wm!!.removeView(view)
            viewMap.remove(view)
        }

        @JvmStatic
        fun removeOverlayViewImmediate(view: View) {
            waitWindowManagerInit()
            wm!!.removeViewImmediate(view)
            viewMap.remove(view)
        }

        @JvmStatic
        fun getDefaultDisplay(): Display {
            waitWindowManagerInit()
            return wm!!.defaultDisplay
        }

        @JvmStatic
        fun hideAllOverlayView() {
            waitWindowManagerInit()
            for ((key) in viewMap) {
                wm!!.removeViewImmediate(key)
            }

            //画面からViewが完全に消えるのを待つ
            Thread.sleep(50)
        }

        @JvmStatic
        fun showAllOverlayView() {
            waitWindowManagerInit()
            for ((key, value) in viewMap) {
                wm!!.addView(key, value)
            }
        }

        @JvmStatic
        fun updateOverlayViewLayout(view: View, params: WindowManager.LayoutParams) {
            waitWindowManagerInit()
            wm!!.updateViewLayout(view, params)
        }

        private fun waitWindowManagerInit() = runBlocking {
            launch {
                while (wm == null) {
                    App.getInstance().startService(Intent(App.getInstance().applicationContext, OverlayService::class.java))
                    Logger.d(TAG, "waiting for initializing completion")
                    delay(300)
                }
            }.join()
        }
    }
}
