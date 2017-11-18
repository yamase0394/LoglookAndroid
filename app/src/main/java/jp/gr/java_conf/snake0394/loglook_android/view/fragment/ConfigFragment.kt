package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.Manifest
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.Button
import jp.gr.java_conf.snake0394.loglook_android.BuildConfig
import jp.gr.java_conf.snake0394.loglook_android.OverlayService
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.SlantLauncher
import jp.gr.java_conf.snake0394.loglook_android.proxy.LittleProxyServerService
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_config.*
import yuku.ambilwarna.AmbilWarnaDialog

class ConfigFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_config, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()

        val prefs = GeneralPrefs(context)

        //ポート番号
        portEdit.setText(prefs.port.toString())

        //検出領域を可視化するか
        showsViewCheck.isChecked = prefs.showsView

        //検出領域のx,y座標
        val point = displaySize
        if (prefs.viewX == -Short.MAX_VALUE.toInt()) {
            prefs.viewX = point.x / -2
        }
        viewXEdit.setText(prefs.viewX.toString())

        if (prefs.viewY == -Short.MAX_VALUE.toInt()) {
            prefs.viewY = point.y / -2
        }
        viewYEdit.setText(prefs.viewY.toString())

        //検出領域の大きさ
        viewWidthEdit.setText(prefs.viewWidth.toString())
        viewHeightEdit.setText(prefs.viewHeight.toString())

        //検出領域の色
        launcherColorView.setBackgroundColor(prefs.viewColor)
        launcherColorView.setOnClickListener {
            //検出領域の色
            val colorDrawable = launcherColorView.background as ColorDrawable
            AmbilWarnaDialog(context, colorDrawable.color, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    // color is the color selected by the user.
                    launcherColorView.setBackgroundColor(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog) {
                    // cancel was selected by the user
                }
            }).show()
        }

        //検出領域に触れたとき振動させるか
        vibratesWhenViewTouchedCheck.isChecked = prefs.vibratesWhenViewTouched

        //プロキシを使用するか
        usesProxyCheck.isChecked = prefs.usesProxy

        //ホスト名
        proxyHostEdit.setText(prefs.proxyHost)

        //ポート番号
        proxyPortEdit.setText(prefs.proxyPort.toString())

        //jsonを保存するか
        savesJsonCheck.isChecked = prefs.logsJson
        savesJsonCheck.visibility = View.GONE

        //リクエストを保存するか
        savesRequestCheck.isChecked = prefs.logsRequest
        savesRequestCheck.visibility = View.GONE

        usesMissionNotificationCheck.isChecked = prefs.usesMissionNotification
        usesDockNotificationCheck.isChecked = prefs.usesDockNotification
        makesSoundWhenNotifyCheck.isChecked = prefs.makesSoundWhenNotify
        vibratesWhenNotifyCheck.isChecked = prefs.vibratesWhenNOtify
        showsWinRankOverlayCheck!!.isChecked = prefs.showsWinRankOverlay
        showsHeavilyDamagedOverlayCheck!!.isChecked = prefs.showsHeavilyDamagedOverlay
        showsHeavilyDamagedWarningWindowCheck!!.isChecked = prefs.showsHeavilyDamagedWarningWindow

        //デバッグ用
        sendsJsonCheck.isChecked = prefs.sendsJson
        if(!BuildConfig.DEBUG){
            sendsJsonCheck.visibility = View.GONE
        }

        //設定を保存するボタン
        val tb = activity.findViewById(R.id.saveBtn) as Button
        tb.setOnClickListener(View.OnClickListener {
            //ポ―ト番号
            try {
                prefs.port = Integer.parseInt(portEdit.text.toString())
            } catch (e: Exception) {
                prefs.port = 8000
                portEdit.setText(prefs.port.toString())
            }

            //検出領域を可視化するか
            prefs.showsView = showsViewCheck.isChecked

            //検出領域のX座標
            try {
                prefs.viewX = Integer.parseInt(viewXEdit.text.toString())
            } catch (e: Exception) {
                prefs.viewX = point.x / -2
                viewXEdit.setText(prefs.viewX.toString())
            }

            //検出領域のY座標
            try {
                prefs.viewY = Integer.parseInt(viewYEdit.text.toString())
            } catch (e: Exception) {
                prefs.viewY = point.y / -2
                viewYEdit.setText(prefs.viewY.toString())
            }

            //検出領域の幅
            try {
                prefs.viewWidth = Integer.parseInt(viewWidthEdit.text.toString())
                if (prefs.viewWidth > 150) {
                    prefs.viewWidth = 150
                    viewWidthEdit.setText(prefs.viewWidth.toString())
                }
            } catch (e: Exception) {
                prefs.viewWidth = 20
                viewWidthEdit.setText(prefs.viewWidth.toString())
            }

            //検出領域の高さ
            try {
                prefs.viewHeight = Integer.parseInt(viewHeightEdit.text.toString())
                if (prefs.viewHeight > 150) {
                    prefs.viewHeight = 150
                    viewHeightEdit.setText(prefs.viewHeight.toString())
                }
            } catch (e: Exception) {
                prefs.viewHeight = 50
                viewHeightEdit.setText(prefs.viewHeight.toString())
            }

            //検出領域の色
            val colorDrawable = launcherColorView.background as ColorDrawable
            val colorInt = colorDrawable.color
            prefs.viewColor = colorInt

            //検出領域タッチ時に振動させるか
            prefs.vibratesWhenViewTouched = vibratesWhenViewTouchedCheck.isChecked

            //上流プロキシを使用するか
            prefs.usesProxy = usesProxyCheck.isChecked

            //上流プロキシのホスト名
            prefs.proxyHost = proxyHostEdit.text
                    .toString()

            //上流プロキシのポート番号
            try {
                prefs.proxyPort = Integer.parseInt(proxyPortEdit.text.toString())
            } catch (e: Exception) {
                prefs.proxyPort = 8080
                proxyPortEdit.setText(prefs.proxyPort.toString())
            }

            //Jsonを記録するか
            prefs.logsJson = savesJsonCheck.isChecked
            //リクエストボディを記録するか
            prefs.logsRequest = savesRequestCheck.isChecked

            prefs.usesMissionNotification = usesMissionNotificationCheck.isChecked
            prefs.usesDockNotification = usesDockNotificationCheck.isChecked
            prefs.makesSoundWhenNotify = makesSoundWhenNotifyCheck.isChecked
            prefs.vibratesWhenNOtify = vibratesWhenNotifyCheck.isChecked
            prefs.showsWinRankOverlay = showsWinRankOverlayCheck!!.isChecked
            prefs.showsHeavilyDamagedOverlay = showsHeavilyDamagedOverlayCheck!!.isChecked
            prefs.showsHeavilyDamagedWarningWindow = showsHeavilyDamagedWarningWindowCheck!!.isChecked

            //稼働中のサービスを一度停止させてから再び起動させる
            activity.stopService(Intent(activity.applicationContext, LittleProxyServerService::class.java))
            activity.stopService(Intent(activity, SlantLauncher::class.java))
            activity.stopService(Intent(activity.applicationContext, OverlayService::class.java))

            if (!MainActivity.canGetUsageStats(context)) {
                AlertDialog.Builder(activity).setTitle("権限の説明")
                        .setMessage("艦これがフォアグラウンドで起動しているか検知するために\"使用状況へのアクセス\"の権限が必要です")
                        .setPositiveButton(android.R.string.ok) { dialog, which ->
                            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                        }
                        .create()
                        .show()

                return@OnClickListener
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Android6以降の端末でランチャーのオーバーレイ用の権限を取得する
                if (!Settings.canDrawOverlays(context)) {
                    AlertDialog.Builder(activity).setTitle("権限の説明")
                            .setMessage("ランチャー、スクリーンショットを使用するために\"他のアプリに重ねて表示\"の権限が必要です")
                            .setPositiveButton(android.R.string.ok) { dialog, which ->
                                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName)))
                            }
                            .create()
                            .show()
                    return@OnClickListener
                }

                if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder(activity).setTitle("権限の説明")
                            .setMessage("スクリーンショット、戦闘ログの記録を行うためにに\"外部記憶領域への書き込み\"の権限が必要です")
                            .setPositiveButton(android.R.string.ok) { dialog, which ->
                                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                            }
                            .create()
                            .show()
                    return@OnClickListener
                }
            }

            activity.startService(Intent(activity.applicationContext, OverlayService::class.java))
            activity.startService(Intent(activity.applicationContext, LittleProxyServerService::class.java))
            activity.startService(Intent(activity, SlantLauncher::class.java))
        })
    }

    //検出領域を画面の左上に表示するために画面のサイズを求める
    private val displaySize: Point
        get() {
            val wm = activity.getSystemService(WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val real = Point(0, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(real)
                return real

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                try {
                    val getRawWidth = Display::class.java.getMethod("getRawWidth")
                    val getRawHeight = Display::class.java.getMethod("getRawHeight")
                    val width = getRawWidth.invoke(display) as Int
                    val height = getRawHeight.invoke(display) as Int
                    real.set(width, height)
                    return real

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            return real
        }

    companion object {

        fun newInstance(): ConfigFragment = ConfigFragment()
    }
}
