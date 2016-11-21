package jp.gr.java_conf.snake0394.loglook_android.proxy;

import org.eclipse.jetty.proxy.ConnectHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by snake0394 on 2016/10/27.
 */

public class MyConnectHandler extends ConnectHandler {
    @Override
    protected void handleConnect(org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response, String serverAddress) {
        // 何か処理
        super.handleConnect(baseRequest, request, response, serverAddress);
    }
}