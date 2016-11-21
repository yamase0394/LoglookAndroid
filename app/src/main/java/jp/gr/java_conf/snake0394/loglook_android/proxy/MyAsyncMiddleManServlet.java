package jp.gr.java_conf.snake0394.loglook_android.proxy;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.AsyncMiddleManServlet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by snake0394 on 2016/11/02.
 */

public class MyAsyncMiddleManServlet extends AsyncMiddleManServlet {

    /*
    @Override
    protected ContentTransformer newClientRequestContentTransformer(HttpServletRequest clientRequest, Request proxyRequest) {
        return new ServerResponseContentTransformer();
    }
    */

    @Override
    protected void addProxyHeaders(HttpServletRequest clientRequest, Request proxyRequest) {
        //ヘッダーを空に
    }

    @Override
    protected ContentTransformer newClientRequestContentTransformer(HttpServletRequest clientRequest, Request proxyRequest) {
        String regex = "/kcsapi/(.+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(clientRequest.getRequestURI());
        if (m.find()) {
            return new ClientRequestContentTransformer(m.group(1));
        }

        return super.newClientRequestContentTransformer(clientRequest, proxyRequest);
    }

    @Override
    protected ContentTransformer newServerResponseContentTransformer(HttpServletRequest clientRequest, HttpServletResponse proxyResponse, Response serverResponse) {
        String regex = "/kcsapi/(.+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(clientRequest.getRequestURI());
        if (m.find()) {
            return new ServerResponseContentTransformer(m.group(1));
        }

        return super.newServerResponseContentTransformer(clientRequest, proxyResponse, serverResponse);
    }

}

