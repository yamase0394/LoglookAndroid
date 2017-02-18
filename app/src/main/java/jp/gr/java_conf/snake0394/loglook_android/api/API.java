package jp.gr.java_conf.snake0394.loglook_android.api;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by snake0394 on 2017/02/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IndexAnnotated
public @interface API {

    /**
     * APIのURIを返します
     *
     * @return APIのURI
     */
    String[] value();
}
