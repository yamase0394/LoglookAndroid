package jp.gr.java_conf.snake0394.loglook_android.proxy;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.atteo.classindex.ClassIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.api.API;
import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi;

/**
 * Created by snake0394 on 2017/02/16.
 */

public class APIListener implements ContentListenerSpi {

    private Map<String, List<APIListenerSpi>> listenerMap;
    private List<APIListenerSpi> allListenerList;

    public APIListener() {
        this.listenerMap = new HashMap<>();
        this.allListenerList = new ArrayList<>();

        for (Class<?> clazz : ClassIndex.getAnnotated(API.class)) {
            Log.d("annotated", clazz.getName());
            API target = clazz.getAnnotation(API.class);
            APIListenerSpi listener;

            try {
                listener = (APIListenerSpi) clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                continue;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            if (TextUtils.isEmpty(target.value()[0])) {
                allListenerList.add(listener);
                continue;
            }

            for (String uri : target.value()) {
                if (listenerMap.containsKey(uri)) {
                    listenerMap.get(uri)
                               .add(listener);
                } else {
                    List<APIListenerSpi> list = new ArrayList<>();
                    list.add(listener);
                    listenerMap.put(uri, list);
                }
            }
        }
    }

    @Override
    public boolean test(RequestMetaData requestMetaData) {
        return true;
    }

    @Override
    public void accept(RequestMetaData reqMetaData, ResponseMetaData resMetaData) {
        try {
            String serverResBody = IOUtils.toString(resMetaData.getResponseBody()
                                                                     .get(), "UTF-8");

            //svdata=を削除
            String jsonStr;
            Pattern p = Pattern.compile("svdata=(.+)");
            Matcher m = p.matcher(serverResBody);
            if (m.find()) {
                jsonStr = m.group(1);
            } else {
                return;
            }

            JsonObject json = new Gson().fromJson(jsonStr, JsonObject.class);

            this.send(json, reqMetaData, resMetaData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void send(JsonObject json, RequestMetaData reqMetaData, ResponseMetaData resMetaData) {

        if(listenerMap.containsKey(reqMetaData.getRequestURI())) {
            for (APIListenerSpi listener : listenerMap.get(reqMetaData.getRequestURI())) {
                if (listener != null) {
                    listener.accept(json, reqMetaData, resMetaData);
                }
            }
        }

        if(!allListenerList.isEmpty()) {
            for (APIListenerSpi listener : this.allListenerList) {
                if (listener != null) {
                    listener.accept(json, reqMetaData, resMetaData);
                }
            }
        }
    }
}
