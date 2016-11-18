package jp.gr.java_conf.snake0394.loglook_android;

import android.os.Environment;
import android.util.Log;

import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.ByteBufferContentProvider;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.Callback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by snake0394 on 2016/10/27.
 */

public class MyProxyServlet extends ProxyServlet {
    //HTTP通信を行う
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // 何か処理
        try {
            super.service(request, response);
        } catch (Exception e) {

        }
    }

    @Override
    protected org.eclipse.jetty.client.api.Response.Listener newProxyResponseListener(HttpServletRequest request, HttpServletResponse response) {
        //艦これの通信のみ中身を確認する
        if (KancolleServerSet.INSTANCE.contains((request.getServerName()))) {
            return new KancolleResponseListener(request, response);
        }
        return super.newProxyResponseListener(request, response);
    }

    @Override
    protected void customizeProxyRequest(Request proxyRequest, HttpServletRequest request) {
        //艦これの通信のみ中身を確認する
        if (!KancolleServerSet.INSTANCE.contains((request.getServerName()))) {

            //プロキシの設定
            if (App.getInstance().getSharedPreferences().getBoolean("useProxy", false)) {
                // exchange.setAddress(new Address(sp.getString("proxyHost", ""), Integer.parseInt(sp.getString("proxyPort", ""))));
                //艦これ起動できない
                //_client.setProxy(new Address(sp.getString("proxyHost", ""), Integer.parseInt(sp.getString("proxyPort", ""))));
            }
            return;
        }

        StringBuilder sb = new StringBuilder();
        ContentProvider contentProvider = proxyRequest.getContent();
        try {
            final Iterator<ByteBuffer> it = contentProvider.iterator();

            while (it.hasNext()) {
                ByteBuffer next = it.next();
                byte[] bytes = new byte[next.remaining()];
                next.get(bytes);
                sb.append(new String(bytes, Charset.forName("UTF-8")));
            }

            final String requestBody = sb.toString();

            if (App.getInstance().getSharedPreferences().getBoolean("saveRequest", false)) {
                //SDカードのディレクトリパス
                File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/request/");

                //パス区切り用セパレータ
                String Fs = File.separator;

                String uri = request.getRequestURI();

                uri = uri.replaceAll("/", "=");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                //テキストファイル保存先のファイルパス
                String filePath = sdcard_path + Fs + sdf.format(Calendar.getInstance().getTime()) + "-" + uri + ".txt";

                //フォルダがなければ作成
                if (!sdcard_path.exists()) {
                    sdcard_path.mkdirs();
                }

                try {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "SJIS")));
                    pw.write(requestBody);
                    pw.flush();
                    pw.close();
                } catch (Exception e) {

                }
            }

            String regex = ".*/kcsapi/(.+)";
            Pattern p = Pattern.compile(regex);
            final Matcher m = p.matcher(request.getRequestURI());
            if (m.find()) {
                new Thread() {
                    @Override
                    public void run() {
                        Log.d("requestBody", requestBody);
                        RequestParser.parse(m.group(1), requestBody);
                    }
                }.start();
            }

            proxyRequest.content(new ByteBufferContentProvider(ByteBuffer.wrap(requestBody.getBytes())));

        } catch (Exception e) {
            //ContentProviderが空の場合はなにもしない
        }
    }

    /**
     * 艦これのレスポンスを確認するためのリスナ
     */
    class KancolleResponseListener extends ProxyResponseListener {
        StringBuilder responseBody;

        KancolleResponseListener(HttpServletRequest request, HttpServletResponse response) {
            super(request, response);
            responseBody = new StringBuilder();
        }

        @Override
        public void onComplete(Result result) {
            super.onComplete(result);

            String uri = String.valueOf(result.getRequest().getURI());

            String regex = ".+/kcsapi/(.+)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(uri);
            if (m.find()) {
                uri = m.group(1);
            } else {
                return;
            }

            Log.d("loglook", uri);

            String jsonStr;
            regex = "\\Asvdata=(.*\\z)";
            p = Pattern.compile(regex);
            m = p.matcher(responseBody);
            if (m.find()) {
                jsonStr = m.group(1);
            } else {
                //"svdata="が無い場合不必要なデータと判断
                return;
            }

            JsonParser.parse(uri, jsonStr);
        }

        @Override
        public void onContent(Response proxyResponse, ByteBuffer content, Callback callback) {
            ByteBuffer contentCopy = content.duplicate();
            super.onContent(proxyResponse, content, callback);

            byte[] byteArray = new byte[contentCopy.remaining()];
            contentCopy.get(byteArray);

            try {
                responseBody.append(new String(byteArray, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            /*
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);

                BufferedReader br = new BufferedReader(new InputStreamReader(bais, Charset.forName("UTF-8")));
                String line;
                while ((line = br.readLine()) != null) {
                    responseBody.append(line);
                }
            } catch (Exception e) {

            }
            */
        }
    }
}