package jp.gr.java_conf.snake0394.loglook_android.storage

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class RealmInt : RealmObject {
    var value:Int = -1

    constructor()

    constructor(value: Int){
        this.value = value
    }
}