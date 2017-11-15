package jp.gr.java_conf.snake0394.loglook_android

import android.annotation.TargetApi
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ScreenCaptureActivity

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ScreenCaptureService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            stopSelf()
            return
        }

        isRunning = true

        setTheme(R.style.AppTheme)

        mediaProjection = ScreenCaptureActivity.getMediaProjection()
        if (mediaProjection == null) {
            stopSelf()
            handler.post({ Toast.makeText(applicationContext, "起動失敗", Toast.LENGTH_SHORT).show() })
            Logger.d("DeckListCaptureService", "mediaProjection = null")
            return
        }

        val metrics = resources.displayMetrics
        OverlayService.getDefaultDisplay().getMetrics(metrics)
        displayWidth = metrics.widthPixels
        displayHeight = metrics.heightPixels
        val density = metrics.densityDpi

        mImageReader = ImageReader.newInstance(displayWidth, displayHeight, PixelFormat.RGBA_8888, 2)

        mVirtualDisplay = mediaProjection.createVirtualDisplay("Capturing Display", displayWidth, displayHeight, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.surface, null, null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d(TAG, "onStartCommand")
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        Logger.d(TAG, "onDestroy")
        mVirtualDisplay.release()
        mediaProjection.stop()
        isRunning = false
    }

    companion object {
        private val TAG = "ScreenCaptureService"
        private val handler by lazy { Handler() }

        private lateinit var mediaProjection: MediaProjection
        private lateinit var mImageReader: ImageReader
        private lateinit var mVirtualDisplay: VirtualDisplay

        private var displayWidth: Int = 0
        private var displayHeight: Int = 0

        @JvmStatic
        var isRunning = false

        @JvmStatic
        fun getScreenshot(): Bitmap {
            val image = mImageReader.acquireLatestImage()
            val planes = image.planes
            val buffer = planes[0].buffer

            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * displayWidth

            val bitmap = Bitmap.createBitmap(displayWidth + rowPadding / pixelStride, displayHeight, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)
            image.close()

            return bitmap
        }
    }
}
