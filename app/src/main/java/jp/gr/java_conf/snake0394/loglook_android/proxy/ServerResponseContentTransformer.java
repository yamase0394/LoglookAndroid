package jp.gr.java_conf.snake0394.loglook_android.proxy;

import android.util.Log;

import org.eclipse.jetty.proxy.AsyncMiddleManServlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.JsonParser;

/**
 * Created by snake0394 on 2016/11/02.
 */

public class ServerResponseContentTransformer implements AsyncMiddleManServlet.ContentTransformer {
    StringBuilder sb;
    final String uri;

    public ServerResponseContentTransformer(String requestURI) {
        super();
        this.uri = requestURI;
        sb = new StringBuilder();
    }
    
    @Override
    public void transform(ByteBuffer byteBuffer, boolean b, List<ByteBuffer> list) throws IOException {
        ByteBuffer contentCopy = byteBuffer.duplicate();

        byte[] byteArray = new byte[contentCopy.remaining()];
        contentCopy.get(byteArray);
        sb.append(new String(byteArray, "UTF-8"));

        list.add(byteBuffer);

        //サーバからのレスポンスが全て届いた
        if (b) {
            new Thread() {
                @Override
                public void run() {
                    onServerResponseComplete();
                }
            }.start();
        }
    }

    private void onServerResponseComplete() {
        String jsonStr;
        String regex = "svdata=(.+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sb.toString());
        if (m.find()) {
            jsonStr = m.group(1);
        } else {
            //"svdata="が無い場合不必要なデータと判断
            return;
        }

        Log.d("loglook", uri);

        JsonParser.parse(uri, jsonStr);
    }
}
