package jp.gr.java_conf.snake0394.loglook_android.proxy;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.atteo.classindex.ClassIndex;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.api.API;
import jp.gr.java_conf.snake0394.loglook_android.api.APIListenerSpi;

/**
 * Created by snake0394 on 2017/02/16.
 */

public class APIListener implements ContentListenerSpi{

    private Map<String, APIListenerSpi> listenerMap;

    public APIListener(){
        this.listenerMap = new HashMap<>();

        for (Class<?> clazz : ClassIndex.getAnnotated(API.class)) {
            Log.d("annotated", clazz.getName());
            API target = clazz.getAnnotation(API.class);
            for (String uri : target.value()) {
                try {
                    listenerMap.put(uri, (APIListenerSpi) clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        /*
        Reflections reflections = new Reflections("jp.gr.java_conf.snake0394.loglook_android.api");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(API.class);
        Log.d("annotated", annotated.toString());
        for (Class<?> clazz : annotated) {
            API target = clazz.getAnnotation(API.class);
            for (String uri : target.value()) {
                listenerMap.put(uri, APIListenerSpi.class.cast(clazz));
            }
        }
        */
    }

    @Override
    public boolean test(RequestMetaData requestMetaData) {
        return true;
    }

    @Override
    public void accept(RequestMetaData reqMetaData, ResponseMetaData resMetaData) {
        try {
            final String serverResBody = IOUtils.toString(resMetaData.getResponseBody().get(), "UTF-8");
            String jsonStr;
            String regex = "svdata=(.+)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(serverResBody);
            if (m.find()) {
                jsonStr = m.group(1);
            } else {
                //"svdata="が無い場合不必要なデータと判断
                return;
            }

            JsonObject json = new Gson().fromJson(jsonStr, JsonObject.class);

            this.send(json, reqMetaData, resMetaData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(JsonObject json, RequestMetaData reqMetaData, ResponseMetaData resMetaData) {
        Log.d("reqURI", reqMetaData.getRequestURI());
        APIListenerSpi listener = listenerMap.get(reqMetaData.getRequestURI());
        if (listener != null) {
            Log.d("listener", listener.toString());
            listener.accept(json, reqMetaData, resMetaData);
        }
    }
}
