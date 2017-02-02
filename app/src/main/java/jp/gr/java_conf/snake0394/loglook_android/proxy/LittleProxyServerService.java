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
import android.widget.Toast;

import com.google.common.primitives.Shorts;

import org.apache.commons.io.IOUtils;
import org.littleshoot.proxy.ChainedProxy;
import org.littleshoot.proxy.ChainedProxyAdapter;
import org.littleshoot.proxy.ChainedProxyManager;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpProxyServerBootstrap;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import jp.gr.java_conf.snake0394.loglook_android.JsonParser;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.RequestParser;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;

public class LittleProxyServerService extends Service implements Runnable {

    private HttpProxyServer server;
    //private Server jettyServer;

    public LittleProxyServerService() {
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
        Notification builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_stat_transparent)
                                                                   .setTicker("start")
                                                                   .setContentTitle("泥提督支援アプリ")
                                                                   .setContentText("")
                                                                   .setWhen(System.currentTimeMillis())
                                                                   .setContentIntent(pending)
                                                                   .setPriority(Notification.PRIORITY_MIN)
                                                                   .build();
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
        server.abort();
        //jettyServer.stop();
        stopForeground(true);
    }

    @Override
    public void run() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        /*
        jettyServer = new Server();
        ServerConnector serverConnector = new ServerConnector(jettyServer);

        // リクエストを待ち受けるポート番号を設定
        serverConnector.setHost("127.0.0.1");
        Log.d("server", sp.getString("port", "8080"));

        serverConnector.setPort(10000);

        jettyServer.addConnector(serverConnector);

        // HTTPS接続をプロキシするため、CONNECTメソッドを処理するハンドラーを設定
        ConnectHandler connectHandler = new MyConnectHandler();
        jettyServer.setHandler(connectHandler);

        // HTTP接続をプロキシするため、ProxyServletを設定
        ServletContextHandler contextHandler = new ServletContextHandler(connectHandler, "/", ServletContextHandler.SESSIONS);

        ServletHolder holder = new ServletHolder(MyAsyncMiddleManServlet.class);
        holder.setInitParameter("timeout", "300000");

        contextHandler.addServlet(holder, "/*");

        try {
            jettyServer.start();
        } catch (Exception e) {
        }
        */

        HttpProxyServerBootstrap serverBuilder = DefaultHttpProxyServer.bootstrap()
                                                                       .withPort(Integer.parseInt(sp.getString("port", "8080")))
                                                                       .withConnectTimeout(30000)
                                                                       .withAllowLocalOnly(true)
                                                                       .withFiltersSource(new CaptureAdapter());

        //上流プロキシの設定
        if (sp.getBoolean("useProxy", false)) {
            serverBuilder.withChainProxyManager(new ChainedProxyManager() {
                @Override
                public void lookupChainedProxies(HttpRequest req, Queue<ChainedProxy> proxies) {
                    if (!KancolleServerSet.INSTANCE.contains(HttpHeaders.getHost(req))) {
                        ChainedProxy proxy = new ChainedProxyAdapter() {
                            @Override
                            public InetSocketAddress getChainedProxyAddress() {
                                String host = sp.getString("proxyHost", "");
                                int port = Integer.parseInt(sp.getString("proxyPort", ""));
                                return new InetSocketAddress(host, port);
                            }
                        };
                        proxies.add(proxy);
                    }
                    proxies.add(ChainedProxyAdapter.FALLBACK_TO_DIRECT_CONNECTION);
                }
            });
        }

        try {
            server = serverBuilder.start();
        } catch (Exception e) {
            if (e.getCause() instanceof BindException) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                     .show();
            } else {
                ErrorLogger.writeLog(e);
            }
            e.printStackTrace();
        }
    }

    private class CaptureAdapter extends HttpFiltersSourceAdapter {

        @Override
        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
            if (originalRequest.getUri()
                               .contains("kcsapi")) {
                return new CaptureFilters(originalRequest, ctx);
            }

            return new HttpFiltersAdapter(originalRequest, ctx);
        }
    }

    private class CaptureFilters extends HttpFiltersAdapter {
        private boolean released;

        private CompositeByteBuf requestBuf = this.ctx.alloc()
                                                      .compositeBuffer();

        private CompositeByteBuf responseBuf = this.ctx.alloc()
                                                       .compositeBuffer();

        private HttpRequest request;

        private HttpResponse response;

        CaptureFilters(HttpRequest originalRequest, ChannelHandlerContext ctx) {
            super(originalRequest, ctx);
        }

        @Override
        public HttpResponse proxyToServerRequest(HttpObject httpObject) {
            if (!this.released) {
                this.add(this.requestBuf, httpObject);
                Log.d("proxyToServerReq", httpObject.toString());
            }

            if (httpObject instanceof HttpRequest) {
                //((HttpRequest) httpObject).headers().add("Connection", "close");
                HttpHeaders.setKeepAlive((HttpRequest) httpObject, false);
            }

            return super.proxyToServerRequest(httpObject);
        }

        @Override
        public HttpObject serverToProxyResponse(HttpObject httpObject) {
            if (!this.released) {
                this.add(this.responseBuf, httpObject);
                Log.d("serverToProxyRes", httpObject.toString());
            }
            return super.serverToProxyResponse(httpObject);
        }

        @Override
        public HttpObject proxyToClientResponse(HttpObject httpObject) {
            if (!this.released) {
                if (httpObject instanceof HttpResponse) {
                    HttpResponse res = (HttpResponse) httpObject;
                    this.response = res;
                    if (!HttpResponseStatus.OK.equals(res.getStatus())) {
                        this.release();
                    }
                    res.headers()
                       .set("Connection", "close");
                }
                Log.d("proxyToClientRes", httpObject.toString());
            }
            return super.proxyToClientResponse(httpObject);
        }

        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            if (httpObject instanceof HttpRequest) {
                //((HttpRequest) httpObject).headers().set("Connection", "close");
                HttpHeaders.setKeepAlive((HttpRequest) httpObject, false);
                //((HttpRequest) httpObject).headers().remove("Proxy-Connection");
                HttpHeaders.removeHeader((HttpRequest) httpObject, "Proxy-Connection");
                this.request = (HttpRequest) httpObject;
            }
            Log.d("clientToProxyReq", httpObject.toString());
            return super.clientToProxyRequest(httpObject);
        }

        @Override
        public void serverToProxyResponseReceived() {
            if (!this.released) {
                try {
                    if (this.request != null && this.response != null) {
                        String regex = "/kcsapi/(.+)";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(this.request.getUri());
                        if (m.find()) {
                            byte[] resbody = this.toByteArray(this.requestBuf.duplicate());
                            byte[] reqbody = this.toByteArray(this.responseBuf.duplicate());
                            final String uri = m.group(1);
                            final String clientReqest = new String(resbody, "UTF-8");
                            final String serverResponse = new String(reqbody, "UTF-8");
                            //タイムアウトを避けるため別スレッドで処理
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    RequestParser.parse(uri, clientReqest);

                                    String jsonStr;
                                    String regex = "svdata=(.+)";
                                    Pattern p = Pattern.compile(regex);
                                    Matcher m = p.matcher(serverResponse);
                                    if (m.find()) {
                                        jsonStr = m.group(1);
                                        JsonParser.parse(uri, jsonStr);
                                        Log.d("uri", uri);
                                        Log.d("clientReq", clientReqest);
                                        Log.d("serverRes", jsonStr);
                                    } else {
                                        //"svdata="が無い場合不必要なデータと判断
                                    }
                                }
                            }).start();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    this.release();
                }
            }
        }

        private void add(CompositeByteBuf buf, HttpObject httpObject) {
            if (httpObject instanceof HttpContent) {
                HttpContent httpContent = (HttpContent) httpObject;
                ByteBuf content = httpContent.content();
                if (content.isReadable()) {
                    buf.addComponent(content.retain());
                    buf.writerIndex(buf.writerIndex() + content.readableBytes());
                }
            }
        }

        private void release() {
            if (!this.released) {
                this.released = true;
                this.requestBuf.release();
                this.responseBuf.release();
                this.request = null;
                this.response = null;
            }
        }

        private byte[] toByteArray(ByteBuf buf) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (ByteBufInputStream in = new ByteBufInputStream(buf)) {
                if (in.available() > 1) {
                    in.mark(Shorts.BYTES);
                    int magicbyte = in.readUnsignedShort();
                    in.reset();
                    InputStream wrap;
                    if (magicbyte == 0x1f8b) {
                        wrap = new GZIPInputStream(in);
                    } else {
                        wrap = in;
                    }
                    IOUtils.copy(wrap, out);
                }
            }
            return out.toByteArray();
        }
    }
}


