package andrey.shpilevoy.http_client

import java.io.File

class RequestParams {

    val map = HashMap<String, Any>()
    var json = false

    fun put(key: String, value: String){
        map[key] = value
    }
    fun put(key: String, value: Int){
        map[key] = value
    }
    fun put(key: String, value: Long){
        map[key] = value
    }
    fun put(key: String, value: Short){
        map[key] = value
    }
    fun put(key: String, value: Float){
        map[key] = value
    }
    fun put(key: String, value: Double){
        map[key] = value
    }
    fun put(key: String, value: File){
        map[key] = value
    }

    fun useJson(value: Boolean){
        json = value
    }

}