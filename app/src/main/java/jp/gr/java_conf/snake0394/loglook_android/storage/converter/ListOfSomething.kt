package jp.gr.java_conf.snake0394.loglook_android.storage.converter

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type




/**
 * Created by snake0394 on 2017/07/21.
 */
class ListOfSomething<X> :ParameterizedType {
    private val wrapped: Class<*>

    constructor(wrapped: Class<X>) {
        this.wrapped = wrapped
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf<Type>(wrapped)
    }

    override fun getRawType(): Type {
        return  ArrayList::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }

}