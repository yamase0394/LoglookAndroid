package jp.gr.java_conf.snake0394.loglook_android.proxy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.common.base.Optional;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity;

public class LittleProxyServerService extends Service implements Runnable {

    private HttpProxyServer server;
    private final Handler handler = new Handler();
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

        //System.setProperty("http.keepAlive", "false");

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
        } catch (final Exception e) {
            if (e.getCause() instanceof BindException) {
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                             .show();
                    }
                });
            } else {
                ErrorLogger.writeLog(e);
            }
            e.printStackTrace();
        }
    }

    private static class Interceptor {

        private List<ContentListenerSpi> listeners;

        public Interceptor() {
            listeners = new ArrayList<>();
            try {
                listeners.add(APIListener.class.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public void intercept(HttpResponse res, byte[] resbody, HttpRequest req, byte[] reqbody) {
            try {
                for (final ContentListenerSpi listener : this.listeners) {
                    final RequestMetaData requestMetaData = RequestMetaDataWrapper.build(req, resbody);
                    if (listener.test(requestMetaData)) {
                        final ResponseMetaData responseMetaData = ResponseMetaDataWrapper.build(res, reqbody);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    listener.accept(requestMetaData, responseMetaData);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class CaptureAdapter extends HttpFiltersSourceAdapter {

        private Interceptor interceptor = new Interceptor();

        @Override
        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {

            if (originalRequest.getUri()
                               .contains("kcsapi")) {
                return new CaptureFilters(originalRequest, ctx, this.interceptor);
            }

            return new HttpFiltersAdapter(originalRequest, ctx);
        }
    }

    private class CaptureFilters extends HttpFiltersAdapter {
        private Interceptor interceptor;

        private boolean released;

        private CompositeByteBuf requestBuf = this.ctx.alloc()
                                                      .compositeBuffer();

        private CompositeByteBuf responseBuf = this.ctx.alloc()
                                                       .compositeBuffer();

        //private StringBuilder sb = new StringBuilder();

        private HttpRequest request;

        private HttpResponse response;

        CaptureFilters(HttpRequest originalRequest, ChannelHandlerContext ctx, Interceptor interceptor) {
            super(originalRequest, ctx);
            this.interceptor = interceptor;
            //Log.d("CaptureFilter", "start");
            //sb.append("**********");
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //sb.append(sdf.format(Calendar.getInstance().getTime()) + "\r\n");
        }

        @Override
        public HttpResponse proxyToServerRequest(HttpObject httpObject) {
            if (!this.released) {
                this.add(this.requestBuf, httpObject);
            }

            if (httpObject instanceof HttpRequest) {
                //((HttpRequest) httpObject).headers().add("Connection", "close");
                HttpHeaders.setKeepAlive((HttpRequest) httpObject, false);
            }
            //Log.d("proxyToServerReq", httpObject.toString());
            //sb.append("--------proxyToServerReq\r\n");
            //sb.append(httpObject.toString());
            //sb.append("\r\n");
            return super.proxyToServerRequest(httpObject);
        }

        @Override
        public HttpObject serverToProxyResponse(HttpObject httpObject) {
            if (!this.released) {
                this.add(this.responseBuf, httpObject);
            }
            //Log.d("serverToProxyRes", httpObject.toString());
            //sb.append("--------serverToProxyRes\r\n");
            //sb.append(httpObject.toString());
            //sb.append("\r\n");
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
                    HttpHeaders.setKeepAlive((HttpResponse) httpObject, false);
                }
            }
            //Log.d("proxyToClientRes", httpObject.toString());
            //sb.append("--------proxyToClientRes\r\n");
            //sb.append(httpObject.toString());
            //sb.append("\r\n");
            return super.proxyToClientResponse(httpObject);
        }

        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            if (httpObject instanceof HttpRequest) {
                HttpHeaders.setKeepAlive((HttpRequest) httpObject, false);
                HttpHeaders.removeHeader((HttpRequest) httpObject, "Proxy-Connection");
                this.request = (HttpRequest) httpObject;
            }
            //Log.d("clientToProxyReq", httpObject.toString());
            //sb.append("--------clientToProxyReq\r\n");
            //sb.append(httpObject.toString());
            //sb.append("\r\n");
            return super.clientToProxyRequest(httpObject);
        }

        @Override
        public void serverToProxyResponseReceived() {
            if (!this.released) {
                try {
                    if (this.request != null && this.response != null) {
                        byte[] resbody = this.toByteArray(this.requestBuf.duplicate());
                        byte[] reqbody = this.toByteArray(this.responseBuf.duplicate());
                        this.interceptor.intercept(this.response, resbody, this.request, reqbody);
                        //sb.append("--------request\r\n");
                        //sb.append(clientReqest);
                        //sb.append("\r\n");
                        //sb.append("--------response\r\n");
                        //sb.append(serverResponse);
                        //sb.append("\r\n");
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

    static class RequestMetaDataWrapper implements RequestMetaData {

        private String contentType;

        private String method;

        //private Map<String, List<String>> parameterMap;

        private String queryString;

        private String requestURI;

        private Optional<InputStream> requestBody;

        @Override
        public String getContentType() {
            return this.contentType;
        }

        void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public Map<String, Collection<String>> getHeaders() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getMethod() {
            return this.method;
        }

        void setMethod(String method) {
            this.method = method;
        }

        @Override
        public Map<String, List<String>> getParameterMap() {
            throw new UnsupportedOperationException();
        }

        /*
        void setParameterMap(Map<String, List<String>> parameterMap) {
            this.parameterMap = parameterMap;
        }
        */

        @Override
        public String getProtocol() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getQueryString() {
            return this.queryString;
        }

        void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public String getRemoteAddr() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getRemotePort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRequestURI() {
            return this.requestURI;
        }

        void setRequestURI(String requestURI) {
            this.requestURI = requestURI;
        }

        @Override
        public String getRequestURL() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getScheme() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getServerName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getServerPort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<InputStream> getRequestBody() {
            return this.requestBody;
        }

        void setRequestBody(Optional<InputStream> requestBody) {
            this.requestBody = requestBody;
        }

        static RequestMetaData build(HttpRequest req, byte[] body) throws UnsupportedEncodingException, URISyntaxException {
            HttpHeaders header = req.headers();
            RequestMetaDataWrapper meta = new RequestMetaDataWrapper();

            meta.setContentType(header.get(HttpHeaders.Names.CONTENT_TYPE));
            meta.setMethod(req.getMethod()
                              .toString());

            /*
            BiConsumer<Map<String, List<String>>, String[]> accumulator = (m, v) -> {
                if (v.length == 2) {
                    m.computeIfAbsent(v[0], k -> new ArrayList<>())
                     .add(v[1]);
                }
            };
            BiConsumer<Map<String, List<String>>, Map<String, List<String>>> combiner = (m1, m2) -> {
                m2.entrySet()
                  .stream()
                  .forEach(entry -> m1.merge(entry.getKey(), entry.getValue(), (l1, l2) -> {
                      l1.addAll(l2);
                      return l1;
                  }));
            };
            String bodystr = URLDecoder.decode(new String(body, StandardCharsets.UTF_8), "UTF-8");
            meta.setParameterMap(Arrays.stream(bodystr.split("&"))
                                       .map(kv -> kv.split("="))
                                       .collect(LinkedHashMap::new, accumulator, combiner));
            */

            URI url = new URI(req.getUri());
            meta.setQueryString(url.getQuery());
            meta.setRequestURI(url.getPath());
            meta.setRequestBody(Optional.of((InputStream) new ByteArrayInputStream(body)));

            return meta;
        }
    }


    static class ResponseMetaDataWrapper implements ResponseMetaData {

        private int status;

        private String contentType;

        private Optional<InputStream> responseBody;

        @Override
        public int getStatus() {
            return this.status;
        }

        void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public Map<String, Collection<String>> getHeaders() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<InputStream> getResponseBody() {
            return this.responseBody;
        }

        void setResponseBody(Optional<InputStream> responseBody) {
            this.responseBody = responseBody;
        }

        static ResponseMetaData build(HttpResponse res, byte[] body) throws UnsupportedEncodingException, URISyntaxException {
            HttpHeaders header = res.headers();
            ResponseMetaDataWrapper meta = new ResponseMetaDataWrapper();

            meta.setStatus(res.getStatus()
                              .code());
            meta.setContentType(header.get(HttpHeaders.Names.CONTENT_TYPE));
            meta.setResponseBody(Optional.of((InputStream) new ByteArrayInputStream(body)));

            return meta;
        }
    }
}


