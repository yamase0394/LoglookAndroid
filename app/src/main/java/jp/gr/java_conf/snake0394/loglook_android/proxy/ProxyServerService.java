package jp.gr.java_conf.snake0394.loglook_android.proxy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;


public class ProxyServerService extends Service implements Runnable {

    private Server server;

    public ProxyServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        //常駐するための通知
        //タップしたときMainActivityを起動
        intent = new Intent(this, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_stat_transparent).setTicker("start").setContentTitle("泥提督支援アプリ").setContentText("").setWhen(System.currentTimeMillis()).setContentIntent(pending).setPriority(Notification.PRIORITY_MIN).build();
        builder.flags = Notification.FLAG_ONGOING_EVENT;
        startForeground(R.string.app_name, builder);

        //プロキシサーバーを起動
        Thread t = new Thread(this);
        t.start();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopForeground(true);
    }

    @Override
    public void run() {
        server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);

        // リクエストを待ち受けるポート番号を設定
        serverConnector.setHost("127.0.0.1");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("server", sp.getString("port", "8080"));

        serverConnector.setPort(Integer.parseInt(sp.getString("port", "8080")));

        server.addConnector(serverConnector);

        // HTTPS接続をプロキシするため、CONNECTメソッドを処理するハンドラーを設定
        ConnectHandler connectHandler = new MyConnectHandler();
        server.setHandler(connectHandler);

        // HTTP接続をプロキシするため、ProxyServletを設定
        ServletContextHandler contextHandler = new ServletContextHandler(connectHandler, "/", ServletContextHandler.SESSIONS);

        ServletHolder holder = new ServletHolder(MyAsyncMiddleManServlet.class);
        holder.setInitParameter("maxThreads", "256");
        holder.setInitParameter("timeout", "600000");

        contextHandler.addServlet(holder, "/*");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }
    }
}

