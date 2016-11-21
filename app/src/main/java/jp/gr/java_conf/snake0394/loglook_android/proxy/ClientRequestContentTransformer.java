package jp.gr.java_conf.snake0394.loglook_android.proxy;

import android.util.Log;

import org.eclipse.jetty.proxy.AsyncMiddleManServlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.RequestParser;

/**
 * Created by snake0394 on 2016/11/02.
 */

public class ClientRequestContentTransformer implements AsyncMiddleManServlet.ContentTransformer {
    StringBuilder sb;
    final String uri;

    public ClientRequestContentTransformer(String requestURI) {
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

        if (b) {
            new Thread() {
                @Override
                public void run() {
                    onClientRequestComplete();
                }
            }.start();
        }
    }

    private void onClientRequestComplete() {
        String requestBody = sb.toString();
        Log.d("requestBody", requestBody);

        RequestParser.parse(uri, requestBody);
    }
}
