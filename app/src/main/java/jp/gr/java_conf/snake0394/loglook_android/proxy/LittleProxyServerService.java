package jp.gr.java_conf.snake0394.loglook_android.proxy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.apache.commons.io.IOUtils;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ThreadPoolConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import jp.gr.java_conf.snake0394.loglook_android.JsonParser;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.RequestParser;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;

import static com.google.common.primitives.Shorts.BYTES;


public class LittleProxyServerService extends Service implements Runnable {

    private HttpProxyServer server;

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
        try {
            server.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopForeground(true);
    }

    @Override
    public void run() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        ThreadPoolConfiguration conf = new ThreadPoolConfiguration().withAcceptorThreads(1)
                                                                    .withClientToProxyWorkerThreads(2)
                                                                    .withProxyToServerWorkerThreads(2);

        server = DefaultHttpProxyServer.bootstrap()
                                       .withPort(Integer.parseInt(sp.getString("port", "8080")))
                                       .withAllowLocalOnly(true)
                                       .withConnectTimeout(30000)
                                       .withThreadPoolConfiguration(conf)
                                       .withFiltersSource(new CaptureAdapter())
                                       .start();
    }

    static class CaptureAdapter extends HttpFiltersSourceAdapter {

        @Override
        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
            if (originalRequest.getUri()
                               .contains("kcsapi")) {
                return new CaptureFilters(originalRequest, ctx);
            }
            return new HttpFiltersAdapter(originalRequest, ctx);
        }
    }

    static class CaptureFilters extends HttpFiltersAdapter {
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
            }
            return super.proxyToServerRequest(httpObject);
        }

        @Override
        public HttpObject serverToProxyResponse(HttpObject httpObject) {
            if (!this.released) {
                this.add(this.responseBuf, httpObject);
            }
            return super.serverToProxyResponse(httpObject);
        }

        @Override
        public HttpObject proxyToClientResponse(HttpObject httpObject) {
            if (httpObject instanceof HttpResponse) {
                HttpResponse res = (HttpResponse) httpObject;
                this.response = res;
                if (!HttpResponseStatus.OK.equals(res.getStatus())) {
                    this.release();
                }
            }
            return super.proxyToClientResponse(httpObject);
        }

        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            if (httpObject instanceof HttpRequest) {
                this.request = (HttpRequest) httpObject;
            }
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
                    in.mark(BYTES);
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


